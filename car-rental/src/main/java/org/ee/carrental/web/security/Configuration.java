package org.ee.carrental.web.security;

import jakarta.inject.Singleton;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:global/DemoDataSource",
        callerQuery = "SELECT password FROM user WHERE login = ?",
        groupsQuery = "SELECT ug.name FROM user u JOIN usergroup ug ON u.id = ug.user_id WHERE u.login = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class,
        hashAlgorithmParameters = {
                "Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512",
                "Pbkdf2PasswordHash.Iterations=3072",
                "Pbkdf2PasswordHash.SaltSizeBytes=64",
        }
)
@Singleton
public class Configuration {

}