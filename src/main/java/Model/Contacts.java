package Model;

/**
 * This class defines and manges Contacts.
 */
public class Contacts {
    int contact_Id;
    String contact_Name;
    String contact_Email;

    /**
     * Appointments constructor.
     */
    public Contacts(int contactID, String contactName, String contactEmail) {
        this.contact_Id = contactID;
        this.contact_Name = contactName;
        this.contact_Email = contactEmail;
    }

    /**
     * @return the contact_Name.
     */
    @Override
    public String toString() {
        return contact_Name;
    }

    /**
     * @return the contact_Id.
     */
    public int getContactID() {
        return contact_Id;
    }
}
