package AllJumbledUp;

/**
 * Created by enea.
 * Date: 11/10/15.
 * Time: 2:52 AM.
 */

//TODO: Add Score history
public class Session {
    private static String SessionID;
    private static String FullName;
    private static String UserPicture;

    public Session () {
    }

    public Session (String SessionID, String FullName, String UserPicture) {
        Session.SessionID = SessionID;
        Session.FullName = FullName;
        Session.UserPicture = UserPicture;
    }

    public static String getSessionID () {
        return SessionID;
    }

    public static String getFullName () {
        return FullName;
    }

    public static String getUserPicture () {
        return UserPicture;
    }

    public String toString () {
        return "Session: " + SessionID + " FullName: " + FullName + " User Picture: " + UserPicture;
    }
}
