package AllJumbledUp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by enea on 9/10/15.
 */

//TODO: Add comments
public class JumbledWord {

    private final String Word_;
    private final String JumbledWord_;
    private Set<Integer> SpecialPositions_;

    public JumbledWord() {
        this(null);
    }

    public JumbledWord(String word) {
        this.Word_ = word;
        this.JumbledWord_ = jumble(word);
    }

    public JumbledWord(String word, List<Integer> lsp) {
        this.Word_ = word;
        this.JumbledWord_ = jumble(word);
        this.SpecialPositions_ = listToSet(lsp);
    }

    /* Jumbles the given word */
    public String jumble(String word) {
        //TODO: Implement logic
        return word + "";
    }

    public String getWord() {
        return Word_;
    }

    public String getJumbledWord() {
        return JumbledWord_;
    }

    private void addSpecialPositions_(int sp) {
        SpecialPositions_.add(sp);
    }

    public Set<Integer> getSpecialPositions_() {
        return SpecialPositions_;
    }

    Set<Integer> listToSet(List<Integer> list) {
        if(list!=null)
            return new HashSet<Integer>(list);
        return new HashSet<Integer>(); //check this
    }

    /*Returns the mask of the hidden word*/
    String getMask() {
        StringBuilder mask = new StringBuilder(String.format("%1$"+Word_.length() + "s", " "));
        for(int pos:SpecialPositions_) {
            mask.setCharAt(pos, '_');
        }
        return mask.toString();
    }

}
