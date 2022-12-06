public class LinkedList <E> {
    private Node head;
    private Node tail;
    private int counter = 0;

    private class Node {
        public E value;
        public Node next;
        public Node (E value){
            this.value = value;
        }
    }

    /** ADD ITEMS TO END OF THE LINKED LIST **/
    public void enqueue (E value){
        Node newNode = new Node(value);
        if (head == null) {
            head = newNode;
        } else{
            tail.next = newNode;
        }
        tail = newNode;
        this.counter++;
    }

    /** REMOVE FIRST ITEM IN LINKED LIST AND RETURN IT **/
    public E dequeue () {
        if(head == null){
            return null;
        }
        Node current = head;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        this.counter--;
        return current.value;
    }

    /** PRINT EVERY ITEM IN LIST IN ORDER **/
    public void printList() {
        Node current = head;
        while (current != null){
            System.out.println(current.value.toString());
            current = current.next;
        }
    }

    /** PRINT A CERTAIN NUMBER OF ITEMS FROM FRONT OF LIST **/
    public void printList(int numNodes){
        Node current = head;
        for(int i = 0; i < numNodes; i++){
            if(current == null){
                break;
            }
            System.out.println(current.value.toString());
            current = current.next;
        }
    }

    /** NUMBER OF ITEMS IN LINKED LIST **/
    public int size(){
        return this.counter;
    }
}
