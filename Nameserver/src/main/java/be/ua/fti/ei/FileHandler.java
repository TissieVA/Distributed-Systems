package be.ua.fti.ei;

import java.io.*;

public class FileHandler {

    public static void createFileMethod(String name) {
        try {
            File myObj = new File(name);
            if (myObj.createNewFile()) {
                //System.out.println("File created: " + myObj.getName());
            } else {
                //System.out.println("File already exists.");
            }
        } catch (IOException e) {}
    }

    public static boolean writeToFile(String filename, String body)
    {
        createFileMethod(filename);
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(body);
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static FileInputStream getFileStream(String filename)
    {
        try {
            return new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}