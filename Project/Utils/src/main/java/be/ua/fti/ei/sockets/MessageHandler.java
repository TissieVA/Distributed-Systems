package be.ua.fti.ei.sockets;

public interface MessageHandler
{
    void parse(SocketBody sb, String msg, String ip, int port);
    void setServer(MulticastSocketServer mss);
}
