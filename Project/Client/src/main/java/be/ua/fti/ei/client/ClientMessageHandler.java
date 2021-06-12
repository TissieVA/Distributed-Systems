package be.ua.fti.ei.client;


import be.ua.fti.ei.utils.http.HttpRequester;
import be.ua.fti.ei.utils.http.FileBody;
import be.ua.fti.ei.utils.http.NodeBody;
import be.ua.fti.ei.utils.http.PublishBody;
import be.ua.fti.ei.utils.sockets.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        sendFindNS(6667);
    }

    @Override
    public void parse(SocketBody sb, String msg, String ip, int port)
    {
        switch (sb.getType())
        {
            case "ns":
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
                break;
            }
            case "update":
            {
                logger.info("Update message received");

                String nameServerAddress = Node.getClient().getNameServerAddress() + "/replicates/" +
                        Node.getClient().getName();

                List<FileBody> files = HttpRequester.GETList(nameServerAddress, FileBody[].class);


                if (files != null && !files.isEmpty())
                    for (FileBody file : files)
                    {

                        if (!file.getNode().getName().equals(Node.getClient().getName()))
                        {
                            try
                            {
                                Node.getFileServer().setReceived(false);
                                logger.info("ask for: " + file.getFilename());

                                // Tell the server the file wanting to receive
                                Node.getFileServer().setFileName(file.getFilename());

                                // Send a unicast to the node containing the file that it should sent the file to this Node
                                // (details of this node in frb)
                                FileRequestBody frb = new FileRequestBody(file.getFilename(), Node.getClient().getIpaddress(), Node.getClient().getFileTransferPort());
                                this.mss.sendUnicastMessage(gson.toJson(frb), file.getNode().getIpaddress(), file.getNode().getMcPort());

                                long pastTime = System.currentTimeMillis();
                                while (!Node.getFileServer().isReceived())
                                {
                                    long time = System.currentTimeMillis();
                                    if (time >= (pastTime + 10 * 1000))
                                    {
                                        logger.error("10 seconds past since the request of " + file.getFilename());
                                        break;
                                    }
                                }

                            } catch (Exception e)
                            {
                                logger.error(e.getMessage());
                            }
                        }
                    }
                break;
            }
            case "file":
                logger.info("File message received");
                FileRequestBody frb = gson.fromJson(msg, FileRequestBody.class);
                logger.info("Sending file: " + frb.getFilename());
                FileClient fileClient = new FileClient(frb.getMyIpaddress(), frb.getFilePort(), "files/" + frb.getFilename());
                break;

            default:
                logger.warn("Message type not recognised, message type: " + sb.getType());
                break;
        }
    }

    public void sendFindNS(int port)
    {
        gson = new Gson();

        SocketBody sb = new SocketBody();
        sb.setType("find");

        String msg = gson.toJson(sb);
        logger.info("send where is ns message");
        logger.info(msg);
        this.mss.sendMessage(msg, port);
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

        logger.info("Http responds received");
        Node.getClient().setNextId(np.getNextId());
        Node.getClient().setPreviousId(np.getPreviousId());
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
            Node.getFileServer().setReceived(false);
            logger.info("ask for: " + filename);

            // Tell the server the file wanting to receive
            Node.getFileServer().setFileName(filename);

            // Send a unicast to the node containing the file that it should sent the file to this Node
            // (details of this node in frb)
            FileRequestBody frb = new FileRequestBody(filename,Node.getClient().getIpaddress(), Node.getClient().getFileTransferPort());
            this.mss.sendUnicastMessage(gson.toJson(frb), nb.getIpaddress(),nb.getMcPort());

            long pastTime = System.currentTimeMillis();
            while(!Node.getFileServer().isReceived())
            {
                long time = System.currentTimeMillis();
                if(time >= (pastTime + 10*1000))
                {
                    logger.error("10 seconds past since the request of "+ filename);
                    break;
                }
            }

        } catch (Exception e)
        {
            logger.error(e.getMessage());
        }

    }
}
