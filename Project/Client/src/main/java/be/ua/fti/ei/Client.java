package be.ua.fti.ei;

import java.net.NetworkInterface;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Client
{
    private String name;
    private String ipaddress;
    private ArrayList<String> files;
    private int id;
    private int previousId;
    private int nextId;
    private String nameServerAddress;
    private int fileTransferPort;
    private int multicastPort;

    public Client(NodeConfig config) throws Exception
    {
        this.name = config.getName();

        var inet = NetworkInterface.getByName(config.getIfName()).getInetAddresses();
        if(inet.hasMoreElements()) this.ipaddress = inet.nextElement().getHostAddress();

        this.fileTransferPort = config.getFileTransferPort();
        this.multicastPort = config.getMulticastPort();
    }

    public Client(String name, String ipaddress, ArrayList<String> files)
    {
        this.name = name;
        this.ipaddress = ipaddress;
        this.files = files;
    }

    public ArrayList<String> getFiles()
    {
        return files;
    }

    public void setFiles(ArrayList<String> files)
    {
        this.files = files;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getIpaddress()
    {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress)
    {
        this.ipaddress = ipaddress;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getPreviousId()
    {
        return previousId;
    }

    public void setPreviousId(int previousId)
    {
        this.previousId = previousId;
    }

    public int getNextId()
    {
        return nextId;
    }

    public void setNextId(int nextId)
    {
        this.nextId = nextId;
    }

    public String getNameServerAddress()
    {
        return nameServerAddress;
    }

    public void setNameServerAddress(String nameServerAddress)
    {
        this.nameServerAddress = nameServerAddress;
    }

    public int getFileTransferPort() {
        return fileTransferPort;
    }

    public void setFileTransferPort(int fileTransferPort) {
        this.fileTransferPort = fileTransferPort;
    }

    public int getMulticastPort() {
        return multicastPort;
    }

    public void setMulticastPort(int multicastPort) {
        this.multicastPort = multicastPort;
    }
}
