package de.tum;

import de.tum.common.ServerLogger;
import de.tum.communication.*;
import de.tum.server.database.Database;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

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

    public static void main( String[] args )
    {
        de.tum.server.communication.ParseCommand parseCommand = new de.tum.server.communication.ParseCommand(args);

        // parse args
        int port = parseCommand.getPort();
        String address = parseCommand.getAddress();
        String bootStrapServerAddress = parseCommand.getBootstrapServerAddress();
        String directory = parseCommand.getDirectory();
        String logFile = parseCommand.getLogFile();
        String logLevel = parseCommand.getLogLevel();
        int cacheSize = parseCommand.getCacheSize();
        String cacheStrategy = parseCommand.getCacheDisplacement();
        boolean helpUsage = parseCommand.getHelpUsage();

        try {
            // init according to the args
            ServerLogger.INSTANCE.init(logLevel,logFile, LOGGER);
            Database.INSTANCE.init(cacheSize, cacheStrategy);

            // run server
            LOGGER.info("Server is starting...");
            new Server().start(address, port, helpUsage);


            LOGGER.info("Server is shutting down...");
        }
        catch (Exception e) {
            LOGGER.severe("Server init failed: " + e.getMessage());
        }
    }
}
