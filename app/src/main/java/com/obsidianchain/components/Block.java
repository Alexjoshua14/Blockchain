package com.obsidianchain.components;

import java.util.Date;
import com.obsidianchain.utilities.StringUtil;

public class Block {
    public String hash;
    public String previousHash;
    private String data;
    private long timestamp;

    public Block(String data, String previousHash) {
        this.data = data;
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
            data
        );

        return calculatedHash;
    }
}
