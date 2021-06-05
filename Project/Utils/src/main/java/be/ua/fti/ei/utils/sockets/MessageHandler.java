package be.ua.fti.ei.utils.sockets;

public interface MessageHandler
{
    void parse(SocketBody sb, String msg, String ip, int port);
    void setServer(MulticastSocketServer mss);
    void onServerStart();
}
