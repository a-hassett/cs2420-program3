// AvlTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x (unimplemented)
// boolean contains( x )  --> Return true if x is present
// boolean remove( x )    --> Return true if x was present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate
/**
 * Implements an AVL tree.
 * Note that all "matching" is based on the compareTo method.
 * @author Mark Allen Weiss
 */
public class AVLTree<AnyType extends Comparable<? super AnyType>> {
    AvlNode<AnyType> root;

    /**
     * Construct the tree.
     */
    public AVLTree() {
        root = null;
    }

    /**
     * @param value the item to insert.
     */
    public void insert(AnyType value) {
        root = insert(value, root);
    }

    /**
     * Internal method to insert into a subtree.  Duplicates are allowed
     *
     * @param value the item to insert.
     * @param newRoot the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> insert(AnyType value, AvlNode<AnyType> newRoot) {
        if (newRoot == null)
            return new AvlNode<>(value, null, null);
        int compareResult = value.compareTo(newRoot.element);
        if (compareResult < 0)
            newRoot.left = insert(value, newRoot.left);
        else
            newRoot.right = insert(value, newRoot.right);
        return balance(newRoot);
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     *
     * @param item the item to remove.
     */
    public void remove(AnyType item) {
        root = remove(item, root);
    }

    /**
     * Internal method to remove from a subtree.
     *
     * @param item the item to remove.
     * @param node the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> remove(AnyType item, AvlNode<AnyType> node) {
        if (node == null) {
            return node;   // Item not found; do nothing
        }

        int compareResult = item.compareTo(node.element);
        if (compareResult < 0) {
            node.left = remove(item, node.left);
        } else if (compareResult > 0) {
            node.right = remove(item, node.right);
        } else if (node.left != null && node.right != null){ // Two children
            node.element = findMin(node.right).element;
            node.right = remove(node.element, node.right); // this new node is then used recursively
        } else {
            node = (node.left != null) ? node.left : node.right;
        }
        return balance(node);
    }

    /**
     * Find an item in the tree.
     *
     * @param value the item to search for.
     * @return true if x is found.
     */
    public boolean contains(AnyType value) {
        return contains(value, root);
    }

    /**
     * Internal method to find an item in a subtree.
     *
     * @param value is item to search for.
     * @param node the node that roots the tree.
     * @return true if x is found in subtree.
     */
    private boolean contains(AnyType value, AvlNode<AnyType> node) {
        while (node != null) {
            int compareResult = value.compareTo(node.element);
            if (compareResult < 0)
                node = node.left;
            else if (compareResult > 0)
                node = node.right;
            else
                return true;    // Match
        }
        return false;   // No match
    }

    // use the strategy from the youtube video
    // delete Min by taking the minimum node and another node and switching
    // node = min
    // node = other
    // other.element = min.element, other.left = min.left, other.right = min.right
    // balance the tree

    public AnyType deleteMin() {
        NodeInfo<AnyType> data = deleteMin(root);
        root = data.newRoot;
        AnyType deletedPartial = data.element;
        balance(root);
        return deletedPartial;
    }

    private NodeInfo<AnyType> deleteMin(AvlNode<AnyType> t) {
        NodeInfo<AnyType> returnData = new NodeInfo<>(null, null);

        if(t == null){
            returnData.element = null;
            returnData.newRoot = null;
        } else if(t.left == null){
            returnData.element = t.element;
            returnData.newRoot = t.right;
        } else if(t.left.left == null){
            returnData.element = t.left.element;
            t.left = t.left.right;
            returnData.newRoot = t;
        } else{
            return deleteMin(t.left);
        }
        return returnData;
    }

    /**
     * Find the smallest item in the tree.
     *
     * @return smallest item or null if empty.
     */
    public AnyType findMin() {
        if (isEmpty())
            throw new RuntimeException();
        return findMin(root).element;
    }

    /**
     * Internal method to find the smallest item in a subtree.
     *
     * @param current the node that roots the tree.
     * @return node containing the smallest item.
     */
    private AvlNode<AnyType> findMin(AvlNode<AnyType> current) {
        if (current == null)
            return current;
        while (current.left != null)
            current = current.left;
        return current;
    }

    /**
     * Find the largest item in the tree.
     *
     * @return the largest item of null if empty.
     */
    public AnyType findMax() {
        if (isEmpty())
            throw new RuntimeException();
        return findMax(root).element;
    }

    /**
     * Internal method to find the largest item in a subtree.
     *
     * @param node the node that roots the tree.
     * @return node containing the largest item.
     */
    private AvlNode<AnyType> findMax(AvlNode<AnyType> node) {
        if (node == null)
            return node;
        while (node.right != null)
            node = node.right;
        return node;
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty() {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree(String label) {
        System.out.println(label);
        if (isEmpty())
            System.out.println("Empty tree");
        else
            printTree(root, "");
    }

    /**
     * Internal method to print a subtree in sorted order.
     *
     * @param t the node that roots the tree.
     */
    private void printTree(AvlNode<AnyType> t, String indent) {
        if (t != null) {
            printTree(t.right, indent + "   ");
            System.out.println(indent + t.element + "(" + t.height + ")");
            printTree(t.left, indent + "   ");
        }
    }

    private static final int ALLOWED_IMBALANCE = 1;

    // Assume t is either balanced or within one of being balanced
    private AvlNode<AnyType> balance(AvlNode<AnyType> t) {
        if (t == null)
            return t;
        if (height(t.left) - height(t.right) > ALLOWED_IMBALANCE)
            if (height(t.left.left) >= height(t.left.right))
                t = rightRotation(t);
            else
                t = doubleRightRotation(t);
        else if (height(t.right) - height(t.left) > ALLOWED_IMBALANCE)
            if (height(t.right.right) >= height(t.right.left))
                t = leftRotation(t);
            else
                t = doubleLeftRotation(t);
        t.height = Math.max(height(t.left), height(t.right)) + 1;
        return t;
    }

    public void checkBalance() {
        checkBalance(root);
    }

    private int checkBalance(AvlNode<AnyType> parent) {
        if (parent == null) {
            return -1;
        } else {
            int heightLeft = checkBalance(parent.left);
            int heightRight = checkBalance(parent.right);
            if (Math.abs(height(parent.left) - height(parent.right)) > 1 || height(parent.left) != heightLeft || height(parent.right) != heightRight)
                System.out.println("\n\n***********************OOPS!!");
        }
        return height(parent);
    }

    /**
     * Return the height of node t, or -1, if null.
     */
    private int height(AvlNode<AnyType> t) {
        if (t == null) return -1;
        return t.height;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> rightRotation(AvlNode<AnyType> t) {
        AvlNode<AnyType> theLeft = t.left;
        t.left = theLeft.right;
        theLeft.right = t;
        t.height = Math.max(height(t.left), height(t.right)) + 1;
        theLeft.height = Math.max(height(theLeft.left), t.height) + 1;
        return theLeft;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> leftRotation(AvlNode<AnyType> t) {
        AvlNode<AnyType> theRight = t.right;
        t.right = theRight.left;
        theRight.left = t;
        t.height = Math.max(height(t.left), height(t.right)) + 1;
        theRight.height = Math.max(height(theRight.right), t.height) + 1;
        return theRight;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleRightRotation(AvlNode<AnyType> t) {
        t.left = leftRotation(t.left);
        return rightRotation(t);
    }

    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleLeftRotation(AvlNode<AnyType> t) {
        t.right = rightRotation(t.right);
        return leftRotation(t);
    }

    private static class AvlNode<AnyType> {
        // Constructors
        AvlNode(AnyType theElement) {
            this(theElement, null, null);
        }

        AvlNode(AnyType theElement, AvlNode<AnyType> leftTree, AvlNode<AnyType> rightTree) {
            element = theElement;
            left = leftTree;
            right = rightTree;
            height = 0;
        }

        AnyType element;      // The data in the node
        AvlNode<AnyType> left;         // Left child
        AvlNode<AnyType> right;        // Right child
        int height;       // Height
    }

    private static class NodeInfo<AnyType>{
        AnyType element;
        AvlNode<AnyType> newRoot;

        NodeInfo(AnyType element, AvlNode<AnyType> n){
            this.element = element;
            this.newRoot = n;
        }
    }

}