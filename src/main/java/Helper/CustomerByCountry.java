package Helper;

/**
 * This class defines and manges Customer By Country.
 */
public class CustomerByCountry {
    private final String eachCountry;
    private final int custCountPerCountry;

    /**
     * CustomerByCountry constructor.
     */
    public CustomerByCountry(String country, int customerCount) {
        this.eachCountry = country;
        this.custCountPerCountry = customerCount;
    }

    /**
     * @return eachCountry (country).
     */
    public String getCountry() {
        return eachCountry;
    }

    /**
     * @return custCountPerCountry (the number of customers per country).
     */
    public int getCustomerCount() {
        return custCountPerCountry;
    }
}

