package Model;

/**
 * This class defines and manges Countries.
 */
public class Countries {
    int country_Id;
    String country_Name;

    /**
     * Countries constructor.
     */
    public Countries(int countryID, String countryName) {
        this.country_Id = countryID;
        this.country_Name = countryName;
    }

    /**
     * @return country_Id.
     */
    public int getCountryID() {
        return country_Id;
    }

    /**
     * @return country_Name.
     */
    @Override
    public String toString() {
        return country_Name;
    }
}

