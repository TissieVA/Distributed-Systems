package be.ua.fti.ei.utils.http;

import be.ua.fti.ei.utils.Hasher;

public class NodeBody
{
    private String name;
    private String ipaddress;
    private int filePort;
    private int mcPort;

    public NodeBody(String name, String ipaddress, int mcPort, int filePort)
    {
        this.name = name;
        this.ipaddress = ipaddress;
        this.filePort = filePort;
        this.mcPort = mcPort;
    }

    public NodeBody()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getHash()
    {
        return Hasher.getHash(this.name);
    }

    public String getIpaddress()
    {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress)
    {
        this.ipaddress = ipaddress;
    }

    public int getFilePort()
    {
        return filePort;
    }

    public void setFilePort(int filePort)
    {
        this.filePort = filePort;
    }

    public int getMcPort()
    {
        return mcPort;
    }

    public void setMcPort(int mcPort)
    {
        this.mcPort = mcPort;
    }
}
