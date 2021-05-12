package be.ua.fti.ei;

import be.ua.fti.ei.sockets.*;
import be.ua.fti.ei.http.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientMessageHandler implements MessageHandler
{
    private static final Logger logger = LoggerFactory.getLogger(MulticastSocketServer.class);
    private static Gson gson;
    private MulticastSocketServer mss;


    @Override
    public void setServer(MulticastSocketServer mss)
    {
        this.mss = mss;
        logger.info("Server set");
    }

    @Override
    public void onServerStart()
    {
        logger.info("sendFindNS");
        sendFindNS();
    }

    @Override
    public void parse(SocketBody sb, String msg, String ip, int port)
    {
        if(sb.getType().equals("ns"))
        {
            logger.info("ns message received");

            NameServerResponseBody nsrb = gson.fromJson(msg, NameServerResponseBody.class);
            String NameServerAddress = "http://" + ip + ":" + nsrb.getPort();
            Node.getClient().setNameServerAddress(NameServerAddress);
            try
            {
                sendAddNodeRestRequest();
            } catch (JsonProcessingException e)
            {
                logger.error(e.getMessage());
            }
        }
    }

    public void sendFindNS()
    {
        gson = new Gson();

        SocketBody sb = new SocketBody();
        sb.setType("find");

        String msg = gson.toJson(sb);
        logger.info("send where is ns message");
        logger.info(msg);
        this.mss.sendMessage(msg);
    }


    public void sendAddNodeRestRequest() throws JsonProcessingException
    {
        logger.info("sen publish request");
        PublishBody pb = new PublishBody(Node.getClient().getName(), Node.getClient().getFiles(),
                Node.getClient().getIpaddress(), Node.getClient().getMulticastPort(),
                Node.getClient().getFileTransferPort());

        String json = gson.toJson(pb);

        NextPreviousBody np = (NextPreviousBody) HttpRequester.
                POST(Node.getClient().getNameServerAddress() + "/publish", json, NextPreviousBody.class);

        Node.getClient().setNextId(np.getNext());
        Node.getClient().setPreviousId(np.getPrevious());
    }
}
