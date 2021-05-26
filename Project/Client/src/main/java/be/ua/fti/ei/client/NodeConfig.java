package be.ua.fti.ei.client;

import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Path;

public class NodeConfig implements java.io.Serializable
{
    private String name;
    private String ifName;
    private int fileTransferPort;
    private int multicastPort;

    public NodeConfig() { }

    public String getIfName()
    {
        return ifName;
    }

    public void setIfName(String ifName)
    {
        this.ifName = ifName;
    }

    public int getFileTransferPort()
    {
        return fileTransferPort;
    }

    public void setFileTransferPort(int fileTransferPort)
    {
        this.fileTransferPort = fileTransferPort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMulticastPort()
    {
        return multicastPort;
    }

    public void setMulticastPort(int multicastPort)
    {
        this.multicastPort = multicastPort;
    }

    public static NodeConfig load(String file) throws Exception
    {
        // Read file to string
        Path filename = Path.of(file);
        String content = Files.readString(filename);

        Gson gson = new Gson();
        return gson.fromJson(content, NodeConfig.class);
    }
}
