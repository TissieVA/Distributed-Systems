package be.ua.fti.ei.client;

import be.ua.fti.ei.utils.sockets.MulticastSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static FileServer fileServer;
    public static FileServer getFileServer() {return fileServer;}

    private static DirectoryUpdateCheck duc;

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
            Node.fileServer = new FileServer(Node.client.getFileTransferPort());
            fileServer.start();

            Node.duc = new DirectoryUpdateCheck(Paths.get(System.getProperty("user.dir"),"replicates/"));


        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());

            // When errors occur at startup, stop the server
            return;
        }

        // Start the multicast socket

        Node.multicastSocket.getStartThread().start();



        logger.info(Node.client.getName() + " started successfully!");
    }
}
