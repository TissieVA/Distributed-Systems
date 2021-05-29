package be.ua.fti.ei.nameserver;

import be.ua.fti.ei.utils.http.NodeBody;

public class Node implements java.io.Serializable
{
    private String name;
    private String ipaddress;

    private int mcPort;
    private int filePort;

    public Node() { }

    public Node(String name, String ipaddress, int mcPort, int filePort)
    {
        this.name = name;
        this.ipaddress = ipaddress;
        this.mcPort = mcPort;
        this.filePort = filePort;
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

    public NodeBody getBody()
    {
        return new NodeBody(this.name, this.ipaddress, this.mcPort, this.filePort);
    }
}
