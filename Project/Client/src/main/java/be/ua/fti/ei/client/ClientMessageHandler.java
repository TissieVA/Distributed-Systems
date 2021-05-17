package be.ua.fti.ei.client;

import be.ua.fti.ei.utils.sockets.*;
import be.ua.fti.ei.utils.http.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
            logger.info("Nameserver message received");
            
            NameServerResponseBody nsrb = gson.fromJson(msg, NameServerResponseBody.class);
            String nameServerAddress = "http://" + ip + ":" + nsrb.getPort();
            Node.getClient().setNameServerAddress(nameServerAddress);
            try
            {
                sendAddNodeRestRequest();
            } catch (JsonProcessingException e)
            {
                logger.error(e.getMessage());
            }
        }
        else if(sb.getType().equals("update"))
        {
            logger.info("Update message received");

            String nameServerAddress = Node.getClient().getNameServerAddress() + "/replicates/" +
                    Node.getClient().getName();

            ArrayList<FileBody> files = (ArrayList<FileBody>) HttpRequester.GET(nameServerAddress, ArrayList.class);

            if (files != null && !files.isEmpty())
            for (FileBody file : files)
            {
                try
                {
                    Node.getFileTransferSocket().downloadFile(file.getNode().getIpaddress(), file.getNode().getFilePort(),
                            file.getFilename());
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage());
                }
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
        logger.info("send publish request");

        PublishBody pb = new PublishBody(Node.getClient().getName(), Node.getClient().getFiles(),
                Node.getClient().getIpaddress(), Node.getClient().getMulticastPort(),
                Node.getClient().getFileTransferPort());

        String json = gson.toJson(pb);

        NextPreviousBody np = (NextPreviousBody) HttpRequester.
                POST(Node.getClient().getNameServerAddress() + "/publish", json, NextPreviousBody.class);

        Node.getClient().setNextId(np.getNext());
        Node.getClient().setPreviousId(np.getPrevious());
    }

    public NodeBody sendFindFile(String filename)
    {
        logger.info("send find file request");

        NodeBody nb = (NodeBody) HttpRequester.GET(Node.getClient().getNameServerAddress() + "/find/" + filename,
                 NodeBody.class);

        logger.info("File on" + nb.getName());

        return nb;
    }

    public void downloadFile(String filename)
    {
        logger.info("Download file request");
        NodeBody nb = this.sendFindFile(filename);

        try
        {
            Node.getFileTransferSocket().downloadFile(nb.getIpaddress(), nb.getFilePort(), filename);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
