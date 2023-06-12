package de.tum.communication;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.io.PrintStream;

/**
 * ClassName: ParseCommand
 * Package: de.tum.server.communication
 * Description:
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/5/13 14:56
 * @Version 1.0
 */
public class ParseCommand {
    private int port = 5551;
    private String address = "127.0.0.1";
    private String bootstrapServerAddress = "192.168.1.24:5144";
    private String directory = "data/";
    private String logFile = "echo.log";
    private String logLevel = "INFO";
    private int cacheSize = 100;
    private String cacheDisplacement = "LRU";
    private boolean helpUsage = false;

    public ParseCommand(String[] args)
    {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-p":
                    port = Integer.parseInt(args[++i]);
                    break;
                case "-a":
                    address = args[++i];
                    break;
                case "-b":
                    bootstrapServerAddress = args[++i];
                    break;
                case "-d":
                    directory = args[++i];
                    break;
                case "-l":
                    logFile = args[++i];
                    break;
                case "-ll":
                    logLevel = args[++i];
                    break;
                case "c":
                    cacheSize = Integer.parseInt(args[++i]);
                    break;
                case "-s":
                    cacheDisplacement = args[++i];
                    break;
                case "-h":
                    helpUsage = true;
                    break;
            }
        }
    }

    public int getPort() {
        return port;
    }
    public String getAddress() {
        return address;
    }
    public String getBootstrapServerAddress() {
        return bootstrapServerAddress;
    }
    public String getDirectory() {
        return directory;
    }
    public String getLogFile() {
        return logFile;
    }
    public String getLogLevel() {
        return logLevel;
    }
    public int getCacheSize() {
        return cacheSize;
    }
    public String getCacheDisplacement() {
        return cacheDisplacement;
    }
    public boolean getHelpUsage() {
        return helpUsage;
    }
}
