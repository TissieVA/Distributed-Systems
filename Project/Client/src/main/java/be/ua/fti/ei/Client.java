package be.ua.fti.ei;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

public class Client
{


    private String name;
    private String ipaddress;
    private int id;
    private int previousId;
    private int nextId;

    public Client(String name, String ipaddress)
    {
        this.name = name;
        this.ipaddress = ipaddress;
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

}
