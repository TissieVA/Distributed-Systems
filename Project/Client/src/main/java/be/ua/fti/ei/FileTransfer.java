package be.ua.fti.ei;

import org.apache.catalina.Server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransfer {


    public static void transferFile(ServerSocket clientsocket, File file) throws IOException {
        //initialize the socket
        Socket socket= clientsocket.accept();
        

    }

}
