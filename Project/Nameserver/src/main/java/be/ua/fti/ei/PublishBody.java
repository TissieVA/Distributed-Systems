package be.ua.fti.ei;
import java.util.ArrayList;

public class PublishBody
{
    String hostname;
    ArrayList<String> files;
    String ipAddress;

    public PublishBody() { }

    public PublishBody(String hostname, ArrayList<String> files, String ipAddress) {
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
