package be.ua.fti.ei.utils.sockets;

public class NameServerResponseBody extends SocketBody implements java.io.Serializable
{
    private int port;

    public NameServerResponseBody()
    {
        super("ns");
    }

    public NameServerResponseBody(int port)
    {
        super("ns");
        this.port = port;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }
}
