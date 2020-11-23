package com.example.Blockchain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class Blockchain
{
    private static int difficulty=3;
    private static String difficultyString="";
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");


    ArrayList<Transaction> unconfirmedTransactions = new ArrayList<Transaction>();
    ArrayList<Block> chain = new ArrayList<Block>();

    public Blockchain() {
        computedDifficultyString();
    }


    public void createGenesisBlock()
    {
        Block GenesisBlock = new Block(0,dateFormat.format(new Date()),"Genesis",0,new Transaction("T1"));
        GenesisBlock.setHash(Block.computeHash(GenesisBlock));
        chain.add(GenesisBlock);
    }

    public boolean addBlock(Block block,String proof)
    {
        String previousHash = getLastBlock().getHash();
        if (previousHash != block.getPreviousHash())
            return false;

        if (! this.isValidProof(block, proof)) return false;

        block.setHash(proof);
        chain.add(block);
        return true;
    }

    public Block getLastBlock()
    {
        return chain.get(chain.size() - 1);
    }

    public Transaction getLastTransaction()
    {
        return unconfirmedTransactions.get(unconfirmedTransactions.size() - 1);
    }

    public void addNewTransaction(Transaction transaction)
    {
        unconfirmedTransactions.add(transaction);
    }

    String proofOfWork(Block block)
    {
        block.setNonce(0);
        String computedHash=Block.computeHash(block);

        while(true)
        {
            if (difficultyString.equals(computedHash.substring(0, difficulty))) break;
            System.out.println("run");
            block.setNonce(block.getNonce()+1);
            computedHash=Block.computeHash(block);
            System.out.println(computedHash);
        }

        return computedHash;
    }

    public void computedDifficultyString()
    {
        for (int i=0;i<difficulty;i++)
        {
            this.difficultyString = this.difficultyString + "a";
        }
    }

    boolean isValidProof(Block block, String blockHash){
        return (difficultyString.equals(blockHash.substring(0, difficulty)) &&
        blockHash == block.computeHash(block));
    }

    boolean checkChainValidity(ArrayList<Block> chain){
        String previousHash = "Genesis";
        for (Block block : chain) {
            String blockHash = block.getHash();
            //delattr(block, "hash")
            if ( ! this.isValidProof(block, blockHash) || previousHash != block.getPreviousHash())
                return false;

        }
        return true;
    }



    public Boolean mine(int nonce)
    {
        Block lastBlock = this.getLastBlock();
        Block newBlock=new Block(lastBlock.getIndex()+1,dateFormat.format(new Date()),Block.computeHash(lastBlock),nonce,getLastTransaction());
        String proof = this.proofOfWork(newBlock);
        this.addBlock(newBlock,proof);
        this.unconfirmedTransactions.clear();
        return true;
    }
}

