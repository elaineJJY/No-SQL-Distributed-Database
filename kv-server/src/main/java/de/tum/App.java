package de.tum;

import de.tum.common.Help;
import de.tum.common.ServerLogger;
import de.tum.communication.ParseCommand;
import de.tum.database.BackupDatabase;
import de.tum.database.MainDatabase;
import de.tum.node.Node;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
    // KVServer's port to listen ECS cannot conflict with KVServer's port to listen client
    public static final int KV_LISTEN_ECS_PORT = 5200;
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
            Socket socket = new Socket(bootStrapServerIP, bootStrapServerPort);
            OutputStream outputStream = socket.getOutputStream();
            ByteBuffer byteBuffer = ByteBuffer.wrap((address + ":" + String.valueOf(port)).getBytes());
            outputStream.write(byteBuffer.array());
            outputStream.flush();

            // init node and start serve，open NIO server for other client/server/ECS
            Node node = new Node(address, port, database, backupDatabase);
            node.startKVServer();
        }
        catch (Exception e) {
            LOGGER.severe("Server init failed: " + e.getMessage());
        }
    }
}

