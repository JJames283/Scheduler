package DAO;

import Helper.JDBC;
import Model.Countries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * CountriesDAO class manages the methods used to interact with Countries in the database.
 */
public class CountriesDAO {

    /**
     * Get country by ID by performing a SQL query statement to the database.
     *
     * @throws SQLException if an error occurs when accessing the database.
     * @return new if a country is found via the Country ID.
     * @return null if a country is not found via the Country ID.
     */
    public static Countries getCountryUsingCountryId(int countryID) throws SQLException {
        String string = "SELECT * FROM countries WHERE Country_ID = ?";
        PreparedStatement preparedstatement = JDBC.getConnection().prepareStatement(string);
        preparedstatement.setInt(1, countryID);
        ResultSet resultset = preparedstatement.executeQuery();
        if (resultset.next()) {
            String country_Name = resultset.getString("Country");
            return new Countries(countryID, country_Name);
        }
        return null;
    }

    /**
     * Get all countries by performing a SQL query to the database.
     *
     * @return clist all countries in database.
     */
    public static ObservableList<Countries> getEveryCountry() {
        ObservableList<Countries> clist = FXCollections.observableArrayList();

        try {
            String select = "SELECT * from countries";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(select);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Countries C = new Countries(countryID, countryName);
                clist.add(C);
            }
            // SQLException catch used in case an error occurs when accessing the database.
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return clist;
    }
}

