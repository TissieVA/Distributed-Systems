package be.ua.fti.ei;

import be.ua.fti.ei.sockets.MulticastSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server
{
    private static MulticastSocketServer multicastSocket;

    public static void main(String[] args)
    {
        SpringApplication.run(Server.class, args);

        try
        {
            Server.multicastSocket = new MulticastSocketServer("224.0.0.1", 6667,
                    NSmessageHandler.getInstance());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static MulticastSocketServer getMulticastSocket()
    {
        return Server.multicastSocket;
    }
}
