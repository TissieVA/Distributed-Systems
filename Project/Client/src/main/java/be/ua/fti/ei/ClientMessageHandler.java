package be.ua.fti.ei;

import be.ua.fti.ei.sockets.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;

public class ClientMessageHandler implements MessageHandler
{
    private static Gson gson;
    private MulticastSocketServer mss;

    @Override
    public void parse(SocketBody sb, String msg, String ip, int port)
    {
        if(sb.getType().equals("ns"))
        {
            NameServerResponseBody nsrb = gson.fromJson(msg, NameServerResponseBody.class);
        }
    }


    @Override
    public void setServer(MulticastSocketServer mss)
    {
        this.mss = mss;
    }

    public void sendFindNS()
    {
        gson = new Gson();

        SocketBody sb = new SocketBody();
        sb.setType("find");

        String msg = gson.toJson(sb);

        this.mss.sendMessage(msg);

    }


    public void sendAddNodeRestRequest() throws JsonProcessingException
    {
        PublishBody pb = new PublishBody(Node.getClient().getName(), Node.getClient().getFiles(), Node.getClient().getIpaddress());
        String json = gson.toJson(pb);

        HttpRequester.POST(Node.getClient().getNameServerAddress() + "/publish", json);
    }
}
