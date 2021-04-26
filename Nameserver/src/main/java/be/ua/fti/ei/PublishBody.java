package be.ua.fti.ei;

import java.util.ArrayList;

public class PublishBody
{
    String hostname;
    ArrayList<String> files;

    public PublishBody() {
    }

    public PublishBody(String hostname, ArrayList<String> files) {
        this.hostname = hostname;
        this.files = files;
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
}
