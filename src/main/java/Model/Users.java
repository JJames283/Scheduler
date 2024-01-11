package Model;

/**
 * This class defines and manges Users.
 */
public class Users {
    int user_Id;

    /**
     * Users constructor.
     */
    public Users(int user_ID, String userName, String PW) {
        this.user_Id = user_ID;
    }

    /**
     * @return the user_Id.
     */
    public int getUserID() {
        return user_Id;
    }

    /**
     * @return the user_Id.
     */
    @Override
    public String toString() {
        return String.valueOf(user_Id);
    }
}
