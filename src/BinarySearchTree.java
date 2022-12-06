import java.lang.Comparable;


public class BinarySearchTree <E extends Comparable> {
    public TreeNode root;

    /** SEE WHETHER A CERTAIN VALUE EXISTS IN THE TREE **/
    public boolean search(E search) {
        TreeNode node = root;

        boolean found = false;

        // keep going until we run out of nodes or until we find it
        while (!found && node != null) {
            if (node.value.compareTo(search) == 0) {
                found = true;
            } else if (node.value.compareTo(search) < 0) {
                node = node.right;
            } else {  //if node.value > search
                node = node.left;
            }
        }
        return found;
    }

    /** ADD WORDS TO THE TREE **/
    public boolean insert(E value) {
        if (root == null) {
            root = new TreeNode(value);
        } else {
            // Search/find the insert location
            TreeNode parent = null;
            TreeNode node = root;  //reference to previous root

            while (node != null) {
                parent = node;  //parent holds previous value while node searches down tree until it hits empty
                if (node.value.compareTo(value) == 0){
                    return false;
                } else if (node.value.compareTo(value) < 0) {
                    node = node.right;
                } else {
                    node = node.left;
                }
            }

            // Add the node to the tree
            TreeNode newNode = new TreeNode(value);

            if (parent.value.compareTo(value) < 0) {
                parent.right = newNode;
            } else {
                parent.left = newNode;
            }
        }
        return true;
    }

    /** TAKE OUT WORDS FROM THE TREE **/
    public boolean remove(E value) {
        // Step 1: find the node to remove
        TreeNode parent = null;
        TreeNode node = root;
        boolean done = false;

        //similar to while loop in insert() method
        while (!done) {
            if (value.compareTo(node.value) < 0) {
                parent = node;
                if(node.left == null){
                    return false;
                }
                node = node.left;
            }
            else if (value.compareTo(node.value) > 0) {
                parent = node;
                if(node.right == null){
                    return false;
                }
                node = node.right;
            }
            else {
                done = true;
            }
        }

        // Step 2a: case for no left child
        if (node.left == null) {
            if (parent == null) {
                root = node.right;
            }
            else {
                if (value.compareTo(parent.value) < 0) {
                    parent.left = node.right;
                }
                else {
                    parent.right = node.right;
                }
            }
        } else { // Step 2b: case for left child
            TreeNode parentOfRight = node;
            TreeNode rightMost = node.left;
            while (rightMost.right != null) {
                parentOfRight = rightMost;
                rightMost = rightMost.right;
            }
            // Copy the largest value into the node being removed
            node.value = rightMost.value;
            if (parentOfRight.right == rightMost) {
                parentOfRight.right = rightMost.left;
            }
            else {
                parentOfRight.left = rightMost.left;
            }
        }
        return true;
    }

    private class TreeNode {
        public E value;
        public TreeNode left;
        public TreeNode right;
        public TreeNode(E value) {
            this.value = value;
        }
    }
}