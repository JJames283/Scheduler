package Model;

import DAO.CountriesDAO;
import DAO.FirstLevelDivisionsDAO;

import java.sql.SQLException;

/**
 * This class defines and manges Customers.
 */
public class Customer {
    int customer_Id;
    String customerName;
    String customerAddress;
    String customerPostal;
    String customerPhone;
    int customerDiv_Id;

    /**
     * Customer constructor.
     */
    public Customer(int id, String name, String address, String postalCode, String phone, int divisionID, FirstLevelDivisions division) {
        this.customer_Id = id;
        this.customerName = name;
        this.customerAddress = address;
        this.customerPhone = phone;
        this.customerDiv_Id = divisionID;
        this.customerPostal = postalCode;
    }

    /**
     * @return the customer_Id.
     */
    public int getId() {
        return customer_Id;
    }

    /**
     * @return the customerName.
     */
    public String getName() {
        return customerName;
    }

    /**
     * @return the customerAddress.
     */
    public String getAddress() {
        return customerAddress;
    }

    /**
     * @return the customerPhone.
     */
    public String getPhone() {
        return customerPhone;
    }

    /**
     * @return the customerDivision_Id.
     */
    public int getDivisionID() {
        return customerDiv_Id;
    }

    /**
     * @return the customerPostalCode.
     */
    public String getPostalCode() {
        return customerPostal;
    }

    /**
     * @param id the customer ID to be set.
     */
    public void setId(int id) { this.customer_Id = id;
    }

    /**
     * @param name the customer Name to be set.
     */
    public void setName(String name) {
        this.customerName = name;
    }

    /**
     * @param address the customer address to be set.
     */
    public void setAddress(String address) {
        this.customerAddress = address;
    }

    /**
     * @param phone the customer phone to be set.
     */
    public void setPhone(String phone) {
        this.customerPhone = phone;
    }

    /**
     * @param div_ID the customer division ID to be set.
     */
    public void setDivisionID(int div_ID) {
        this.customerDiv_Id = div_ID;
    }

    /**
     * @param postal the customer postal code to be set.
     */
    public void setPostalCode(String postal) {
        this.customerPostal = postal;
    }

    /**
     * @return the customer_Id.
     */
    @Override
    public String toString() {
        return String.valueOf(customer_Id);
    }

    /**
     * Using the Countries DAO, get country by ID.
     *
     * @throws SQLException if an error occurs when accessing the database.
     * @return country by ID.
     */
    public Countries getCountry() throws SQLException {
        FirstLevelDivisions countryDiv = this.getDivision();
        return CountriesDAO.getCountryUsingCountryId(countryDiv.getCountryID());
    }

    /**
     * Using the Divisions DAO, get first level division by ID.
     *
     * @throws SQLException if an error occurs when accessing the database.
     * @return first level division by ID.
     */
    public FirstLevelDivisions getDivision() throws SQLException {
        return FirstLevelDivisionsDAO.getFirstLevelDivUsingID(this.customerDiv_Id);
    }
}
