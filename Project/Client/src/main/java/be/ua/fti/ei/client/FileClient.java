package be.ua.fti.ei.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class FileClient {

    private Socket s;
    private static final Logger logger = LoggerFactory.getLogger(FileClient.class);

    public FileClient(String host, int port, String file) {
        logger.info("arrived at fileClient");
        try {
            logger.info("fileClient try loop");
            s = new Socket(host, port);
            sendFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendFile(String file) throws IOException {
        logger.info("fileClient sendfile");
        logger.info("Sending file from /files"+file);
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        FileInputStream fis = new FileInputStream("files/"+file);
        byte[] buffer = new byte[4096];

        while (fis.read(buffer) > 0) {
            dos.write(buffer);
        }

        fis.close();
        dos.close();
    }

}