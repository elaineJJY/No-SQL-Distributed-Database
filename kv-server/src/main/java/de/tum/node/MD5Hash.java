package de.tum.node;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ClassName: MD5Hash
 * Package: de.tum.server
 * Description: Provides MD5 hash function as a String
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/05/06 20:25
 * @Version 1.0
 */

public class MD5Hash {
    private MD5Hash() {
        throw new AssertionError("This class is not meant to be instantiated");
    }

    public static String hash(String key) {
        try {
            // Create an instance of the MD5 algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Compute the MD5 hash
            byte[] hashBytes = md.digest(key.getBytes());

            // Convert the hash bytes to a hexadecimal string representation
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            //TODO: mod calculation to 2^32

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
