package DAO;

import Helper.JDBC;
import Model.FirstLevelDivisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * FirstLevelDivisionsDAO class manages the methods used to interact with Divisions in the database.
 */
public class FirstLevelDivisionsDAO {

    /**
     * Get first level divisions by Division ID by performing a SQL query statement to the database.
     *
     * @throws SQLException if an error occurs when accessing the database.
     * @return firstleveldivision first level divisions.
     */
    public static FirstLevelDivisions getFirstLevelDivUsingID(int id) throws SQLException {
        FirstLevelDivisions firstleveldivision = null;
        String select = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement statement = JDBC.getConnection().prepareStatement(select);
        statement.setInt(1, id);
        ResultSet resultset = statement.executeQuery();
        if (resultset.next()) {
            int div_ID = resultset.getInt("Division_ID");
            String div_Name = resultset.getString("Division");
            int divisionCountry_ID = resultset.getInt("COUNTRY_ID");

            firstleveldivision = new FirstLevelDivisions(div_ID, div_Name, divisionCountry_ID);
        }
        return firstleveldivision;
    }

    /**
     * Get first level divisions by Country ID by performing a SQL query statement to the database.
     *
     * @return firstleveldivisions first level divisions.
     */
    public static ObservableList<FirstLevelDivisions> FirstLevelDivUsingCountry_ID(int countryId) {
        ObservableList<FirstLevelDivisions> firstLevelDiv = FXCollections.observableArrayList();
        try {
            String select = "SELECT Division_ID, Division, Country_ID FROM first_level_divisions WHERE Country_ID = ?";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(select);
            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int division_Id = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int divisionCountry_Id = rs.getInt("Country_ID");
                firstLevelDiv.add(new FirstLevelDivisions(division_Id, divisionName, divisionCountry_Id));
            }
            // SQLException catch used in case an error occurs when accessing the database.
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return firstLevelDiv;
    }
}

