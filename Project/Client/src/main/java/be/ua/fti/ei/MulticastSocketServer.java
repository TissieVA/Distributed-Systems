package be.ua.fti.ei;

import be.ua.fti.ei.sockets.NameServerResponseBody;
import be.ua.fti.ei.sockets.SocketBody;

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
    private InetAddress address;
    private final int port;
    private boolean running;

    /**
     * @param multicastIp This is an IPv4 address in the multicast range (224.0.0.0 - 239.255.255.255)
     * @param port This is the port the socket server listens on
     */
    public MulticastSocketServer(String multicastIp, int port) throws Exception
    {
        this.gson = new Gson();

        this.address = InetAddress.getByName(multicastIp);
        this.port = port;

        this.socket = new MulticastSocket(this.port);
        InetSocketAddress address = new InetSocketAddress(this.address, this.port);
        this.socket.joinGroup(address, NetworkInterface.getByIndex(0));
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

        while(this.running)
        {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);

            try
            {
                this.socket.receive(packet);
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

            if(body.getType().equals("ns"))
            {
                NameServerResponseBody nsbody = this.gson.fromJson(received, NameServerResponseBody.class);

                // Debug this code!!!!!!
                String nsurl = "http://" + ip.getHostAddress() + ":" + nsbody.getPort();
                Node.getClient().setNameServerAddress(nsurl);

                // 1. Find files on this server
                // 2. Publish (HttpRequester)
                // PublishBody pb = new PublishBody();
                // HttpRequester.httpRequestPOST(Node.getClient().getNameServerAddress() + "/publish",
                //         this.gson.toJson(pb));

                // NS Socket -->udp-> RENEGOTIATE
            }
        }

        this.socket.close();
    }

    /**
     * Send a multicast message to find the Name Server
     */
    public void findNS()
    {
        SocketBody sb = new SocketBody();
        sb.setType("find");

        String msg = this.gson.toJson(sb);
        byte[] buf = msg.getBytes(StandardCharsets.UTF_8);

        DatagramPacket packet = new DatagramPacket(buf, buf.length, this.address, this.port);

        try
        {
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
