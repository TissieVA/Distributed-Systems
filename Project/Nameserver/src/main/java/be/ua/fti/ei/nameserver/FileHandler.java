package be.ua.fti.ei.nameserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class FileHandler
{
    private static final Logger logger = LoggerFactory.getLogger(FileHandler.class);

    public static void createFileMethod(String name)
    {
        try
        {
            File myObj = new File(name);
            myObj.createNewFile();
        }
        catch (IOException e)
        {
            logger.error(e.getMessage());
        }
    }

    public static boolean writeToFile(String filename, String body)
    {
        createFileMethod(filename);
        try
        {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(body);
            myWriter.close();
            logger.info("Successfully wrote to the file.");
            return true;
        }
        catch (IOException e)
        {
            logger.error(e.getMessage());
            return false;
        }
    }

    public static FileInputStream getFileStream(String filename)
    {
        try
        {
            return new FileInputStream(filename);
        }
        catch (FileNotFoundException e)
        {
            logger.warn(e.getMessage());
            return null;
        }
    }
}