package com.evaluacion.gateway.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class AesEncryptionUtil {

    private static final String SECRET_PASSPHRASE = "MiClaveSecreta2026";

    public static String decrypt(String cipherTextBase64) {
        try {
            byte[] cipherData = Base64.getDecoder().decode(cipherTextBase64);

            byte[] salt = Arrays.copyOfRange(cipherData, 8, 16);
            byte[] cipherText = Arrays.copyOfRange(cipherData, 16, cipherData.length);

            byte[][] keyAndIv = deriveKeyAndIv(SECRET_PASSPHRASE.getBytes(StandardCharsets.UTF_8), salt);

            SecretKeySpec keySpec = new SecretKeySpec(keyAndIv[0], "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(keyAndIv[1]);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decrypted = cipher.doFinal(cipherText);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException("No fue posible descifrar el atributo 'secreto'. Verifique la clave.", e);
        }
    }

    private static byte[][] deriveKeyAndIv(byte[] password, byte[] salt) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] result = new byte[0];
        byte[] currentDigest = new byte[0];

        while (result.length < 48) {
            md5.reset();
            md5.update(currentDigest);
            md5.update(password);
            md5.update(salt);
            currentDigest = md5.digest();

            byte[] newResult = new byte[result.length + currentDigest.length];
            System.arraycopy(result, 0, newResult, 0, result.length);
            System.arraycopy(currentDigest, 0, newResult, result.length, currentDigest.length);
            result = newResult;
        }

        byte[] key = Arrays.copyOfRange(result, 0, 32);
        byte[] iv = Arrays.copyOfRange(result, 32, 48);
        return new byte[][]{key, iv};
    }
}
