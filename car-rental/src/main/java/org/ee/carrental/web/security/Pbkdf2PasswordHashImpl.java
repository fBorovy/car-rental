//package org.ee.carrental.web.security;
//
//import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
//
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.PBEKeySpec;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.security.spec.InvalidKeySpecException;
//import java.security.spec.KeySpec;
//import java.util.Base64;
//import java.util.Map;
//
//public class Pbkdf2PasswordHashImpl implements Pbkdf2PasswordHash {
//
//    private String algorithm = "PBKDF2WithHmacSHA512";
//    private int iterations = 3072;
//    private int saltSizeBytes = 64;
//
//    @Override
//    public void initialize(Map<String, String> parameters) {
//        if (parameters.containsKey("Pbkdf2PasswordHash.Algorithm")) {
//            algorithm = parameters.get("Pbkdf2PasswordHash.Algorithm");
//        }
//        if (parameters.containsKey("Pbkdf2PasswordHash.Iterations")) {
//            iterations = Integer.parseInt(parameters.get("Pbkdf2PasswordHash.Iterations"));
//        }
//        if (parameters.containsKey("Pbkdf2PasswordHash.SaltSizeBytes")) {
//            saltSizeBytes = Integer.parseInt(parameters.get("Pbkdf2PasswordHash.SaltSizeBytes"));
//        }
//    }
//
//    @Override
//    public String generate(char[] password) {
//        byte[] salt = new byte[saltSizeBytes];
//        SecureRandom random = new SecureRandom();
//        random.nextBytes(salt);
//        return generate(password, salt);
//    }
//
//    public String generate(char[] password, byte[] salt) {
//        try {
//            KeySpec spec = new PBEKeySpec(password, salt, iterations, 64 * 8);
//            SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
//            byte[] hash = factory.generateSecret(spec).getEncoded();
//            return Base64.getEncoder().encodeToString(hash);
//        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
//            throw new RuntimeException("Error generating hash", e);
//        }
//    }
//
//    @Override
//    public boolean verify(char[] password, String hashedPassword) {
//        byte[] hash = Base64.getDecoder().decode(hashedPassword);
//        byte[] salt = new byte[saltSizeBytes];
//        String newHash = generate(password, salt);
//        return newHash.equals(hashedPassword);
//    }
//}
