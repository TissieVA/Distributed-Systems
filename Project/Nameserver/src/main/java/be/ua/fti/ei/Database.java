package be.ua.fti.ei;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;
import java.util.Iterator;
import java.util.stream.Collectors;

public class Database
{
    private HashMap<Integer, Node> hostDatabase;
    private HashMap<Integer,Integer> localFileDatabase;


    public Database()
    {
        this.hostDatabase = new HashMap<>();
        this.localFileDatabase = new HashMap<>();
        this.readXML();
    }

    public Node searchFile(String filename)
    {
        int hash = Hasher.getHash(filename);

        if(!this.localFileDatabase.containsKey(hash))
            return null;


        List<Integer> sortedKeys = hostDatabase.keySet().stream().sorted().collect(Collectors.toList());

        for (int i = sortedKeys.size()-1; i >= 0 ; i--)
        {
            if(sortedKeys.get(i) < hash)
            {
                return this.hostDatabase.get(sortedKeys.get(i));
            }
        }

        return this.hostDatabase.get(sortedKeys.get(sortedKeys.size()-1));
    }

    public boolean addNewNode(String hostname, ArrayList<String> files, String ipAddress)
    {

        if (this.hostDatabase.values().stream().anyMatch(x -> x.getIpaddress().equals(ipAddress)))
            return false;

        int hash = Hasher.getHash(hostname);

        if(this.hostDatabase.containsKey(hash))
        {
            return false;
        }

        this.hostDatabase.put(hash,new Node(hostname, ipAddress));

        System.out.println("hostname: " + hostname + "=" + hash);

        files.forEach(x -> {
            localFileDatabase.put(Hasher.getHash(x),hash);
            System.out.println(x + "=" + Hasher.getHash(x));
        });

        outputXML();

        return true;
    }


    public boolean removeNode(String nodeName)
    {

        int hash = Hasher.getHash(nodeName);
        if(this.hostDatabase.containsKey(hash))
        {
            Iterator it =this.localFileDatabase.entrySet().iterator();
            while (it.hasNext())
            {
                Map.Entry me= (Map.Entry) it.next();
                if(me.getValue().equals(hash))
                    it.remove();
            }
            this.hostDatabase.remove(hash);
            outputXML();

            return true;
        }
        return false;
    }

    public int getHigherNeighbour(String hostName)
    {
        return this.getHigherNeighbour(Hasher.getHash(hostName));
    }

    public int getHigherNeighbour(int hostId)
    {
        List<Integer> ascendingStream = this.hostDatabase.keySet().stream().sorted().collect(Collectors.toList());

        return ascendingStream.stream().filter(integer -> integer >= hostId).findFirst()
                .orElse(ascendingStream.get(0));
    }

    public int getLowerNeighbour(String hostName)
    {
        return this.getLowerNeighbour(Hasher.getHash(hostName));
    }
    
    public int getLowerNeighbour(int hostId)
    {
        List<Integer> descendingStream = this.hostDatabase.keySet().stream()
                .sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        return descendingStream.stream().filter(integer -> integer < hostId).findFirst()
                .orElse(descendingStream.get(0));
    }

    public void readXML()
    {
        InputStream is = FileHandler.getFileStream("Database.xml");
        if(is == null)
            return;

        XMLDecoder xmlDecoder = new XMLDecoder(is);
        this.hostDatabase = (HashMap<Integer, Node>) xmlDecoder.readObject();
        this.localFileDatabase = (HashMap<Integer, Integer>) xmlDecoder.readObject();
    }

    public boolean outputXML()
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLEncoder xmlEncoder = new XMLEncoder(output);
        xmlEncoder.writeObject(this.hostDatabase);
        xmlEncoder.writeObject(this.localFileDatabase);
        xmlEncoder.close();

        String mapToString = output.toString();

        return FileHandler.writeToFile("Database.xml",mapToString);
    }


    private static Database instance;

    public static Database getInstance()
    {
        if(instance == null)
            instance = new Database();

        return instance;
    }

    public HashMap<Integer, Node> getHostDatabase() {
        return hostDatabase;
    }

}
