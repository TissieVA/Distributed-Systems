package be.ua.fti.ei.client;

import be.ua.fti.ei.utils.sockets.MulticastSocketServer;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@SpringBootApplication
public class Node
{
    private static final Logger logger = LoggerFactory.getLogger(Node.class);

    private static Client client;
    public static Client getClient()
    {
        return Node.client;
    }

    private static MulticastSocketServer multicastSocket;
    private static FileTransferSocket fileSocket;
    public static FileTransferSocket getFileTransferSocket() {return fileSocket; }
    //private static FileClient fileClient;
    private static FileServer fileServer;
    public static FileServer getFileServer() {return fileServer;}

    public static void main(String[] args)
    {
        // Start spring application
        new SpringApplicationBuilder(Node.class)
                .web(WebApplicationType.NONE)
                .run(args);

        try
        {
            Node.client = new Client(NodeConfig.load("config.json"));

            File dir = new File("files");
            ArrayList<String> files = (ArrayList<String>) Arrays.stream(dir.list()).collect(Collectors.toList());
            Node.client.setFiles(files);

            ClientMessageHandler cmh = new ClientMessageHandler();
            // In IPv4: any address from 224.0.0.0 -> 239.255.255.255 can be used as a multicast address
            // Meaning anyone who joins the same multicast ip-group can receive these messages
            Node.multicastSocket = new MulticastSocketServer("230.0.0.7", Node.client.getMulticastPort(),
                    new ClientMessageHandler());
            //Node.fileSocket = new FileTransferSocket(Node.client.getFileTransferPort());
            Node.fileServer = new FileServer(6667);
            fileServer.start();

        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());

            // When errors occur at startup, stop the server
            return;
        }

        // Start the multicast socket

        Node.multicastSocket.getStartThread().start();
        // Search NameServer
        //Node.socket.findNS();

        // Start the file socket
        Node.fileSocket.getStartThread().start();


        logger.info(Node.client.getName() + " started successfully!");
    }
}
