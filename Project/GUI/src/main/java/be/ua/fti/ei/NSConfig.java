package be.ua.fti.ei;
import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Path;
public class NSConfig implements java.io.Serializable
{
    private String ifName;
    private String ipAddress;
    private int fileTransferPort;
    private int multicastPort;
    private int httpPort;

    public NSConfig() {}

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIfName() {
        return ifName;
    }

    public void setIfName(String ifName) {
        this.ifName = ifName;
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


    public static NSConfig load(String file) throws Exception
    {
        // Read file to string
        Path filename = Path.of(file);
        String content = Files.readString(filename);

        Gson gson = new Gson();
        return gson.fromJson(content, NSConfig.class);
    }
}
