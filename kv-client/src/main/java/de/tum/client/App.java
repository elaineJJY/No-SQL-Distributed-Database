package de.tum.client;

import java.io.IOException;
import java.net.SocketException;

/**
 * ClassName: App
 * Package: de.tum.client
 * Description: App main class for client
 *
 * @author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @reate 2023/05/06 20:25
 * @Version 1.0
 */

public class App {

    public static void main(String[] args) {
        Shell shell = new Shell();
        try {
            shell.handleCommand();
        }
        catch (SocketException e) {
            shell.getLogger().severe(e.getMessage());
        }
        catch (IOException e) {
            shell.getLogger().severe(e.getMessage());
        }
        catch (Exception e) {
            shell.getLogger().severe(e.getMessage());
        }
    }
}
