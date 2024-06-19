//package org.ee.carrental.web.security;
//
//import jakarta.ejb.EJB;
//import jakarta.ejb.Stateless;
//import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Stateless
//public class PasswordHasher {
//
//    @EJB
//    private Pbkdf2PasswordHash passwordHash;
//
//    public PasswordHasher(){}
//
//    // Constructor for testing
//    public PasswordHasher(Pbkdf2PasswordHash passwordHash) {
//        this.passwordHash = passwordHash;
//        initialize();
//    }
//
//
//    public void initialize() {
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
//        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
//        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
//        passwordHash.initialize(parameters);
//    }
//
//
//    public String hash(String password) {
//        return passwordHash.generate(password.toCharArray());
//    }
//
//
//    public boolean verify(String password, String hashedPassword) {
//        return passwordHash.verify(password.toCharArray(), hashedPassword);
//    }
//}
