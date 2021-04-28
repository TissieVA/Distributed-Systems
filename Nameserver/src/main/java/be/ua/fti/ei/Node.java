package be.ua.fti.ei;

public class Node implements java.io.Serializable
{
    private String ipaddress;
    private String name;

    public Node() {
    }

    public Node( String name, String ipaddress) {
        this.ipaddress = ipaddress;
        this.name = name;
    }

    public String getIpaddress() {
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
}
