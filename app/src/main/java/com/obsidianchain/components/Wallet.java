package com.obsidianchain.components;

import java.security.*;
import java.security.spec.ECGenParameterSpec; //Error thrown when not specified

public class Wallet {
    private PrivateKey privateKey;
    public PublicKey publicKey;

    public Wallet() {
         generateKeyPair();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    private void generateKeyPair() {
        try {
            // Instantiate various security components
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            // Initialize key generator and generate key pair
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            // Set our private and public keys
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
