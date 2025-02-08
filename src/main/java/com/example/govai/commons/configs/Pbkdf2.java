package com.example.govai.commons.configs;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@ApplicationScoped
public class Pbkdf2 implements Pbkdf2PasswordHash {
    private static final int ITERATIONS = 10000;
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 256;

    @Override
    public String generate(char[] password) {
        try {
            byte[] salt = generateSalt();
            byte[] hash = pbkdf2(password, salt);

            return Base64.getEncoder().encodeToString(salt) + ":" +
                    Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error generating password hash", e);
        }
    }

    @Override
    public boolean verify(char[] password, String hashedPassword) {
        try {
            String[] parts = hashedPassword.split(":");
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);

            byte[] testHash = pbkdf2(password, salt);
            return compareBytes(hash, testHash);
        } catch (Exception e) {
            throw new RuntimeException("Error verifying password", e);
        }
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] pbkdf2(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, HASH_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return skf.generateSecret(spec).getEncoded();
    }

    private boolean compareBytes(byte[] a, byte[] b) {
        if (a.length != b.length) return false;
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
}
