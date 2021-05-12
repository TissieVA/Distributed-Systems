package be.ua.fti.ei.http;
import java.util.ArrayList;

public class PublishBody implements java.io.Serializable
{
    String hostname;
    ArrayList<String> files;
    String ipAddress;

    int mcPort;
    int filePort;

    public PublishBody()
    {
        super();
    }

    public PublishBody(String hostname, ArrayList<String> files, String ipAddress, int mcPort, int filePort)
    {
        super();

        this.hostname = hostname;
        this.files = files;
        this.ipAddress = ipAddress;
        this.mcPort = mcPort;
        this.filePort = filePort;
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

    public int getMcPort()
    {
        return mcPort;
    }

    public void setMcPort(int mcPort)
    {
        this.mcPort = mcPort;
    }

    public int getFilePort()
    {
        return filePort;
    }

    public void setFilePort(int filePort)
    {
        this.filePort = filePort;
    }
}
