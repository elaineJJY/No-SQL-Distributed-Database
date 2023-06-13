package de.tum.common;

/**
 * ClassName: Help
 * Package: de.tum.communication
 * Description:
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/6/2 23:00
 * @Version 1.0
 */
public class Help {
    /**
     * To display the help text
     */
    public static void helpDisplay() {
        System.out.println(
                "Options:\n"
                        + "    help:                   Prints help text.\n"
                        + "    connect <addrs> <port>: Tries to establish a TCP- connection to the echo server based on the given\n"
                        + "                            server address and the port numnber of th echo service.\n"
                        + "    disconnect:             Tries to disconnect from the connected server.\n"
                        + "    send <message>:         Sends a text message to the echo server according to the communication protocol\n"
                        + "    logLevel <level>:       Sets the logger to the specified log level\n"
                        + "    quit:                   Tears down the active connection to the server and exits the program execution\n");
    }
}

