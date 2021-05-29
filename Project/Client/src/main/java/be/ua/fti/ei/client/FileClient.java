package be.ua.fti.ei.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class FileClient {

    private Socket socket;
    DataOutputStream out = null;
    DataInputStream in = null;
    private static final Logger logger = LoggerFactory.getLogger(FileClient.class);

    public FileClient(String host, int port, String fileName)
    {
        try
        {
            logger.info("FileClient -> ip to send to = "+host+" port= "+port);
            socket = new Socket(host, port);
            logger.info("send over from "+fileName);
            File file = new File(fileName);
            long length = file.length();
            byte[] bytes = new byte[16 * 1024];
            InputStream in = new FileInputStream(file);
            OutputStream out = socket.getOutputStream();

            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }

            out.close();
            in.close();
            socket.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }


}