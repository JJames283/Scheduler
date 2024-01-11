package Model;

import DAO.CountriesDAO;

import java.sql.SQLException;

/**
 * This class defines and manges First Level Divisions.
 */
public class FirstLevelDivisions {
    int firstleveldiv_Id;
    String firstleveldivName;
    int firstleveldivCountry_Id;

    /**
     * First Level Divisions constructor.
     */
    public FirstLevelDivisions(int divisionID, String divisionName, int countryID) {
        this.firstleveldiv_Id = divisionID;
        this.firstleveldivName = divisionName;
        this.firstleveldivCountry_Id = countryID;
    }

    /**
     * @return the first level division_Id.
     */
    public int getDivisionID() {
        return firstleveldiv_Id;
    }

    /**
     * @return the first level division Name.
     */
    public String getDivisionName() {
        return firstleveldivName;
    }

    /**
     * @return the first level division Country_Id.
     */
    public int getCountryID() {
        return firstleveldivCountry_Id;
    }

    /**
     * @return the first level division Name.
     */
    @Override
    public String toString() {
        return firstleveldivName;
    }

    /**
     * Using the Countries DAO, get country by ID from the database.
     *
     * @throws SQLException if an error occurs when accessing the database.
     * @return country by ID.
     */
    public Countries getCountry() throws SQLException {
        return CountriesDAO.getCountryUsingCountryId(this.getCountryID());
    }
}

