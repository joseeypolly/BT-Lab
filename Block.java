import java.security.MessageDigest;
import java.util.Date;

public class Block {

    private int index;            // Block index in the chain
    private long timestamp;       // Block creation timestamp
    private String data;          // Block data (e.g., transaction details)
    private String previousHash;  // Hash of the previous block
    private String hash;          // Hash of this block
    private int nonce;            // Nonce for Proof of Work

    // Constructor
    public Block(int index, String data, String previousHash) {
        this.index = index;
        this.timestamp = new Date().getTime();
        this.data = data;
        this.previousHash = previousHash;
        this.nonce = 0;  // Start with nonce 0
        this.hash = calculateHash();  // Calculate the block's hash
    }

    // Method to calculate the hash of the block
    public String calculateHash() {
        String input = index + Long.toString(timestamp) + data + previousHash + nonce;
        return applySHA256(input);
    }

    // Method to apply SHA-256 to a string input
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

    // Proof of Work: Find a hash with leading zeros (difficulty level)
    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block mined: " + hash);
    }

    // Getters for block attributes
    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getData() {
        return data;
    }

    public int getIndex() {
        return index;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getNonce() {
        return nonce;
    }

    // Main method for demonstration
    public static void main(String[] args) {
        // Difficulty level for mining (number of leading zeros)
        int difficulty = 4;

        // Create the first (genesis) block
        Block genesisBlock = new Block(0, "Genesis Block", "0");
        System.out.println("Mining genesis block...");
        genesisBlock.mineBlock(difficulty);

        // Create the second block
        Block secondBlock = new Block(1, "Second Block Data", genesisBlock.getHash());
        System.out.println("Mining second block...");
        secondBlock.mineBlock(difficulty);
    }
}
