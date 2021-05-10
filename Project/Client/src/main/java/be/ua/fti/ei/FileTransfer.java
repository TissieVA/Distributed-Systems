package be.ua.fti.ei;

import org.apache.catalina.Server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class FileTransfer {


    public static void transferFileSender(int port, File file, InetAddress ipb) throws IOException
    {
        //initialize the socket
        ServerSocket clientsocket=new ServerSocket(port);
        Socket socket = clientsocket.accept();
        //InetAddress IA = InetAddress.getByName("localhost");
        //FIlE
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        //get socket output stream
        OutputStream os = socket.getOutputStream();
        //put the file into an array
        byte[] contents;
        long fileLength = file.length();
        long current=0;
        while(current!=fileLength){
            int size=10000;
            if(fileLength-current>=size){
                current+=size;
            }
            else{
                size = (int)(fileLength-current);
                current = fileLength;
            }
            contents = new byte[size];
            bis.read(contents,0,size);
            os.write(contents);
        }
        os.flush();
        //filetransfer is finished
        socket.close();
        clientsocket.close();
    }


    public static void transferFileReciever(String ipaddress,int port) throws IOException
    {
        //initialize socket
        Socket socket = new Socket(ipaddress,port);
        byte[] contents = new byte[10000];
        //file outputstrem to the output path
        FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("user.dir"));
        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
        InputStream is = socket.getInputStream();
        //no of butes read in one read call
        int bytesRead = 0;
        while((bytesRead = is.read(contents))!=-1)
        {
            bos.write(contents,0,bytesRead);
        }
        bos.flush();
        socket.close();
        System.out.println("files save succesfull");
    }



}
