package be.ua.fti.ei;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Database
{
    private HashMap<Integer, String> hostNameDatabase;
    private ArrayList<Integer> localFileDatabase;
    private ArrayList<String> usedIpAddresses;

    public Database()
    {
        this.hostNameDatabase = new HashMap<>();
        this.localFileDatabase = new ArrayList<>();
        this.usedIpAddresses = new ArrayList<>();
        this.readXML();
    }

    public String searchFile(String filename)
    {
        int hash = Hasher.getHash(filename);

        if(!this.localFileDatabase.contains(hash))
            return "File not found!";


        List<Integer> sortedKeys = hostNameDatabase.keySet().stream().sorted().collect(Collectors.toList());

        for (int i = sortedKeys.size()-1; i >= 0 ; i--)
        {
            if(sortedKeys.get(i) < hash)
            {
                return this.hostNameDatabase.get(sortedKeys.get(i));
            }
        }

        return this.hostNameDatabase.get(sortedKeys.get(sortedKeys.size()-1));
    }


    public boolean addNewNode(String hostname, ArrayList<String> files, String ipAddress)
    {
        if(this.usedIpAddresses.contains(ipAddress))
            return false;

        this.usedIpAddresses.add(ipAddress);

        int hash = Hasher.getHash(hostname);
        this.hostNameDatabase.put(hash,hostname);

        System.out.println("hostname" + hostname + "=" + hash);

        files.forEach(x -> {
            localFileDatabase.add(Hasher.getHash(x));
            System.out.println(x + "=" + Hasher.getHash(x));
        });

        outputXML();

        return true;
    }

    public void readXML()
    {
        InputStream is = FileHandler.getFileStream("Database.xml");
        if(is == null)
            return;

        XMLDecoder xmlDecoder = new XMLDecoder(is);
        this.hostNameDatabase = (HashMap<Integer, String>) xmlDecoder.readObject();
        this.localFileDatabase = (ArrayList<Integer>) xmlDecoder.readObject();
        this.usedIpAddresses = (ArrayList<String>) xmlDecoder.readObject();
    }

    public boolean outputXML()
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLEncoder xmlEncoder = new XMLEncoder(output);
        xmlEncoder.writeObject(this.hostNameDatabase);
        xmlEncoder.writeObject(this.localFileDatabase);
        xmlEncoder.writeObject(this.usedIpAddresses);
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
}
