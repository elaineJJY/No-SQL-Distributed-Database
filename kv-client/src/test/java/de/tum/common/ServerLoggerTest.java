package de.tum.common;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.logging.*;

public class ServerLoggerTest {
    private final Logger logger = Logger.getLogger(ServerLoggerTest.class.getName());

    @Test
    public void testInit() throws IOException {
        String level = "WARNING";
        String path = "logs/server.log";
        ServerLogger.INSTANCE.init(level, path, logger);

        Logger logger = ServerLogger.INSTANCE.getLogger();

        // Verify that the logger has been initialized correctly
        assertTrue(logger.getHandlers().length > 0);
        assertEquals(logger.getHandlers()[0].getClass(), FileHandler.class);
        assertEquals(logger.getHandlers()[0].getFormatter().getClass(), SimpleFormatter.class);
        assertEquals(logger.getHandlers()[0].getLevel(), Level.ALL);
        assertEquals(logger.getLevel(), Level.parse(level));

        // Verify that the log file has been created
        File logFile = new File(path);
        assertTrue(logFile.exists());
    }

    @Test
    public void testInitWithNull() throws IOException {
        String level = "WARNING";
        ServerLogger.INSTANCE.init(level, null, logger);
        // Verify that the log file has been created
        File logFile = new File("logs/server.log");
        assertTrue(logFile.exists());
    }
}
