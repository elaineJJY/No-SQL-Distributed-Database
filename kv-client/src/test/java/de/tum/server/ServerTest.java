package de.tum.server;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerTest {
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 8080;
    private static final String CAPACITY = "1000";
    private static final String CACHE_DISPLACEMENT_ALGORITHM = "FIFO";
    private static final String DIRECTORY = "src/test/java/de/tum/server/data/";

    private static SocketChannel clientChannel;
    @BeforeAll
    public static void setUp() throws IOException {

        Thread serverThread = new Thread
                (() -> App.main(new String[]{"-a", ADDRESS, "-p", String.valueOf(PORT), "-s", CACHE_DISPLACEMENT_ALGORITHM, "-c", CAPACITY, "-d", DIRECTORY}));
        serverThread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        clientChannel = SocketChannel.open(new InetSocketAddress(ADDRESS, PORT));
        clientChannel.configureBlocking(false);
}
    @AfterAll
    public static void tearDown() throws IOException {
        if (clientChannel != null) {
            clientChannel.close();
        }
        Thread.interrupted();

        String folderPath = "src/test/java/de/tum/server/data/";
        Path folder = Paths.get(folderPath);

        // Delete the folder and its contents recursively
        if (Files.exists(folder)) {
            Files.walk(folder)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }


    @Test
    @Order(1)
    public void testConnection() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // Receive the response from the server
        buffer.clear();
        clientChannel.read(buffer);
        buffer.flip();
        byte[] responseBytes = new byte[buffer.remaining()];
        buffer.get(responseBytes);
        String response = new String(responseBytes);

        // Close the client socket channel
        System.out.println(response);

        // Assert the received response
        assertEquals("Hello client\n", response);
    }


    @Test
    @Order(2)
    public void testPutRequest() throws IOException, InterruptedException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // Send a get request to the server
        String getRequest = "put 1 a\n";
        buffer.clear();
        buffer.put(getRequest.getBytes());
        buffer.flip();
        clientChannel.write(buffer);
        Thread.sleep(1000);

        // Receive the response from the server
        buffer.clear();
        clientChannel.read(buffer);
        buffer.flip();
        byte[] responseBytes = new byte[buffer.remaining()];
        buffer.get(responseBytes);
        String response = new String(responseBytes);

        // Assert the received response
        assertEquals("put_success 1\n", response);
    }

    @Test
    @Order(3)
    public void testUpdateRequest() throws IOException, InterruptedException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // Send a get request to the server
        String getRequest = "put 1 b\n";
        buffer.clear();
        buffer.put(getRequest.getBytes());
        buffer.flip();
        clientChannel.write(buffer);
        Thread.sleep(1000);

        // Receive the response from the server
        buffer.clear();
        clientChannel.read(buffer);
        buffer.flip();
        byte[] responseBytes = new byte[buffer.remaining()];
        buffer.get(responseBytes);
        String response = new String(responseBytes);

        // Assert the received response
        assertEquals("put_update 1\n", response);
    }

    @Test
    @Order(4)
    public void testGetRequest() throws IOException, InterruptedException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // Send a get request to the server
        String getRequest = "get 1\n";
        buffer.clear();
        buffer.put(getRequest.getBytes());
        buffer.flip();
        clientChannel.write(buffer);
        Thread.sleep(1000);

        // Receive the response from the server
        buffer.clear();
        clientChannel.read(buffer);
        buffer.flip();
        byte[] responseBytes = new byte[buffer.remaining()];
        buffer.get(responseBytes);
        String response = new String(responseBytes);

        // Assert the received response
        assertEquals("get_success 1 b\n", response);
    }

    @Test
    @Order(5)
    public void getNonExistingKey() throws IOException, InterruptedException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // Send a get request to the server
        String getRequest = "get 2\n";
        buffer.clear();
        buffer.put(getRequest.getBytes());
        buffer.flip();
        clientChannel.write(buffer);
        Thread.sleep(1000);

        // Receive the response from the server
        buffer.clear();
        clientChannel.read(buffer);
        buffer.flip();
        byte[] responseBytes = new byte[buffer.remaining()];
        buffer.get(responseBytes);
        String response = new String(responseBytes);

        // Assert the received response
        assertEquals("get_error 2\n", response);
    }
}

