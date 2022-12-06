import java.util.Objects;
import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.util.ArrayList;

public class LadderGame {
    static int MaxWordSize = 15;  //Max legnth word allowed
    ArrayList<String>[] allList;  // Array of ArrayLists of words of each length.
    Random random;  // Random number generator
    /**
     *  Creates separate ArrayLists for words of each length
     * @param dictionaryFileName  Contains all words to be used in word ladder in 
    alpha order
     */
    public LadderGame(String dictionaryFileName) {
        random = new Random();

        // build an array list where each item is another array list
        allList = new ArrayList[MaxWordSize];
        for (int i = 0; i < MaxWordSize; i++)
            allList[i] = new ArrayList<String>();

        // read the dictionary and add each word to its corresponding word length list
        try {
            Scanner reader = new Scanner(new File(dictionaryFileName));
            while (reader.hasNext()) {
                String word = reader.next().toLowerCase();
                if (word.length() < MaxWordSize){
                    allList[word.length()].add(word);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Call this function if user wants to give input after running the program
     * or wants random words to be picked for them
     */
    public void interact(){
        // user decides whether to input or get random words
        Scanner input = new Scanner(System.in);
        System.out.println("\nWould you like to provide words or have them randomly selected for you?");
        System.out.print("Enter (1) for provided or (2) for random: ");
        int inputType = input.nextInt();

        // initialize some variables for later
        String startWord;
        String endWord;

        if (inputType == 1) {  // GET WORDS FROM USER
            Scanner grabber = new Scanner(System.in);
            System.out.print("First word: ");
            startWord = grabber.nextLine().toLowerCase();
            System.out.print("Second word: ");
            endWord = grabber.nextLine().toLowerCase();

            play(startWord, endWord);

        } else if (inputType == 2) {  // PICK RANDOM WORDS
            int column = 1;
            int colLength = 0;

            // if the column is empty, pick a new column
            while(colLength <= 0){
                column = (int) (Math.random() * MaxWordSize);
                colLength = allList[column].size();
            }

            // if the random words are the same, pick new ones
            do { startWord = allList[column].get((int) (Math.random() * colLength));
                endWord = allList[column].get((int) (Math.random() * colLength));
            } while (Objects.equals(startWord, endWord));

            play(startWord, endWord);

        } else {  // TRY AGAIN TO GET WORDS
            System.out.println("Invalid type: did not enter (1) or (2)");
            interact();
        }
    }

    public void play(String startWord, String endWord){
        if (startWord.length() != endWord.length()){
            System.out.println("Words are not the same length");
            return;
        } else if (startWord.length()  >= MaxWordSize){
            System.out.println("The words are too long");
            return;
        } else if (startWord.length() <= 0){
            System.out.println("Words must be have at least one letter");
            return;
        }

        System.out.println("*********************************************************************");
        System.out.println("Seeking a solution from " + startWord + " -> " + endWord + " Size of List " + allList[startWord.length()].size());
        System.out.println();
        System.out.println("A* Solution:");
        aStarPlay(startWord, endWord);
        System.out.println();
        System.out.println("Brute Force Solution:");
        bruteForcePlay(startWord, endWord);
    }

    private void aStarPlay(String startWord, String endWord){
        BinarySearchTree<String> dictionary = new BinarySearchTree<>();
        java.util.Collections.shuffle(allList[startWord.length()], new java.util.Random(System.currentTimeMillis()));
        for(String word : allList[startWord.length()]){
            dictionary.insert(word);
        }

        int numEnqueues = 0;

        AVLTree<LadderInfo> priorityQ = new AVLTree<>();
        priorityQ.insert(new LadderInfo(endWord, 0, startWord));
        numEnqueues++;

        boolean done = false;

        while(!priorityQ.isEmpty() && !done){
            LadderInfo info = priorityQ.deleteMin();

            String[] words = info.ladder.split(" ");
            String lastWord = words[words.length - 1];
            char[] word = lastWord.toCharArray();  // holds original word for reference
            char[] dynamicWord = lastWord.toCharArray();  // will change in the for loops below

            // CHECK FOR EVERY POSSIBLE WORD PATH
            for(int i = 0; i < dynamicWord.length; i++){  // change each letter of word one at a time
                for(int j = (int)'a'; j <= (int)'z'; j++){  // go through entire alphabet
                    // split and recombine one-letter-off words
                    dynamicWord[i] = (char)j;
                    String checkWord = String.valueOf(dynamicWord);

                    if(dictionary.search(checkWord)){
                        // ADD TO QUEUE IF VALID WORD
                        LadderInfo newLadder = new LadderInfo(endWord, info.moves + 1, info.ladder + " " + checkWord);
                        String[] pastWords = info.ladder.split(" ");
                        boolean alreadyDone = false;
                        for(int k = 0; k < pastWords.length; k++){
                            if(checkWord.equals(pastWords[k])){
                                alreadyDone = true;
                            }
                        }
                        if(!alreadyDone){
                            priorityQ.insert(newLadder);
                            numEnqueues++;
                        }

                        // IF FOUND, PRINT TO SCREEN
                        if(checkWord.equals(endWord)){
                            System.out.println(newLadder);
                            System.out.println("Total enqueues: " + numEnqueues + "\n");
                            priorityQ.makeEmpty();
                            done = true;
                            return;
                        }
                    }
                }
                dynamicWord[i] = word[i];
            }
        }
    }

    /**
     * Given starting and ending words, create a word ladder of minimal length.
     * @param startWord  Beginning word of word ladder
     * @param endWord  Ending word on word ladder
     */
    private void bruteForcePlay(String startWord, String endWord) {
        // make a bst of all the words of the correct length
        BinarySearchTree<String> dictTree = new BinarySearchTree<>();
        java.util.Collections.shuffle(allList[startWord.length()], new java.util.Random(System.currentTimeMillis()));
        for(String word : allList[startWord.length()]){
            dictTree.insert(word);
        }

        if (!dictTree.search(startWord) || !dictTree.search(endWord)){
            System.out.println("Words do not both exist in the dictionary");
            return;
        }

        LinkedList<LadderInfo> queue = new LinkedList<>();
        findLadder(dictTree, queue, startWord, endWord);
    }

    private static void findLadder(BinarySearchTree<String> dictionary, LinkedList<LadderInfo> partials, String startWord, String endWord){
        int numEnqueues = 0;

        // special case
        if(Objects.equals(startWord, endWord)){
            LadderInfo ladder = new LadderInfo(startWord, 0, startWord);
            System.out.println(ladder);
            return;
        }

        // set initial partial ladder
        dictionary.remove(startWord);
        partials.enqueue(new LadderInfo(startWord, 0, startWord));
        numEnqueues++;

        boolean done = false;

        while((partials.size() > 0) && (!done)){
            LadderInfo info = partials.dequeue();

            char[] word = info.word.toCharArray();  // holds original word for reference
            char[] dynamicWord = info.word.toCharArray();  // will change in the for loops below

            // CHECK FOR EVERY POSSIBLE WORD PATH
            for(int i = 0; i < dynamicWord.length; i++){  // change each letter of word one at a time
                for(int j = (int)'a'; j <= (int)'z'; j++){  // go through entire alphabet
                    // split and recombine one-letter-off words
                    dynamicWord[i] = (char)j;
                    String checkWord = String.valueOf(dynamicWord);

                    if(dictionary.search(checkWord)){
                        dictionary.remove(checkWord);

                        // ADD TO QUEUE IF VALID WORD
                        LadderInfo newLadder = new LadderInfo(checkWord, info.moves + 1, info.ladder + " " + checkWord);
                        partials.enqueue(newLadder);
                        numEnqueues++;

                        // IF FOUND, PRINT TO SCREEN
                        if(checkWord.equals(endWord)){
                            System.out.println(newLadder);
                            System.out.println("Total enqueues: " + numEnqueues + "\n");
                            return;
                        }
                    }
                }
                dynamicWord[i] = word[i];
            }
        }
        // IF NOT FOUND, PROVE REASONABLE ATTEMPT
        System.out.println("Could not find a word ladder :(");
        System.out.println("Total enqueues: " + numEnqueues + "\n");
    }
    /**
     * Find a word ladder between random words of length len
     * @param len  Length of words in desired word ladder
     */
    /**
     * Is only used to test the ladder and provides no real functionality
     */
    public void play(int len) {
        if (len >= MaxWordSize){
            return;
        }
        ArrayList<String> list = allList[len];
        String startWord = list.get(random.nextInt(list.size()));
        String endWord = list.get(random.nextInt(list.size()));

        System.out.println("*********************************************************************");
        System.out.println("Seeking a solution from " + startWord + " -> " + endWord + " Size of List " + allList[startWord.length()].size());
        System.out.println();
        System.out.println("A* Solution:");
        aStarPlay(startWord, endWord);
        System.out.println();
        System.out.println("Brute Force Solution:");
        bruteForcePlay(startWord, endWord);
    }
}