package de.tum.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Hash {
    public MD5Hash() {
        throw new AssertionError("This class is not meant to be instantiated");
    }

    public static String getHash(String key) {
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

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
