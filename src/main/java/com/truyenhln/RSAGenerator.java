package com.truyenhln;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class RSAGenerator {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair pair = generator.generateKeyPair();

        PrivateKey privateKeyByte = pair.getPrivate();
        PublicKey publicKeyByte = pair.getPublic();

        Base64.Encoder encoder = Base64.getEncoder();
        String publicKeyString = encoder.encodeToString(publicKeyByte.getEncoded());
        String privateKeyString = encoder.encodeToString(privateKeyByte.getEncoded());

        try (FileOutputStream fos = new FileOutputStream("public.key")) {
            fos.write(publicKeyString.getBytes(StandardCharsets.UTF_8));
        }

        try (FileOutputStream fos = new FileOutputStream("private.key")) {
            fos.write(privateKeyString.getBytes(StandardCharsets.UTF_8));
        }
    }
}
