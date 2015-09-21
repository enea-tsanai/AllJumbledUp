package AllJumbledUp;

import com.sun.xml.internal.bind.v2.TODO;

/**
 * Created by enea on 9/10/15.
 */

//TODO: Add comments
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
        //TODO: Implement logic
        return word + "";
    }

    public String getWord() {
        return Word_;
    }

    public String getJumbledWord() {
        return JumbledWord_;
    }
}
