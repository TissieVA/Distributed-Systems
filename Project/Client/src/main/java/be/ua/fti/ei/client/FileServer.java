package be.ua.fti.ei.client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer extends Thread
{
    private String fileName;
    private ServerSocket serverSocket;
    private boolean running;
    private boolean received;

    public FileServer(int port) throws IOException
    {

        try
        {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e)
        {
            System.out.println("Can't setup server on this port number. ");
        }

    }


    public void run() {
        this.running = true;
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                saveFile(socket);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try
        {
            serverSocket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void saveFile(Socket clientSocket) throws IOException
    {

            InputStream in = null;
            OutputStream out = null;
            in = clientSocket.getInputStream();
            out = new FileOutputStream("replicates/"+fileName);
            byte[] bytes = new byte[16*1024];

            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }

            out.close();
            in.close();
            clientSocket.close();
            this.received = true;
    }


    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public boolean isRunning()
    {
        return running;
    }

    public void setRunning(boolean running)
    {
        this.running = running;
    }

    public boolean isReceived()
    {
        return received;
    }

    public void setReceived(boolean received)
    {
        this.received = received;
    }
}