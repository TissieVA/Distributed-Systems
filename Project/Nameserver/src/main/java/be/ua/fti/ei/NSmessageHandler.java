package be.ua.fti.ei;


import be.ua.fti.ei.sockets.MulticastSocketServer;
import be.ua.fti.ei.sockets.*;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NSmessageHandler implements MessageHandler
{
    private static final Logger logger = LoggerFactory.getLogger(be.ua.fti.ei.sockets.MulticastSocketServer.class);
    private static Gson gson;
    private be.ua.fti.ei.sockets.MulticastSocketServer mss;

    @Override
    public void setServer(MulticastSocketServer mss)
    {
        this.mss = mss;
    }

    @Override
    public void onServerStart(){}

    @Override
    public void parse(SocketBody sb, String msg, String ip, int port)
    {
        if(sb.getType().equals("find"))
        {
            sendNS();
        }


    }

    public void sendNS()
    {
        gson = new Gson();

        NameServerResponseBody nsb = new NameServerResponseBody(8080);

        String msg = gson.toJson(nsb);

        this.mss.sendMessage(msg);

    }

    public void update()
    {
        gson = new Gson();

        NextPreviousBody nxtprv = new NextPreviousBody();

        String msg = gson.toJson(nxtprv);

        this.mss.sendMessage(msg);
    }
}
