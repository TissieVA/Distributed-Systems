package be.ua.fti.ei;

import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class Database
{
    private HashMap<Integer, Integer> database;

    public Database()
    {
        this.database = new HashMap<>();
        this.database.put(6885, 0);
        this.database.put(32507, 1);
    }

    public int getID(String filename)
    {
        int hash = Hasher.getHash(filename);

        if(database.containsKey(hash))
            return database.get(hash);

        return -1;
    }

    public boolean outputXML()
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XMLEncoder xmlEncoder = new XMLEncoder(output);
        xmlEncoder.writeObject(database);
        xmlEncoder.close();

        String mapToString = output.toString();

        return FileHandler.writeToFile("output.xml",mapToString);

    }

    private static Database instance;

    public static Database getInstance()
    {
        if(instance == null)
            instance = new Database();

        return instance;
    }
}
