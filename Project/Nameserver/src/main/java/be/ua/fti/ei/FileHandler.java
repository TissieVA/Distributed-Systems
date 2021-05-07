package be.ua.fti.ei;

import java.io.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            //System.out.println("Successfully wrote to the file.");
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
            logger.error(e.getMessage());
            return null;
        }
    }
}