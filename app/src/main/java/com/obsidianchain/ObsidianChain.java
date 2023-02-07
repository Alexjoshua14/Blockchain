/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.obsidianchain;

import com.obsidianchain.components.*;

public class ObsidianChain {

    public String getGreeting() {
        return "Hello World!";
    }

    public void createSomeBlocks() {
        Block genesisBlock = new Block("Hi im the first block", "0");
		System.out.println("Hash for block 1 : " + genesisBlock.hash);
		
		Block secondBlock = new Block("Yo im the second block",genesisBlock.hash);
		System.out.println("Hash for block 2 : " + secondBlock.hash);
		
		Block thirdBlock = new Block("Hey im the third block",secondBlock.hash);
		System.out.println("Hash for block 3 : " + thirdBlock.hash);
    }

    public static void main(String[] args) {
        ObsidianChain oc = new ObsidianChain();
        
        System.out.println(oc.getGreeting());
        oc.createSomeBlocks();
    }
}
