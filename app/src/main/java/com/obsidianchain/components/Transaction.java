package com.obsidianchain.components;

import com.obsidianchain.utilities.*;
import java.security.*;
import java.util.ArrayList;

public class Transaction {
    public String transactionId;
    public PublicKey sender;
    public PublicKey recipient;
    public float value;
    public byte[] signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

    // Used to keep track of number of transactions generated
    private static int sequence = 0; 

    public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    private String calculateHash() {
        sequence++;
        return StringUtil.applySHA256(
                            compileData(sender, recipient, value) +
                            sequence
                            );
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = compileData(sender, recipient, value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = compileData(sender, recipient, value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public String compileData(PublicKey s, PublicKey r, float f) {
        return StringUtil.getStringFromKey(sender) + StringUtil.getStringFromKey(recipient) + Float.toString(value);
    }
 }