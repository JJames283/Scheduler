package DAO;

import Helper.JDBC;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * LoginDAO class manages the methods used to interact with Login information (username, passwords)
 * in the database.
 */
public class LoginDAO {

    /**
     * Based on the username entered by a user, the database is accessed and the username's associated
     * password is compared to the password entered by a user.
     *
     * @param pw user selects the login button.
     * @throws SQLException if an error occurs when accessing the database.
     * @return true if the password from the database match the password entered by the user.
     * @return false if the password from the database do not match the password entered by the user.
     */
    public static boolean comparePW(String loginname, String pw) throws SQLException {
        String select = "SELECT * FROM users WHERE User_Name = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(select);
        ps.setString(1, loginname);
        ResultSet resultset = ps.executeQuery();
        if (resultset.next()) {
            String savedPW = resultset.getString("Password");
            if (pw.equals(savedPW)) {
                return true;
            }
        }
        return false;
    }
}
