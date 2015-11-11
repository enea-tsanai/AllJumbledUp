package AllJumbledUp;

/**
 * Created by enea on 11/10/15.
 */
public class Session {
    private static String SessionID;
    private static String FullName;

    public Session () {
    }

    public Session (String SessionID, String FullName) {
        Session.SessionID = SessionID;
        Session.FullName = FullName;
    }

    public static String getSessionID () {
        return SessionID;
    }

    public static String getFullName () {
        return FullName;
    }

    public String toString () {
        return "Session: " + SessionID + " FullName: " + FullName;
    }
}
