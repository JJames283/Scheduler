package Controller;

import DAO.ApptDAO;
import DAO.ContactsDAO;
import DAO.CustDAO;
import DAO.UserDAO;
import Helper.TimeConversion;
import Model.Appointments;
import Model.Contacts;
import Model.Customer;
import Model.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * ModifyAppointmentController class initialized. The ModifyAppointmentController modifies
 * Appointment data.
 */
public class ModifyAppointmentController implements Initializable {

    /**
     * Text field for Appointment ID.
     */
    @FXML
    private TextField modifyApptId;

    /**
     * Text field for Appointment Title.
     */
    @FXML
    private TextField modifyapptTitle;

    /**
     * Text field for Appointment Type.
     */
    @FXML
    private TextField modifyapptType;

    /**
     * Text field for Appointment Description.
     */
    @FXML
    private TextField modifyapptDesc;

    /**
     * Text field for Appointment Location.
     */
    @FXML
    private TextField modifyapptLoc;

    /**
     * Date picker for Appointment Start Date.
     */
    @FXML
    private DatePicker modifyapptSD;

    /**
     * Date Picker for Appointment End Date.
     */
    @FXML
    private DatePicker modifyapptED;

    /**
     * Combo box for Appointment Start Time.
     */
    @FXML
    private ComboBox<LocalTime> modifyapptST;

    /**
     * Combo box for Appointment End Time.
     */
    @FXML
    private ComboBox<LocalTime> modifyapptET;

    /**
     * Combo box for Customer ID.
     */
    @FXML
    private ComboBox<Customer> modifyapptCustId;

    /**
     * Combo box for User ID.
     */
    @FXML
    private ComboBox<Users> modifyapptUserId;

    /**
     * Combo box for Contact ID.
     */
    @FXML
    private ComboBox<Contacts> modifyapptContactId;

    /**
     * Button to cancel modifying an appointment.
     */
    @FXML
    private Button cancelmodifyApptbutton;

    /**
     * Button to save modified appointment.
     */
    @FXML
    private Button savemodifyApptbutton;

    /**
     * Button to save modified appointment.
     */
    @FXML
    private Button exitapplicationbtn;


    /**
     * If user selects the Save button, the modified Appointment is checked to ensure that all
     * fields are properly filled out, that it is within business hours, and that it does not overlap
     * with an already existing appointment by the same user, otherwise the user will receive an error
     * pop up message. If all fields are filled out correctly, and no errors occur when accessing the
     * database, then the modified Appointment will be saved and the user is returned to the main
     * Appointments scene.
     *
     * @param event user selects the Save button.
     * @throws IOException from FXMLLoader.
     * @throws SQLException if an error occurs when accessing the database.
     */
    @FXML
    void clicktosavemodifyAppointment(ActionEvent event) throws IOException, SQLException {
        try {
            // If a user leaves any of the fields or combo boxes in the modify appointment
            // scene blank, they will receive an error message.
            if (modifyapptTitle.getText().isEmpty() || modifyapptDesc.getText().isEmpty() || modifyapptLoc.getText().isEmpty() || modifyapptType.getText().isEmpty() || modifyapptSD.getValue() == null || modifyapptST.getValue() == null || modifyapptED.getValue() == null || modifyapptET.getValue() == null || modifyapptCustId.getValue() == null || modifyapptUserId.getValue() == null || modifyapptContactId.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Missing Information.");
                alert.setContentText("Please make sure all fields are properly\ncompleted before attempting to save.");
                alert.showAndWait();
                return;
            } else if (modifyapptET.getValue().isBefore(modifyapptST.getValue())) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Appointment not modified.");
                error.setContentText("Please ensure the appointment start time\nis before the appointment end time.");
                error.showAndWait();
                return;
            } else if (modifyapptED.getValue().isBefore(modifyapptSD.getValue())) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Appointment not modified.");
                error.setContentText("Please ensure the appointment start date\nis before the appointment end date.");
                error.showAndWait();
                return;
            } else if (modifyapptET.getValue().equals(modifyapptST.getValue())) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Appointment not modified.");
                error.setContentText("Please ensure the appointment start and\nend times are different.");
                error.showAndWait();
                return;
            }

            LocalDate startDatePicked = LocalDate.from(modifyapptSD.getValue().atStartOfDay());
            LocalDateTime start = LocalDateTime.of(startDatePicked, modifyapptST.getValue());

            LocalDate endDatePicked = LocalDate.from(modifyapptED.getValue().atStartOfDay());
            LocalDateTime end = LocalDateTime.of(endDatePicked, modifyapptET.getValue());

            boolean duringHours = ApptDAO.openHoursChecker(start, end);

            // If the appointment is outside of business hours (office hours), the user will receive an error
            // message, otherwise if no errors are received, the appointment is successfully modified.
            // NOTE: Due to appointment times being controlled via combo boxes, users are unable to
            // schedule appointments outside of business hours, therefore this error does not occur.
            if (end.isBefore(start) || start.isAfter(end)) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Appointment not modified.");
                error.setContentText("Appointment times must be within business hours.\nBusiness hours are currently 8AM-10PM ET.");
                error.showAndWait();
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Appointments saveAppt = getAppt();

        // Uses apptOverlap method via ApptDAO to check if appointment attempting to be modified
        // overlaps with an already existing appointment by the same user. If so, user will receive
        // an error message(note: error message in ApptDAO apptOverlap method).
        if (ApptDAO.apptOverlap(saveAppt))
            return;

        int appointmentId = Integer.parseInt(modifyApptId.getText());
        String appointmentTitle = modifyapptTitle.getText();
        String appointmentType = modifyapptType.getText();
        String appointmentDescription = modifyapptDesc.getText();
        String appointmentLocation = modifyapptLoc.getText();
        LocalDate appointmentStartDate = modifyapptSD.getValue();
        LocalDate appointmentEndDate = modifyapptED.getValue();
        LocalTime appointmentStartTime = modifyapptST.getValue();
        LocalTime appointmentEndTime = modifyapptET.getValue();
        LocalDateTime startDateTime = LocalDateTime.of(appointmentStartDate, appointmentStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(appointmentEndDate, appointmentEndTime);
        // Get values for the selected contact ID, user ID, and customer ID combo boxes.
        Customer cust = modifyapptCustId.getValue();
        Users user = modifyapptUserId.getValue();
        Contacts contact = modifyapptContactId.getValue();

        // Generate object "modifyappt" and save the modified appointment to the database by
        // using the Appointment DAO (ApptDAO).
        Appointments modifyappt = ApptDAO.getAppt(appointmentId);
        assert modifyappt != null;
        modifyappt.setTitle(appointmentTitle);
        modifyappt.setType(appointmentType);
        modifyappt.setDescription(appointmentDescription);
        modifyappt.setLocation(appointmentLocation);
        modifyappt.setStart(startDateTime);
        modifyappt.setEnd(endDateTime);
        modifyappt.setCustomerID(cust.getId());
        modifyappt.setUserID(user.getUserID());
        modifyappt.setContactID(contact.getContactID());
        try {
            // Use ApptDAO to modify existing appointment to the database.
            ApptDAO.modifyAppt(modifyappt);
            // SQLException catch in case there is an issue interacting with the database.
        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
        // If successfully modified/saved, user is brought back to the main Appointments scene.
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Appointments.fxml")));
        Scene scene = new Scene(root, 900, 564);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * getAppt() method is used above to modify existing appointment to database.
     *
     * @return new Appointments.
     * @throws SQLException if an error occurs when accessing the database.
     */
    private Appointments getAppt() throws SQLException {
        // 1 is added to the highest appointment ID identified in creation of a new appointment to get an unused ID.
        int appointment_Id = ApptDAO.getHighestApptID() + 1;
        String appointmentTitle = modifyapptTitle.getText();
        String appointmentType = modifyapptType.getText();
        String appointmentDescription = modifyapptDesc.getText();
        String appointmentLocation = modifyapptLoc.getText();
        LocalDate appointmentStartDate = modifyapptSD.getValue();
        LocalDate appointmentEndDate = modifyapptED.getValue();
        LocalTime appointmentStartTime = modifyapptST.getValue();
        LocalTime appointmentEndTime = modifyapptET.getValue();
        LocalDateTime appointmentStartDateTime = LocalDateTime.of(appointmentStartDate, appointmentStartTime);
        LocalDateTime appointmentEndDateTime = LocalDateTime.of(appointmentEndDate, appointmentEndTime);
        Customer cust = modifyapptCustId.getValue();
        Users user = modifyapptUserId.getValue();
        Contacts contact = modifyapptContactId.getValue();
        return new Appointments(appointment_Id, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, appointmentStartDateTime, appointmentEndDateTime, cust.getId(), user.getUserID(), contact.getContactID());
    }

    /**
     * If the user selects the cancel button, they are returned to the main Appointments scene
     * (from the Modify Appointment scene).
     *
     * @param event user selects the Cancel button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void clicktocancelmodifyAppointment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Appointments.fxml")));
        Scene scene = new Scene(root, 900, 564);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * User selects the Exit button and exits the application (from the Modify Appointment screen).
     * Please note, the user most confirm they wish to exit before they exit the application.
     *
     * @param event user selects the Exit button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void clicktoExitApplication(ActionEvent event) throws IOException {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Exit Application");
        confirmation.setContentText("Do you wish to exit the Scheduling Application?");
        Optional<ButtonType> result = confirmation.showAndWait();
        // The user must confirm they wish to exit the application by selecting "OK".
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * If user selects to modify an appointment, the existing appointment data is "sent" from
     * the Appointments scene, to the Modify Appointments scene.
     *
     * @param appts user selects an appointment and clicks the modify button.
     * @throws SQLException if an error occurs when accessing the database.
     */
    @FXML
    public void forwardAppt(Appointments appts) throws SQLException {
        modifyApptId.setText(String.valueOf(appts.getAppointmentID()));
        modifyapptTitle.setText(appts.getTitle());
        modifyapptType.setText(appts.getType());
        modifyapptDesc.setText(appts.getDescription());
        modifyapptLoc.setText(appts.getLocation());
        modifyapptSD.setValue(appts.getStart().toLocalDate());
        modifyapptED.setValue(appts.getEnd().toLocalDate());
        modifyapptST.setItems(TimeConversion.getOpenOfficeHours());
        modifyapptET.setItems(TimeConversion.getOpenOfficeHours());
        modifyapptST.setValue(appts.getStart().toLocalTime());
        modifyapptET.setValue(appts.getEnd().toLocalTime());
        modifyapptCustId.setItems(CustDAO.getEveryCust());
        //Get a Customer by their ID using the Customer DAO and database.
        Customer cust = CustDAO.getCustUsingCustID(appts.getCustomerID());
        modifyapptCustId.setValue(cust);
        modifyapptUserId.setItems(UserDAO.getEveryUser());
        //Get a User by their ID using the User DAO and database.
        Users user = UserDAO.getUserInfo(appts.getUserID());
        modifyapptUserId.setValue(user);
        modifyapptContactId.setItems(ContactsDAO.getEveryContact());
        //Get a Contact by their ID using the Contacts DAO and database.
        Contacts contact = ContactsDAO.getContactUsingContact_ID(appts.getContactID());
        modifyapptContactId.setValue(contact);
    }

    /**
     * Modify Appointment initialized. Modify Appointment ID text field set to disabled so that
     * users are not able to manually modify Appointment ID's.
     *
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifyApptId.setDisable(true);
    }
}
