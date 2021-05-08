package be.ua.fti.ei;

import be.ua.fti.ei.sockets.PublishBody;
import be.ua.fti.ei.sockets.SocketBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

public class MessageHandler
{
    private static Gson gson;

    public static void sendFindNS()
    {
        gson = new Gson();

        SocketBody sb = new SocketBody();
        sb.setType("find");

        String msg = gson.toJson(sb);

        // multisocketserver  sendmessage
    }


    public static void sendAddNodeRestRequest() throws JsonProcessingException
    {
        PublishBody pb = new PublishBody(Node.getClient().getName(), Node.getClient().getFiles(), Node.getClient().getIpaddress());
        String json = gson.toJson(pb);

        HttpRequester.POST(Node.getClient().getNameServerAddress() + "/publish", json);

    }
}
