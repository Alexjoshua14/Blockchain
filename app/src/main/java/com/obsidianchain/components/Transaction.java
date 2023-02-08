package com.obsidianchain.components;

import com.obsidianchain.ObsidianChain;
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
                            StringUtil.compileData(sender, recipient, value) +
                            sequence
                            );
    }

    public void generateSignature(PrivateKey privateKey) {
        String data = StringUtil.compileData(sender, recipient, value);
        signature = StringUtil.applyECDSASig(privateKey, data);
    }

    public boolean verifySignature() {
        String data = StringUtil.compileData(sender, recipient, value);
        return StringUtil.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction() {

        //Verify Signature
        if (!verifySignature()) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        inputs.stream()
            .forEach(i -> {
                i.UTXO = ObsidianChain.UTXOs.get(i.transactionOutputId);
            });
        
        float inputsValue = getInputsValue();

        if (inputsValue < ObsidianChain.minimumTransaction) {
            System.out.println("#Transactino Inputs too small: " + inputsValue);
            return false;
        }

        float leftOver = inputsValue - value;
        transactionId = calculateHash();
        
        if (leftOver < 0) {
            System.out.println("Insufficient funds.");
            return false;
        }

        outputs.add(new TransactionOutput( this.recipient, value, transactionId));
        outputs.add(new TransactionOutput( this.sender, leftOver, transactionId));


        outputs.stream()
                .forEach(o -> ObsidianChain.UTXOs.put(o.id, o));

        inputs.stream()
                .filter(i -> i != null)
                .forEach(i -> ObsidianChain.UTXOs.remove(i.UTXO.id));
        
        return true;
    }
    

    public float getInputsValue() {
        return inputs.stream()
            .filter(i -> i.UTXO != null)
            .map(i -> i.UTXO.value)
            .reduce(0f, (v, total) -> total + v);
    }

    public float getOutputsValue() {
        return outputs.stream()
                        .map(o -> o.value)
                        .reduce(0f, (v, total) -> total + v);
    }
 }