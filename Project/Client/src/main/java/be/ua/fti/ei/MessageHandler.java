package be.ua.fti.ei;

import be.ua.fti.ei.sockets.PublishBody;
import be.ua.fti.ei.sockets.SocketBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class MessageHandler
{

    public static void sendFindNS()
    {
        Gson gson = new Gson();

        SocketBody sb = new SocketBody();
        sb.setType("find");

        String msg = gson.toJson(sb);
        // multisocketserver  sendmessage
    }


    public static void sendAddNodeRestRequest() throws JsonProcessingException
    {
        PublishBody pb = new PublishBody(Node.getClient().getName(), Node.getClient().getFiles(), Node.getClient().getIpaddress());

        ObjectMapper mapper = new ObjectMapper();

        String pbJSON = mapper.writeValueAsString(pb);
        HttpRequester.httpRequestPOST(Node.getClient().getNameServerAddress() + "/publish", pbJSON);

    }
}
