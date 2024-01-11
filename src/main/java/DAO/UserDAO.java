package DAO;

import Helper.JDBC;
import Model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserDAO class manages the methods used to interact with Users in the database.
 */
public class UserDAO {

    /**
     * Get all users by performing a SQL query statement to the database.
     *
     * @return allusers all users from database.
     */
    public static ObservableList<Users> getEveryUser() {
        ObservableList<Users> everyUser = FXCollections.observableArrayList();
        try {
            String select = "SELECT * FROM users";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int user_Id = rs.getInt("User_ID");
                String loginName = rs.getString("User_Name");
                String loginPW = rs.getString("Password");
                everyUser.add(new Users(user_Id, loginName, loginPW));
            }
            // SQLException catch used in case an error occurs when accessing the database.
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return everyUser;
    }

    /**
     * Get user by ID by performing a SQL query statement to the database.
     *
     * @throws SQLException if an error occurs when accessing the database.
     * @return new if a user is found via the User ID.
     * @return null if a user is not found via the User ID.
     */
    public static Users getUserInfo(int userID) throws SQLException {
        String select = "SELECT * FROM users WHERE User_ID = ?";
        PreparedStatement preparedstatement = JDBC.getConnection().prepareStatement(select);
        preparedstatement.setInt(1, userID);
        ResultSet resultset = preparedstatement.executeQuery();
        if (resultset.next()) {
            String loginName = resultset.getString("User_Name");
            String loginPW = resultset.getString("Password");
            return new Users(userID, loginName, loginPW);
        }
        return null;
    }
}

