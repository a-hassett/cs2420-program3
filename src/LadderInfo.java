/**
 * This class creates WordInfo objects which allows the program
 * to keep track of words, their partial ladder, and number of moves in the word 
 ladder.
 */
public class LadderInfo implements Comparable <LadderInfo> {
    public String word;  // last word in the word ladder
    public int moves;    // number of in the word ladder
    public String ladder;// printable representation of ladder

    public LadderInfo(String word, int moves, String ladder){
        this.word = word;
        this.moves = moves;
        this.ladder = ladder;
    }
    public String toString(){
        return "Word " + word + " Moves " + moves + " Ladder ["+ ladder +"]";
    }

    @Override
    public int compareTo(LadderInfo ladder2) {
        int priority1 = lettersAway(this) + this.moves;
        int priority2 = lettersAway(ladder2) + ladder2.moves;

        return Integer.compare(priority1, priority2);
    }
    private int lettersAway(LadderInfo path){
        String[] wordsSoFar = path.ladder.split(" ");
        String lastWordStr = wordsSoFar[wordsSoFar.length - 1];
        char[] firstWord = path.word.toCharArray();
        char[] lastWord = lastWordStr.toCharArray();
        int lettersAway = 0;
        for(int i = 0; i < path.word.length(); i++){
            if(firstWord[i] != lastWord[i]){
                lettersAway++;
            }
        }
        return lettersAway;
    }
}