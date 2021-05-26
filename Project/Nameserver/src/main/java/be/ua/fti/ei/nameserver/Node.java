package be.ua.fti.ei.nameserver;

import be.ua.fti.ei.utils.http.NodeBody;

import java.util.ArrayList;
import java.util.List;

public class Node implements java.io.Serializable
{
    private String name;
    private String ipaddress;

    private int mcPort;
    private int filePort;

    private ArrayList<String> files;

    public Node()
    {
        this.files = new ArrayList<>();
    }

    public Node(String name, String ipaddress, int mcPort, int filePort)
    {
        this.name = name;
        this.ipaddress = ipaddress;
        this.mcPort = mcPort;
        this.filePort = filePort;
        this.files = new ArrayList<>();
    }

    public String getIpaddress()
    {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void addFiles(List<String> files)
    {
        this.files.addAll(files);
    }

    public ArrayList<String> getFiles()
    {
        return this.files;
    }

    public void setFiles(ArrayList<String> files)
    {
        this.files = files;
    }

    public NodeBody getBody()
    {
        return new NodeBody(this.name, this.ipaddress, this.filePort);
    }
}
