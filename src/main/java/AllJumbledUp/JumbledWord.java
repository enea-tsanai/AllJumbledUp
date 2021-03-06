package AllJumbledUp;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by enea.
 * Date: 9/10/15.
 * Time: 2:40 AM.
 */

//Todo: Remove unused constructors and methods
public class JumbledWord {

    private final String word;
    private final String jumbledWord;
    private Set<Integer> specialPositions;

    public JumbledWord() {
        this(null);
    }

    public JumbledWord(String word) {
        this.word = word;
        this.jumbledWord = jumble(word);
    }

    public JumbledWord(String word, String spchars) {
        this.word = word;
        this.jumbledWord = jumble(word);
        this.specialPositions = spcharsToSps(spchars);
    }

    public JumbledWord(String word, List<Integer> lsp) {
        this.word = word;
        this.jumbledWord = jumble(word);
        this.specialPositions = listToSet(lsp);
    }

    /*Returns the mask of the hidden word*/
    public String getMask() {
        StringBuilder mask = new StringBuilder(String.format("%1$"+ word.length() + "s", " "));
        for(int pos: specialPositions) {
            mask.setCharAt(pos, '_');
        }
        return mask.toString();
    }

    /*Returns the special characters as a string*/
    public String getSPchars() {
        String spchars = "";
        for(int pos: specialPositions) {
            spchars += getWord().charAt(pos);
        }
        return spchars;
    }

    public String getWord() {
        return word;
    }

    public String getJumbledWord() {
        return jumbledWord;
    }

    /* Jumbles the given word */
    public String jumble(String word) {
        StringBuilder jumbledWord = new StringBuilder(word.length());
        int numOfTries = 0;
        do {
            jumbledWord.setLength(0);
            List<Character> characters = new ArrayList<>();
            for (char c : word.toCharArray()) {
                characters.add(c);
            }

            while (characters.size() > 0) {
                int randPicker = (int) (Math.random() * characters.size());
                jumbledWord.append(characters.remove(randPicker));
            }
            numOfTries ++;
        } while (word.equals(jumbledWord.toString()) && numOfTries < 9);
        return jumbledWord.toString();
    }

    public static String jumble(String staticWord, String jWord) {
        if (jWord.length() > 1) {
            StringBuilder jumbledWord = new StringBuilder(jWord.length());
            int numOfTries = 0;
            do {
                jumbledWord.setLength(0);
                List<Character> characters = new ArrayList<>();
                for (char c : jWord.toCharArray()) {
                    characters.add(c);
                }

                while (characters.size() > 0) {
                    int randPicker = (int) (Math.random() * characters.size());
                    jumbledWord.append(characters.remove(randPicker));
                }
                numOfTries ++;
            } while (jWord.equals(jumbledWord.toString()) && numOfTries < 9);
            return jumbledWord.toString();
        }
        return jWord;
    }

    Set<Integer> listToSet(List<Integer> list) {
        if(list!=null)
            return new HashSet<>(list);
        return new HashSet<>(); //check this
    }

    Set<Integer> spcharsToSps(String spchars) {
        Set<Integer> sps = new HashSet<>();
        StringCharacterIterator it = new StringCharacterIterator(spchars);
        for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
           sps.add(word.indexOf(ch));
        }
        return sps; //check this
    }

    private void addSpecialPositions_(int sp) {
        specialPositions.add(sp);
    }

    public Set<Integer> getSpecialPositions() {
        return specialPositions;
    }

}
