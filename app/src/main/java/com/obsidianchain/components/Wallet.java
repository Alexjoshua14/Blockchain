package com.obsidianchain.components;

import com.obsidianchain.ObsidianChain;
import java.security.*;
import java.security.spec.ECGenParameterSpec; //Error thrown when not specified
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

/* Stores information for a specific wallet
 * Information includes:
 *      Public & Private keys
 *      List of UTXOs owned by this wallet
 */
public class Wallet {
    private PrivateKey privateKey;
    public PublicKey publicKey;

    public HashMap<String, TransactionOutput> UTXOs = 
        new HashMap<String, TransactionOutput>();

    public Wallet() {
         generateKeyPair();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    /* Generates a public and private key pair
     * 
     */
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

    /* Goes through all UTXOs stored in ObsidianChain and adds the ones owned
     * by this wallet to this.UTXOs and returns the total value owned
     */
    public float getBalance() {
        return ObsidianChain.UTXOs.entrySet().stream()
                .map(transOut -> transOut.getValue())
                .filter(UTXO -> UTXO.isMine(publicKey))
                .map(UTXO -> {
                    UTXOs.put(UTXO.id, UTXO);
                    return UTXO.value;
                })
                .reduce(0f, (value, total) -> total + value);
    }

    public Transaction sendFunds(PublicKey recipient, float value) {
        if (getBalance() < value) {
            System.out.println("#Not enough funds to complete transaction.");
            return null;
        }

        ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

        float total = 0f;
        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.value;
            inputs.add(new TransactionInput(UTXO.id));
            if (total > value) 
                break;
        }

        Transaction newTransaction = new Transaction(publicKey, recipient, value, inputs);
        newTransaction.generateSignature(privateKey);

        inputs.stream()
            .forEach(i -> UTXOs.remove(i.transactionOutputId));
        return newTransaction;
    }


}
