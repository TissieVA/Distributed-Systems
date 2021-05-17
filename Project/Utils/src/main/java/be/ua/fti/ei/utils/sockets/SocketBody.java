package be.ua.fti.ei.utils.sockets;

public class SocketBody implements java.io.Serializable
{
    private String type;

    public SocketBody() { }

    public SocketBody(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
