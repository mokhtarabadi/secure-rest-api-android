package com.mokhtarabadi.secureapi.android.utility;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EncryptionUtility {

    private final char[] hexArray = "0123456789abcdef".toCharArray();

    private byte[] hmac(String algorithm, byte[] key, byte[] message)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(message);
    }

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @SneakyThrows
    public String generateHashWithHmac256(String message, String key) {
        String hashingAlgorithm = "HmacSHA256";
        byte[] bytes = hmac(hashingAlgorithm, key.getBytes(), message.getBytes());
        return bytesToHex(bytes);
    }
}
