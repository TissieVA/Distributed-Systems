package be.ua.fti.ei.utils.http;

import be.ua.fti.ei.utils.Hasher;

public class FileBody implements java.io.Serializable
{
    private String filename;
    private NodeBody node;

    public FileBody() { }

    public FileBody(String filename, NodeBody node)
    {
        this.filename = filename;
        this.node = node;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public NodeBody getNode()
    {
        return node;
    }

    public void setNode(NodeBody node)
    {
        this.node = node;
    }

    public int getHostHash()
    {
        return Hasher.getHash(this.node);
    }

    public int getFileHash()
    {
        return Hasher.getHash(this.filename);
    }
}
