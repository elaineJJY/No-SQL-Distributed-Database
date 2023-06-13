package de.tum;

import de.tum.common.Help;
import de.tum.common.ServerLogger;
import de.tum.communication.ParseCommand;
import de.tum.database.BackupDatabase;
import de.tum.database.MainDatabase;
import de.tum.grpc_api.ECServiceGrpc;
import de.tum.grpc_api.KVServerProto;
import de.tum.node.Node;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
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
            Node node = new Node(address, port, database, backupDatabase);
            System.out.println("test before rpc server");

            ServerBuilder rpcServerBuilder = ServerBuilder.forPort(0); // dynamic port

            rpcServerBuilder.addService(node);
            Server rpcServer = rpcServerBuilder.build();
            rpcServer.start();

            System.out.println("test after rpc server");
            int rpcPort = rpcServer.getPort();
            System.out.println(rpcPort);

            // register to ECS
            registerHandler(bootStrapServerIP, bootStrapServerPort, address, port, rpcPort);
            System.out.println("test after register");
            LOGGER.info("RPC service published on port: " + rpcPort + ", waiting to receive heartbeat from ECS/Other Servers");

//            KVServer kvServer = new KVServer(node);
//            kvServer.start(address, port);
            rpcServer.awaitTermination();
        }
        catch (Exception e) {
            LOGGER.severe("Server init failed: " + e.getMessage());
        }
    }

    public static void registerHandler(String bootStrapServerIP, int bootStrapServerPort, String address, int port, int rpcPort) throws InterruptedException{

        System.out.println("test in registerHandler" + bootStrapServerIP + ":" + bootStrapServerPort + ":"
                +  ":" + address + ":" + port + ":" +  rpcPort);

        // Register to ECS
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(bootStrapServerIP, bootStrapServerPort).usePlaintext().build();

        System.out.println("test in registerHandler" + managedChannel);
        ECServiceGrpc.ECServiceBlockingStub ecsService = ECServiceGrpc.newBlockingStub(managedChannel);
        System.out.println("test in registerHandler" + ecsService);

        KVServerProto.NodeMessage.Builder nodeMessageBuilder = KVServerProto.NodeMessage.newBuilder()
                .setHost(address)
                .setRpcPort(rpcPort)
                .setPortForClient(port);
        System.out.println("test in registerHandler" + nodeMessageBuilder);

        KVServerProto.RegisterRequest registerRequest = KVServerProto.RegisterRequest.newBuilder()
                .setNode(nodeMessageBuilder.build())
                .setRpcPort(rpcPort)
                .build();

        System.out.println("test in registerHandler" + registerRequest);

        ecsService.register(registerRequest);

        System.out.println("test register");

        managedChannel.shutdown();
        LOGGER.info("Register to ECS successfully");
    }
}

