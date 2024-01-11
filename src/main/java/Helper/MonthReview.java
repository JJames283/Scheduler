package Helper;

/**
 *  This class defines and manges the Month Review.
 */
public class MonthReview {
    private final String appointmentMonth;
    private final String appointmentType;
    private final int totalnumberAppts;

    /**
     * MonthReview constructor.
     */
    public MonthReview(String month, String appointmentType, int total) {
        this.appointmentMonth = month;
        this.appointmentType = appointmentType;
        this.totalnumberAppts = total;
    }

    /**
     * @return month.
     */
    public String getMonth() {
        return appointmentMonth;
    }

    /**
     * @return appointment type.
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * @return total number of appointments.
     */
    public int getTotal() {
        return totalnumberAppts;
    }
}

