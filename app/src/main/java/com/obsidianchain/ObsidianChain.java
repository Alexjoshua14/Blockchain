/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.obsidianchain;

import com.obsidianchain.components.*;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;


public class ObsidianChain {
    public static ArrayList<Block> blockchain = new ArrayList<Block>();

    public String getGreeting() {
        return "Hello World!";
    }

    public void createSomeBlocks() {
        blockchain.add(new Block("Hi im the first block", "0"));
		
		blockchain.add(new Block("Yo im the second block", blockchain.get(blockchain.size() - 1).hash));
		
		blockchain.add(new Block("Hey im the third block", blockchain.get(blockchain.size() - 1).hash));
		
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println(blockchainJson);

    }

    public static void main(String[] args) {
        ObsidianChain oc = new ObsidianChain();
        
        System.out.println(oc.getGreeting());
        oc.createSomeBlocks();
    }
}
