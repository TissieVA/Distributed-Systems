package be.ua.fti.ei.nameserver;

import be.ua.fti.ei.utils.Hasher;
import be.ua.fti.ei.utils.http.FileBody;
import be.ua.fti.ei.utils.http.NodeBody;
import be.ua.fti.ei.utils.sockets.NextPreviousBody;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;
import java.util.Iterator;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database
{
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    private HashMap<Integer, Node> hostDatabase;
    private HashMap<Integer, FileBody> localFileDatabase;
    private HashMap<Integer, FileBody> replicateDatabase;

    public Database()
    {
        this.hostDatabase = new HashMap<>();
        this.localFileDatabase = new HashMap<>();
        this.replicateDatabase = new HashMap<>();
        this.readXML();
    }

    public void buildReplicateDatabase()
    {
        this.localFileDatabase.values().stream().map(FileBody::getFilename).forEach(file -> {
            int replicateHost = this.getReplicateId(Hasher.getHash(file));
            FileBody fb = new FileBody(file, this.hostDatabase.get(replicateHost).getBody());
            // TODO: should the filebody contain the node of the original file

            this.replicateDatabase.put(fb.getHash(), fb);
        });
    }

    public int getReplicateId(String filename)
    {
        int hash = Hasher.getHash(filename);
        return this.getReplicateId(hash);
    }

    public int getReplicateId(int hash)
    {
        int localHost = Hasher.getHash(this.localFileDatabase.get(hash).getNode().getName());

        List<Integer> sortedKeys = hostDatabase.keySet().stream().sorted().collect(Collectors.toList());

        for (int i = sortedKeys.size()-1; i >= 0 ; i--)
        {
            if(sortedKeys.get(i) < hash)
            {
                int hostId =  sortedKeys.get(i);

                if(hostId != localHost)
                {
                    return hostId;
                }
                else
                {
                    if(i == 0)
                    {
                        return sortedKeys.get(sortedKeys.size() - 1);
                    }
                    else
                    {
                        return sortedKeys.get(i - 1);
                    }
                }
            }
        }

        int hostId = sortedKeys.get(sortedKeys.size() - 1);
        return hostId != localHost ? hostId : sortedKeys.get(sortedKeys.size() - 2);
    }

    public List<FileBody> getReplicates(int hash)
    {
        return this.replicateDatabase.values().stream().filter(f -> f.getHash() == hash).collect(Collectors.toList());
    }

    public Node searchFile(String filename)
    {
        int hash = Hasher.getHash(filename);

        if(!this.localFileDatabase.containsKey(hash))
            return null;

        return this.hostDatabase.get(this.replicateDatabase.get(hash).getHash());
    }

    public boolean addNewNode(String hostname, ArrayList<String> files, String ipAddress, int mcPort, int filePort)
    {
        logger.info("Adding new node");

        if (this.hostDatabase.values().stream().anyMatch(x -> x.getIpaddress().equals(ipAddress)))
            return false;

        int hash = Hasher.getHash(hostname);

        if(this.hostDatabase.containsKey(hash))
        {
            return false;
        }

        this.hostDatabase.put(hash, new Node(hostname, ipAddress, mcPort, filePort));

        System.out.println("hostname: " + hostname + "=" + hash);

        files.forEach(x -> {
            localFileDatabase.put(Hasher.getHash(x), new FileBody(x, this.hostDatabase.get(hash).getBody()));
            System.out.println(x + "=" + Hasher.getHash(x));
        });

        this.buildReplicateDatabase();

        outputXML();

        NSmessageHandler.getInstance().update();

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
            this.buildReplicateDatabase();

            outputXML();

            NSmessageHandler.getInstance().update();

            return true;
        }
        return false;
    }

    public boolean removeNode(int hash)
    {
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
            this.buildReplicateDatabase();

            outputXML();

            NSmessageHandler.getInstance().update();

            return true;
        }
        return false;
    }

    public NextPreviousBody getNeighbours(String hostname)
    {
        return getNeighbours(Hasher.getHash(hostname));
    }

    public NextPreviousBody getNeighbours(int hash)
    {

        if (Database.getInstance().getHostDatabase().size() == 1)
        {
            return new NextPreviousBody(hash, hash);
        }
        else
        {
            int higherNeighbour = Database.getInstance().getHigherNeighbour(hash);
            int lowerNeighbour = Database.getInstance().getLowerNeighbour(hash);
            return new NextPreviousBody(lowerNeighbour, higherNeighbour);
        }
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
        this.localFileDatabase = (HashMap<Integer, FileBody>) xmlDecoder.readObject();

        this.buildReplicateDatabase();
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
