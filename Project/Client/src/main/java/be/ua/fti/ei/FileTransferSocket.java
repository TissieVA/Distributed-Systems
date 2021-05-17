package be.ua.fti.ei;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileTransferSocket
{
    private static final Logger logger = LoggerFactory.getLogger(FileTransferSocket.class);

    private ServerSocket socket;
    private boolean running;

    FileTransferSocket(int port)
    {
        try
        {
            this.socket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            logger.error(e.getMessage());
        }
    }

    /**
     * Get the startup thread of the file transfer socket
     */
    public FileTransferSocketThread getStartThread()
    {
        return new FileTransferSocketThread(this);
    }

    /**
     * Start the File Transfer socket
     */
    void startServer() throws Exception
    {
        this.running = true;
        while(this.running)
        {
            try
            {
                var client = this.socket.accept();

                var is = client.getInputStream();
                var reader = new BufferedReader(new InputStreamReader(is, UTF_8));
                String filename = reader.lines().collect(Collectors.joining());

                var file = new File("files/" + filename);
                var fis = new FileInputStream(file);
                var bis = new BufferedInputStream(fis);
                var os = client.getOutputStream();

                long fileLength = file.length();
                long current = 0;

                while (current != fileLength) {
                    int size = 10000;

                    if (fileLength - current >= size) {
                        current += size;
                    } else {
                        size = (int) (fileLength - current);
                        current = fileLength;
                    }

                    byte[] contents = new byte[size];
                    bis.read(contents, 0, size);
                    os.write(contents);
                }

                os.flush();
                client.close();
            }
            catch(Exception ex)
            {
                logger.error(ex.getMessage());
                break;
            }
        }

        this.socket.close();
    }

    /**
     * Download a file from a target server.
     * @param ip The target ip.
     * @param port The target port.
     * @param filename The name of the file to be transferred.
     */
    public void downloadFile(String ip, int port, String filename) throws Exception
    {
        // Start a connection
        Socket socket = new Socket(ip, port);

        // Request the specified file
        var os = socket.getOutputStream();
        var pw = new PrintWriter(os);
        pw.println(filename);

        // Receive file contents
        var is = socket.getInputStream();

        FileOutputStream fileOutputStream = new FileOutputStream("replicates/" + filename);
        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

        byte[] contents = new byte[10000];//buffer
        int bytesRead = 0;

        while((bytesRead = is.read(contents)) != -1)
        {
            bos.write(contents,0,bytesRead);
        }

        bos.flush();
        socket.close();
    }

    /**
     * Stop the socket server gracefully.
     */
    public void stop()
    {
        this.running = false;
    }
}

class FileTransferSocketThread extends Thread
{
    private static final Logger logger = LoggerFactory.getLogger(FileTransferSocketThread.class);

    FileTransferSocket socket;

    FileTransferSocketThread(FileTransferSocket socket)
    {
        this.socket = socket;
    }

    public void run()
    {
        try
        {
            this.socket.startServer();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }
}
