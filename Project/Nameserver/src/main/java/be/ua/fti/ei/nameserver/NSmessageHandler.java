package be.ua.fti.ei.nameserver;

import be.ua.fti.ei.utils.sockets.*;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NSmessageHandler implements MessageHandler
{
    private static final Logger logger = LoggerFactory.getLogger(be.ua.fti.ei.utils.sockets.MulticastSocketServer.class);
    private static Gson gson;
    private be.ua.fti.ei.utils.sockets.MulticastSocketServer mss;

    @Override
    public void setServer(MulticastSocketServer mss)
    {
        logger.info("Socketserver set");
        this.mss = mss;
    }

    @Override
    public void onServerStart(){}

    @Override
    public void parse(SocketBody sb, String msg, String ip, int port)
    {
        if(sb.getType().equals("find"))
        {
            logger.info("find message received");
            sendNS(ip, port);
        }
    }

    public void sendNS(String ip, int port)
    {
        gson = new Gson();

        NameServerResponseBody nsb = new NameServerResponseBody(8080);

        String msg = gson.toJson(nsb);
        logger.info("send this is nameserver message");
        this.mss.sendUnicastMessage(msg,ip,port);

    }

    /**
     *
     * @param nodeId hash of either ne node or node to be deleted
     * @param deleteNodeOrNewNode true if it is a node we want to delete, false if it is a newly added node
     */
    public void updateNeighbours(int nodeId, boolean deleteNodeOrNewNode)
    {
        logger.info("Updating neighbouring nodes");
        gson = new Gson();

        int nextID = Database.getInstance().getHigherNeighbour(nodeId);
        int prevID = Database.getInstance().getLowerNeighbour(nodeId);

        Node next = Database.getInstance().getHostDatabase().get(nextID);
        Node prev = Database.getInstance().getHostDatabase().get(prevID);

        if(deleteNodeOrNewNode)
            Database.getInstance().removeNode(nodeId);

        // Send message to higherNeighbour to update its neighbours
        NextPreviousBody toHigherNeighbour = Database.getInstance().getNeighbours(nextID);
        String msg = gson.toJson(toHigherNeighbour);

        this.mss.sendUnicastMessage(msg, next.getIpaddress(), next.getMcPort());

        // Send message to lowerNeighbour to update its neighbours
        NextPreviousBody toLowerNeighbour = Database.getInstance().getNeighbours(prevID);
        msg = gson.toJson(toLowerNeighbour);

        this.mss.sendUnicastMessage(msg, prev.getIpaddress(), prev.getMcPort());
    }

    public void update()
    {
        logger.info("Sending update message");
        SocketBody sb = new SocketBody("update");
        String msg = gson.toJson(sb);

        // Send the message to every unique port that can be found in the nodes DB
        Database.getInstance().getHostDatabase().values().stream().map(Node::getMcPort).distinct().forEach(port ->
                this.mss.sendMessage(msg, port));

    }

    private static NSmessageHandler instance;

    public static NSmessageHandler getInstance()
    {
        if(instance == null)
            instance = new NSmessageHandler();

        return instance;
    }
}
