package DAO;

import Helper.JDBC;
import Model.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * ContactsDAO class manages the methods used to interact with Contacts in the database.
 */
public class ContactsDAO {

    /**
     * Get every contact (get all contacts) in the database by performing a SQL query to the database.
     *
     * @return allContacts all contacts from database.
     */
    public static ObservableList<Contacts> getEveryContact() {
        ObservableList<Contacts> allContacts = FXCollections.observableArrayList();
        try {
            String select = "SELECT * FROM contacts";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String contactEmail = rs.getString("Email");

                allContacts.add(new Contacts(contactID, contactName, contactEmail));
            }
            // SQLException catch used in case an error occurs when accessing the database.
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return allContacts;
    }

    /**
     * Get contact by ID by performing a SQL query statement to the database.
     *
     * @throws SQLException if an error occurs when accessing the database.
     * @return new if a contact is found via the Contact ID.
     * @return null if a contact is not found via the Contact ID.
     */
    public static Contacts getContactUsingContact_ID(int contactID) throws SQLException {
        String select = "SELECT * FROM contacts WHERE Contact_ID = ?";
        PreparedStatement prestate = JDBC.getConnection().prepareStatement(select);
        prestate.setInt(1, contactID);
        ResultSet resultset = prestate.executeQuery();
        if (resultset.next()) {
            String Name = resultset.getString("Contact_Name");
            String contactEmail = resultset.getString("Email");
            return new Contacts(contactID, Name, contactEmail);
        }
        return null;
    }
}

