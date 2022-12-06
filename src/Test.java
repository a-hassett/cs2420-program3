public class Test {
    // Test program
    public static void main( String [ ] args ) {
//        AVLTree<Integer> t = new AVLTree<>();
//        AVLTree<Dwarf> t2 = new AVLTree<>();
//        String[] nameList = {"Snowflake", "Sneezy", "Doc", "Grumpy", "Bashful",
//                "Dopey", "Happy", "Doc", "Grumpy", "Bashful", "Doc", "Grumpy", "Bashful"};
//        for (int i=0; i < nameList.length; i++) {
//            t2.insert(new Dwarf(nameList[i]));
//        }
//        t2.printTree( "The Tree" );
//        t2.remove(new Dwarf("Bashful"));
//        t2.printTree( "The Tree after delete Bashful" );
//        for (int i=0; i < 8; i++) {
//            t2.deleteMin();
//            t2.printTree( "\n\n The Tree after deleteMin" );
//        }
//
//        AVLTree<LadderInfo> ladder = new AVLTree<>();
//
//        int moves = 0;
//        LadderInfo node1 = new LadderInfo("word", 3, "word wooo tork");
//        LadderInfo node2 = new LadderInfo("stink", 2, "stink spark");
//        LadderInfo node3 = new LadderInfo("feet", 2, "feet tite");
//        //System.out.println(node1.compareTo(node2));
//        ladder.insert(node1);
//        ladder.insert(node2);
//        ladder.insert(node3);
//        ladder.printTree("First");
//        ladder.deleteMin();
//        ladder.printTree("Second");
//        System.out.println();
//        ladder.deleteMin();
//        System.out.println(ladder.deleteMin());
//        ladder.insert(node2);
//        ladder.printTree("Third");

        LadderGame game = new LadderGame("dictionary.txt");
        //game.interact();
        game.play("bed", "sad");
    }
}
