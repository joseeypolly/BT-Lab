import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MerkleTree {

    // MerkleNode class to represent each node in the tree
    static class MerkleNode {
        String hash;
        MerkleNode left;
        MerkleNode right;

        public MerkleNode(String hash) {
            this.hash = hash;
        }
    }

    // Method to compute SHA-256 hash
    public static String computeSHA256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Method to build the Merkle Tree from a list of data blocks
    public static MerkleNode buildMerkleTree(List<String> dataBlocks) {
        List<MerkleNode> currentLevel = new ArrayList<>();

        // Create leaf nodes
        for (String data : dataBlocks) {
            currentLevel.add(new MerkleNode(computeSHA256(data)));
        }

        // Build tree from leaf nodes upwards
        while (currentLevel.size() > 1) {
            List<MerkleNode> nextLevel = new ArrayList<>();

            for (int i = 0; i < currentLevel.size(); i += 2) {
                MerkleNode left = currentLevel.get(i);
                MerkleNode right = (i + 1 < currentLevel.size()) ? currentLevel.get(i + 1) : left;

                // Combine the left and right node hashes
                String combinedHash = computeSHA256(left.hash + right.hash);
                MerkleNode parentNode = new MerkleNode(combinedHash);
                parentNode.left = left;
                parentNode.right = right;

                nextLevel.add(parentNode);
            }
            currentLevel = nextLevel;
        }

        // Root node of the tree
        return currentLevel.get(0);
    }

    // Helper method to display the Merkle root
    public static String getMerkleRoot(MerkleNode root) {
        return root.hash;
    }

    public static void main(String[] args) {
        // Sample data blocks
        List<String> dataBlocks = new ArrayList<>();
        dataBlocks.add("Block A");
        dataBlocks.add("Block B");
        dataBlocks.add("Block C");
        dataBlocks.add("Block D");

        // Build Merkle Tree
        MerkleNode root = buildMerkleTree(dataBlocks);

        // Display Merkle root
        System.out.println("Merkle Root: " + getMerkleRoot(root));
    }
}
