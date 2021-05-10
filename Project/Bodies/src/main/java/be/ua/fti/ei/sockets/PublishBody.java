package be.ua.fti.ei.sockets;
import java.util.ArrayList;

public class PublishBody extends SocketBody implements java.io.Serializable
{
    String hostname;
    ArrayList<String> files;
    String ipAddress;

    public PublishBody() {super("find"); }

    public PublishBody(String hostname, ArrayList<String> files, String ipAddress)
    {
        //  [Ander project] (zelfde project)
        //  [MulticastSocket] - [A: MessageHandler] - (NodeMessageHandler)
        super("find");
        this.hostname = hostname;
        this.files = files;
        this.ipAddress = ipAddress;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public ArrayList<String> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<String> files) {
        this.files = files;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
