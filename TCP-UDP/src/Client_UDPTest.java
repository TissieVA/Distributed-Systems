import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class Client_UDPTest {

    @Test
    public void whenCanSendAndReceivePacket_thenCorrect() throws IOException {
        new Server_UDP().start();
        Client_UDP client = new Client_UDP();
        String echo = client.sendEcho("hello server");
        assertEquals("hello server", echo);
        echo = client.sendEcho("server is working");
        assertFalse(echo.equals("hello server"));
    }

}