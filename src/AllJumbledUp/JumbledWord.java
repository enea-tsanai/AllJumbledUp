package AllJumbledUp;

/**
 * Created by enea on 9/10/15.
 */
public class JumbledWord {

    private final String Word_;
    private final String JumbledWord_;

    public JumbledWord() {
        this(null);
    }

    public JumbledWord(String word) {
        this.Word_ = word;
        this.JumbledWord_ = jumble(word);
    }

    /* Jumbles the given word */
    public String jumble(String word) {
        return word + "";
    }

    public String getWord() {
        return Word_;
    }

    public String getJumbledWord() {
        return JumbledWord_;
    }
}
