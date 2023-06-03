package de.tum.common;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public enum ServerLogger {

    INSTANCE;
    private Logger LOGGER;
    private FileHandler fileHandler;
    private Level logLevel;

    public void init(String level, String path, Logger logger) throws IOException {
        logLevel = Level.parse(level);
        path = path == null ? "logs/server.log" : path;
        checkFileExistence(path);
        LOGGER = logger;
        fileHandler = new FileHandler(path, true);
        fileHandler.setFormatter(new SimpleFormatter());
        LOGGER.setLevel(logLevel);
        LOGGER.addHandler(fileHandler);
        LOGGER.info("Already initialized loggers using Level " + level);
    }

    public Logger getLogger() {
        if (INSTANCE.LOGGER == null) {
            LOGGER = Logger.getLogger(ServerLogger.class.getName());
        }
        return INSTANCE.LOGGER;
    }

    // check if the file exists, if not, create it
    private void checkFileExistence(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            if (file.getParentFile() != null){
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
        }
    }

}
