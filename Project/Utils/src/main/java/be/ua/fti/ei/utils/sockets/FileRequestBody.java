package be.ua.fti.ei.utils.sockets;

public class FileRequestBody extends SocketBody implements java.io.Serializable
{
    private String filename;
    private String myIpaddress;
    private int filePort;

    public FileRequestBody() { }

    public FileRequestBody(String filename, String myIpaddress, int filePort)
    {
        super("file");
        this.filename = filename;
        this.myIpaddress = myIpaddress;
        this.filePort = filePort;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public String getMyIpaddress()
    {
        return myIpaddress;
    }

    public void setMyIpaddress(String myIpaddress)
    {
        this.myIpaddress = myIpaddress;
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
