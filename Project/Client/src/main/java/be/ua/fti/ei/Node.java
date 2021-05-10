package be.ua.fti.ei;

import be.ua.fti.ei.sockets.MulticastSocketServer;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

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

    public static void main(String[] args)
    {
        // Start spring application
        new SpringApplicationBuilder(Node.class)
                .web(WebApplicationType.NONE)
                .run(args);

        try
        {
            ArrayList<String> files = new ArrayList<>(Arrays.asList("abc.txt", "def.txt"));
            // Instantiate client info & socket
            Node.client = new Client("TestNode", InetAddress.getLocalHost().getHostAddress(), files);

            // In IPv4: any address from 224.0.0.0 -> 239.255.255.255 can be used as a multicast address
            // Meaning anyone who joins the same multicast ip-group can receive these messages
            Node.multicastSocket = new MulticastSocketServer("230.0.0.7", 6666, new ClientMessageHandler());

            Node.fileSocket = new FileTransferSocket(6665);

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
