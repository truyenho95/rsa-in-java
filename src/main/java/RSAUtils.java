import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtils {
    public static void main(String[] args) throws
                                           IOException,
                                           NoSuchAlgorithmException,
                                           InvalidKeySpecException,
                                           NoSuchPaddingException,
                                           InvalidKeyException,
                                           IllegalBlockSizeException,
                                           BadPaddingException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        File publicKeyFile = new File("public.key");
        byte[] publicKeyBytes1 = Files.readAllBytes(publicKeyFile.toPath());
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] publicKeyBytes = decoder.decode(publicKeyBytes1);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        File privateKeyFile = new File("private.key");
        byte[] privateKeyBytes1 = Files.readAllBytes(privateKeyFile.toPath());
        byte[] privateKeyBytes = decoder.decode(privateKeyBytes1);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        String secretMessage = "justin cool secret message";

        Cipher encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        encryptCipher.init(Cipher.ENCRYPT_MODE, privateKey);

        byte[] secretMessageBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
        String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
        System.out.println("******************** encodedMessage *********************");
        System.out.println(encodedMessage);

        Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
        decryptCipher.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
        String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
        System.out.println("******************** decryptedMessage *********************");
        System.out.println(decryptedMessage);
    }
}
