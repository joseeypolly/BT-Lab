import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Block class representing each block in the blockchain
class Block {
    private int index;            // Position in the blockchain
    private long timestamp;       // Block creation timestamp
    private String data;          // Transaction data (or any block data)
    private String previousHash;  // Hash of the previous block
    private String hash;          // Hash of the current block
    private int nonce;            // Nonce used for Proof of Work

    // Constructor to initialize a new block
    public Block(int index, String data, String previousHash) {
        this.index = index;
        this.timestamp = new Date().getTime();
        this.data = data;
        this.previousHash = previousHash;
        this.hash = calculateHash();  // Calculate hash when block is created
    }

    // Method to calculate the block's hash
    public String calculateHash() {
        String input = index + Long.toString(timestamp) + data + previousHash + nonce;
        return applySHA256(input);
    }

    // Apply SHA-256 hash function
    public static String applySHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Proof of Work: A simple mechanism to validate blocks
    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');  // Target: hash must start with 'difficulty' number of zeros
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + hash);
    }

    // Getters for accessing block details
    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }
}

// Blockchain class that manages the chain of blocks
// Blockchain class that manages the chain of blocks
class Blockchain {
    private List<Block> blockchain;  // List to hold all blocks in the chain
    private int difficulty;          // Difficulty level for Proof of Work

    // Constructor to initialize the blockchain with the genesis block
    public Blockchain(int difficulty) {
        this.blockchain = new ArrayList<>();
        this.difficulty = difficulty;
        // Add genesis block to the chain
        blockchain.add(createGenesisBlock());
    }

    // Method to create the genesis (first) block
    private Block createGenesisBlock() {
        return new Block(0, "Genesis Block", "0");
    }

    // Get the last block in the blockchain (make this method public)
    public Block getLatestBlock() {
        return blockchain.get(blockchain.size() - 1);
    }

    // Add a new block to the blockchain
    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);  // Mine the block (apply Proof of Work)
        blockchain.add(newBlock);
    }

    // Validate the blockchain by checking hashes and previous hashes
    public boolean isChainValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            // Check if the current block's hash is correct
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current block hash is invalid!");
                return false;
            }

            // Check if the current block points to the correct previous block's hash
            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                System.out.println("Previous block hash is invalid!");
                return false;
            }
        }
        return true;
    }

    // Display the blockchain information
    public void displayBlockchain() {
        for (Block block : blockchain) {
            System.out.println("Index: " + blockchain.indexOf(block));
            System.out.println("Previous Hash: " + block.getPreviousHash());
            System.out.println("Hash: " + block.getHash());
            System.out.println("-----------");
        }
    }
}
public class SimpleBlockchain {

    public static void main(String[] args) {
        // Set difficulty level for Proof of Work (higher means harder)
        int difficulty = 4;

        // Create a new blockchain
        Blockchain blockchain = new Blockchain(difficulty);

        // Add blocks to the blockchain
        System.out.println("Mining block 1...");
        blockchain.addBlock(new Block(1, "Transaction Data 1", blockchain.getLatestBlock().getHash()));

        System.out.println("Mining block 2...");
        blockchain.addBlock(new Block(2, "Transaction Data 2", blockchain.getLatestBlock().getHash()));

        System.out.println("Mining block 3...");
        blockchain.addBlock(new Block(3, "Transaction Data 3", blockchain.getLatestBlock().getHash()));

        // Display the blockchain
        blockchain.displayBlockchain();

        // Validate the blockchain
        System.out.println("Is Blockchain valid? " + blockchain.isChainValid());
    }
}
