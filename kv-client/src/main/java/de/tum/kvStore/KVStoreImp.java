package de.tum.kvStore;

import de.tum.common.MD5Hash;

import java.util.Arrays;

public class KVStoreImp implements KVStore {

    private MetaData metaData;
    private final CommunicationModule communicationModule;

    public KVStoreImp() {
        this.communicationModule = CommunicationModule.INSTANCE;
    }

    public byte[] startConnection(String ip, int port) throws Exception {
        communicationModule.startConnection(ip, port);
        if (communicationModule.isConnected()) {
            return communicationModule.receive();
        } else {
            return null;
        }
    }

    public boolean isConnected() {
        return communicationModule.isConnected();
    }

    public boolean stopConnection() throws Exception {
        communicationModule.stopConnection();
        return !communicationModule.isConnected();
    }

    public byte[] put(String key, String value) throws Exception {
        return commandHandler("put", key, value);
    }

    public byte[] get(String key) throws Exception {
        return commandHandler("get", key, "");
    }

    public byte[] delete(String key) throws Exception {
        return commandHandler("delete", key, "");
    }

    public byte[] commandHandler(String command, String key, String value) throws Exception {
        if (metaData == null) {
            communicationModule.send((command + " " + key + " " + value).getBytes());
            byte[] receivedMessage = communicationModule.receive();

            String[] parts = Arrays.toString(receivedMessage).split(" ");
            if (parts[0].equals("keyrange_success")) {
                updateMetaData(parts[1]);
                String[] address = metaData.getIPByHash(MD5Hash.getHash(key)).split(":");

                startConnection(address[0], Integer.parseInt(address[1]));
                communicationModule.send((command + " " + key + " " + value).getBytes());
                return communicationModule.receive();
            }
            else {
                return receivedMessage;
            }
        }
        else {
            String[] address = metaData.getIPByHash(MD5Hash.getHash(key)).split(":");
            startConnection(address[0], Integer.parseInt(address[1]));
            communicationModule.send((command + " " + key + " " + value).getBytes());
            return communicationModule.receive();
        }
    }

    public void updateMetaData(String response) throws Exception {
        this.metaData = new MetaData();
        metaData.init(response);
    }
}
