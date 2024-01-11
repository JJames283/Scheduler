package Model;

import java.time.LocalDateTime;

/**
 * This class defines and manges Appointments.
 */
public class Appointments {
    private int apptID;
    private String appointmentTitle;
    private String appointmentType;
    private String appointmentDesc;
    private String appointmentLoc;
    private LocalDateTime appointmentStartDateTime;
    private LocalDateTime appointmentEndDateTime;
    public int cust_ID;
    public int user_ID;
    public int contact_ID;

    /**
     * Appointments constructor.
     */
    public Appointments(int appointmentID, String title, String description, String location, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID) {
        this.apptID = appointmentID;
        this.appointmentTitle = title;
        this.appointmentDesc = description;
        this.appointmentLoc = location;
        this.appointmentType = type;
        this.appointmentStartDateTime = start;
        this.appointmentEndDateTime = end;
        this.cust_ID = customerID;
        this.user_ID = userID;
        this.contact_ID = contactID;
    }

    /**
     * @return the appointment ID (apptID).
     */
    public int getAppointmentID() {
        return apptID;
    }

    /**
     * @return the appointment title.
     */
    public String getTitle() {
        return appointmentTitle;
    }

    /**
     * @return the appointment type.
     */
    public String getType() { return appointmentType; }

    /**
     * @return the appointment description.
     */
    public String getDescription() {
        return appointmentDesc;
    }

    /**
     * @return the appointment location.
     */
    public String getLocation() {
        return appointmentLoc;
    }

    /**
     * @return the appointment start.
     */
    public LocalDateTime getStart() {
        return appointmentStartDateTime;
    }

    /**
     * @return the appointment end.
     */
    public LocalDateTime getEnd() {
        return appointmentEndDateTime;
    }

    /**
     * @return the customer ID.
     */
    public int getCustomerID() {
        return cust_ID;
    }

    /**
     * @return the user ID.
     */
    public int getUserID() {
        return user_ID;
    }

    /**
     * @return the contact ID.
     */
    public int getContactID() {
        return contact_ID;
    }

    /**
     * @param customerID the customer ID to be set.
     */
    public void setCustomerID(int customerID) {
        this.cust_ID = customerID;
    }

    /**
     * @param title the appointment title to be set.
     */
    public void setTitle(String title) {
        this.appointmentTitle = title;
    }

    /**
     * @param type the appointment type to be set.
     */
    public void setType(String type) {
        this.appointmentType = type;
    }

    /**
     * @param description the appointment description to be set.
     */
    public void setDescription(String description) {
        this.appointmentDesc = description;
    }

    /**
     * @param location the appointment location to be set.
     */
    public void setLocation(String location) {
        this.appointmentLoc = location;
    }

    /**
     * @param start the appointment start to be set.
     */
    public void setStart(LocalDateTime start) {
        this.appointmentStartDateTime = start;
    }

    /**
     * @param end the appointment end to be set.
     */
    public void setEnd(LocalDateTime end) {
        this.appointmentEndDateTime = end;
    }

    /**
     * @param userID the user ID to be set.
     */
    public void setUserID(int userID) {
        this.user_ID = userID;
    }

    /**
     * @param contactID the contact ID to be set.
     */
    public void setContactID(int contactID) {
        this.contact_ID = contactID;
    }

}
