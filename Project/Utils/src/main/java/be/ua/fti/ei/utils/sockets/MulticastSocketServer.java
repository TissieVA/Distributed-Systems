package be.ua.fti.ei.utils.sockets;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.*;
import java.nio.charset.StandardCharsets;


public class MulticastSocketServer
{
    private static final Logger logger = LoggerFactory.getLogger(MulticastSocketServer.class);
    private final Gson gson;
    private final MulticastSocket socket;
    private final MessageHandler messageHandler;

    private InetAddress address;
    private final int port;
    private boolean running;

    /**
     * @param multicastIp This is an IPv4 address in the multicast range (224.0.0.0 - 239.255.255.255)
     * @param port This is the port the socket server listens on
     */
    public MulticastSocketServer(String multicastIp, int port, MessageHandler messageHandler) throws Exception
    {
        this.gson = new Gson();

        this.address = InetAddress.getByName(multicastIp);
        this.port = port;

        this.messageHandler = messageHandler;
        this.messageHandler.setServer(this);

        this.socket = new MulticastSocket(this.port);

        InetSocketAddress inetSocketAddress = new InetSocketAddress(this.address, this.port);
        this.socket.joinGroup(inetSocketAddress, NetworkInterface.getByName("ethwe0"));

    }

    /**
     * Get a thread that starts the socket server
     * @return The start thread
     */
    public Thread getStartThread()
    {
        return new StartSocketThread(this);
    }

    /**
     * Start the socket server
     */
    void startServer()
    {
        this.running = true;

        // When things break increase this value
        byte[] buf = new byte[256];

        this.messageHandler.onServerStart();

        logger.info("Listening");
        while(this.running)
        {
            logger.info("Started Listening loop");
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try
            {
                this.socket.receive(packet);
                logger.info("Receiving");
            }
            catch(Exception ex)
            {
                logger.error(ex.getMessage());
                continue;
            }

            InetAddress ip = packet.getAddress();
            int port = packet.getPort();

            String received = new String(packet.getData(), 0, packet.getLength());
            SocketBody body = this.gson.fromJson(received, SocketBody.class);
            logger.info("Received message type: " + body.getType());
            //this.messageHandler.parse(body, received, ip.getHostAddress(), port);
            new ParseThread(body, received, ip.getHostAddress(), port, this.messageHandler).start();

            logger.info("Ended Listening loop");
        }

        logger.info("Socket Closed");
        this.socket.close();
    }

    /**
     * Send a multicast message
     */
    public void sendMessage(String msg)
    {
        byte[] buf = msg.getBytes(StandardCharsets.UTF_8);

        DatagramPacket packet = new DatagramPacket(buf, buf.length, this.address, 6667);

        try
        {
            logger.info("Multicast sent");
            this.socket.send(packet);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }
    }

    /**
     * Send a unicast message
     */
    public void sendUnicastMessage(String msg, String ipaddress, int port)
    {
        byte[] buf = msg.getBytes(StandardCharsets.UTF_8);

        try
        {
            logger.info("Unicast sent");
            DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(ipaddress), port);
            this.socket.send(packet);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
        }
    }



    /**
     * Stop the socket server
     */
    public void close()
    {
        this.running = false;
    }

    public InetAddress getAddress()
    {
        return this.address;
    }

    public void setAddress(InetAddress address)
    {
        this.address = address;
    }
}

class StartSocketThread extends Thread
{
    MulticastSocketServer socket;

    StartSocketThread(MulticastSocketServer socket)
    {
        this.socket = socket;
    }

    public void run()
    {
        this.socket.startServer();
    }
}

class ParseThread extends Thread
{
    MessageHandler mh;
    SocketBody body;
    String received;
    String ip;
    int port;

    public ParseThread(SocketBody body, String received, String ip, int port, MessageHandler mh)
    {
        this.mh = mh;
        this.body = body;
        this.received = received;
        this.port = port;
    }

    public void run()
    {
        this.mh.parse(body, received, ip, port);
    }
}