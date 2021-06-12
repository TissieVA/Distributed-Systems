package be.ua.fti.ei.nameserver;

import be.ua.fti.ei.utils.sockets.MulticastSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server
{
    private static MulticastSocketServer multicastSocket;
    private static final Logger logger = LoggerFactory.getLogger(Server.class);


    public static void main(String[] args)
    {
        SpringApplication.run(Server.class, args);
        try
        {
            Server.multicastSocket = new MulticastSocketServer("230.0.0.7", 6667,
                    NSmessageHandler.getInstance());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        logger.info("Start Thread");
        Server.multicastSocket.getStartThread().start();
    }
    public static MulticastSocketServer getMulticastSocket()
    {
        return Server.multicastSocket;
    }
}
