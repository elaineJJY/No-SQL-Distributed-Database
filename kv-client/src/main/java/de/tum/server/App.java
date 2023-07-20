package de.tum.server;

import de.tum.common.ServerLogger;
import de.tum.server.communication.ParseCommand;
import de.tum.server.communication.Server;
import de.tum.server.database.Database;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * ClassName: App
 * Package: de.tum.server
 * Description: App main class for server
 *
 * @author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @version 1.0
 */
public class App 
{
    private static Logger LOGGER = Logger.getLogger(App.class.getName());
    public static void main( String[] args )
    {
        ParseCommand parseCommand = new ParseCommand(args);

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
            Database.INSTANCE.setDirectory(directory);
            // run server
            LOGGER.info("Server is starting...");
            new Server().start(address, port, helpUsage);
            LOGGER.info("Server is shutting down...");
        }
        catch (IOException e) {
            LOGGER.severe("[IOException] Server failed" + e.getMessage());
        }
        catch (Exception e) {
            LOGGER.severe("Server failed: " + e.getMessage());
        }
    }

}
