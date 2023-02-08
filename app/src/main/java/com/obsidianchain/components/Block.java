package com.obsidianchain.components;

import com.obsidianchain.utilities.StringUtil;
import java.util.Date;
import java.util.ArrayList;

public class Block {
    public String hash;
    public String previousHash;
    public String merkleRoot;
    private ArrayList<Transaction> transactions =
        new ArrayList<Transaction>();
    private long timestamp;
    private int nonce;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
    }

    /* Calculate hash for current block based on previous block's hash, timestamp of when this block was created, 
     * and the data contained within this block.
     */
    public String calculateHash() {
        String calculatedHash = StringUtil.applySHA256(
            previousHash +
            Long.toString(timestamp) +
            Integer.toString(nonce) +
            merkleRoot
        );

        return calculatedHash;
    }

    public void mine(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = new String(new char[difficulty]).replace('\0', '0');

        while (!hash.substring( 0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
            
        }
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) return false;
        if (previousHash != "0" && !transaction.processTransaction()) {
            System.out.println("Transaction failed to process");
            return false;
        }

        transactions.add(transaction);
        System.out.println("Transaction successfully added to Block");
        return true;
    }
}
