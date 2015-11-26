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

//TODO: Add comments
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

    public JumbledWord(String word, List<Integer> lsp) {
        this.word = word;
        this.jumbledWord = jumble(word);
        this.specialPositions = listToSet(lsp);
    }

    public JumbledWord(String word, String spchars) {
        this.word = word;
        this.jumbledWord = jumble(word);
        this.specialPositions = spcharsToSps(spchars);
    }

    /* Jumbles the given word */
    public String jumble(String word) {
        StringBuilder jumbledWord = new StringBuilder(word.length());
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
            System.out.println("Jumbled:  " + jumbledWord + "  Word: " + word);
        } while (word.equals(jumbledWord.toString()));
        return jumbledWord.toString();
    }

    public String getWord() {
        return word;
    }

    public String getJumbledWord() {
        return jumbledWord;
    }

    private void addSpecialPositions_(int sp) {
        specialPositions.add(sp);
    }

    public Set<Integer> getSpecialPositions() {
        return specialPositions;
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

    /*Returns the mask of the hidden word*/
    String getMask() {
        StringBuilder mask = new StringBuilder(String.format("%1$"+ word.length() + "s", " "));
        for(int pos: specialPositions) {
            mask.setCharAt(pos, '_');
        }
        return mask.toString();
    }

    /*Returns the special characters as a string*/
    String getSPchars() {
        String spchars = "";
        for(int pos: specialPositions) {
            spchars += getWord().charAt(pos);
        }
        return spchars;
    }

}
