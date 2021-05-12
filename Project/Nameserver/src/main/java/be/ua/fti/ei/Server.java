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
            Server.multicastSocket = new MulticastSocketServer("230.0.0.7", 6666,
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
