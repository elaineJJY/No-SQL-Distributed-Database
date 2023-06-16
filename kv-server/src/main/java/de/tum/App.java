package de.tum;

import de.tum.common.Help;
import de.tum.common.ServerLogger;
import de.tum.communication.ParseCommand;
import de.tum.database.BackupDatabase;
import de.tum.database.MainDatabase;
import de.tum.node.MetaData;
import de.tum.node.Node;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

/**
 * ClassName: App
 * Package: de.tum.server
 * Description: App main class for server
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/05/06 20:25
 * @Version 1.0
 */
public class App 
{
    private static Logger LOGGER = Logger.getLogger(App.class.getName());
    private static Socket socket;
    public static void main( String[] args )
    {
        ParseCommand parseCommand = new ParseCommand(args);
        
        // CLI parse args
        int port = parseCommand.getPort();
        String address = parseCommand.getAddress();
        String bootStrapServerAddress = parseCommand.getBootstrapServerAddress();
        String bootStrapServerIP = bootStrapServerAddress.split(":")[0];
        int bootStrapServerPort = Integer.parseInt(bootStrapServerAddress.split(":")[1]);
        String directory = parseCommand.getDirectory(); //not used yet
        String logFile = parseCommand.getLogFile();
        String logLevel = parseCommand.getLogLevel();
        int cacheSize = parseCommand.getCacheSize();
        String cacheStrategy = parseCommand.getCacheDisplacement();
        boolean helpUsage = parseCommand.getHelpUsage();
        if (helpUsage) Help.helpDisplay();

        try {
            // prepare for server
            ServerLogger.INSTANCE.init(logLevel,logFile, LOGGER);

            MainDatabase database = new MainDatabase(cacheSize, cacheStrategy);
            String mainDatabaseDir = "src/main/java/de/tum/database/data/" + port + "/mainData";
            database.setDirectory(mainDatabaseDir);
            String backupDatabaseDir = "src/main/java/de/tum/database/data/" + port + "/backupDatabase";
            BackupDatabase backupDatabase = new BackupDatabase();
            backupDatabase.setDirectory(backupDatabaseDir);

            // register to ECS and sent address of this node to it
            socket = new Socket(bootStrapServerIP, bootStrapServerPort);
            OutputStream outputStream = socket.getOutputStream();
            ByteBuffer byteBuffer = ByteBuffer.wrap((address + ":" + String.valueOf(port)).getBytes());
            outputStream.write(byteBuffer.array());
            outputStream.flush();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down server...");
                try {
                    // send shutdown message to ECS
                    OutputStream outputStreamShutdown = socket.getOutputStream();
                    ByteBuffer byteBufferShutdown = ByteBuffer.wrap((address + ":" + String.valueOf(port)).getBytes());
                    outputStreamShutdown.write(byteBufferShutdown.array());
                    outputStreamShutdown.flush();

                    // receive response from ECS
                    while (true) {
                        byte[] buffer = new byte[1024];
                        int bytesRead = socket.getInputStream().read(buffer);
                        if (bytesRead != -1) {
                            byte[] message = new byte[bytesRead];
                            System.arraycopy(buffer, 0, message, 0, bytesRead);
                            String messageString = new String(message);
                            if (messageString.equals("REMOVE_NODE_SUCCESS")) {
                                System.out.println("Successfully removed node from ECS");
                                break;
                            } else {
                                LOGGER.severe("Error removing node from ECS");
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    LOGGER.severe("Error notifying ECS to remove node: " + e.getMessage());
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        LOGGER.severe("Error closing socket: " + e.getMessage());
                    }
                }
            }));

            // init node and start serve，open NIO server for other client/server/ECS
            Node node = new Node(address, port, database, backupDatabase);
            MetaData.INSTANCE.setLocalNode(node);
            node.startKVServer();
        }
        catch (Exception e) {
            LOGGER.severe("Server init failed: " + e.getMessage());
        }
    }
}

