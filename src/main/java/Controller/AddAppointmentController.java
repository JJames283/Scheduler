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
import java.time.*;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AddAppointmentController class initialized. The AddAppointmentController adds Appointment data.
 */
public class AddAppointmentController implements Initializable {

    /**
     * Text field for Appointment ID.
     */
    @FXML
    private TextField addApptIdtxtfield;

    /**
     * Text field for Appointment Title.
     */
    @FXML
    private TextField addapptTitle;

    /**
     * Text field for Appointment Type.
     */
    @FXML
    private TextField addapptType;

    /**
     * Text field for Appointment Description.
     */
    @FXML
    private TextField addapptDesc;

    /**
     * Text field for Appointment Location.
     */
    @FXML
    private TextField addapptLoc;

    /**
     * Date Picker for Appointment Start Date.
     */
    @FXML
    private DatePicker addapptSD;

    /**
     * Date Picker for Appointment End Date.
     */
    @FXML
    private DatePicker addapptED;

    /**
     * Combo box for Appointment Start Time.
     */
    @FXML
    private ComboBox<LocalTime> addapptST;

    /**
     * Combo box for Appointment End Time.
     */
    @FXML
    private ComboBox<LocalTime> addapptET;

    /**
     * Combo box for Customer ID.
     */
    @FXML
    private ComboBox<Customer> addapptCustId;

    /**
     * Combo box for User ID.
     */
    @FXML
    private ComboBox<Users> addapptUserId;

    /**
     * Combo box for Contact ID.
     */
    @FXML
    private ComboBox<Contacts> addapptContactId;

    /**
     * Button to save Appointment.
     */
    @FXML
    private Button saveaddApptbutton;

    /**
     * Button to cancel adding an Appointment.
     */
    @FXML
    private Button canceladdApptbutton;

    /**
     * Button to exit application from Add Appointment scene.
     */
    @FXML
    private Button exitApplicationBtn;


    /**
     * If user selects the Save button, the appointment is checked to ensure that all fields are
     * properly filled out, otherwise the user will receive an error pop up message. If all fields
     * are filled out correctly, and no errors occur when accessing the database, then the
     * appointment will be saved and the user is returned to the main Appointments scene.
     *
     * @param appts user selects the save button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void clicktosaveaddAppointment(ActionEvent appts) throws IOException, SQLException {
        try {
            // If a user leaves any of the fields or combo boxes in the add appointment scene
            // blank, they will receive an error message.
            if (addapptTitle.getText().isEmpty() || addapptDesc.getText().isEmpty() || addapptLoc.getText().isEmpty() || addapptType.getText().isEmpty() || addapptSD.getValue() == null || addapptST.getValue() == null || addapptED.getValue() == null || addapptET.getValue() == null || addapptCustId.getValue() == null || addapptUserId.getValue() == null || addapptContactId.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!");
                alert.setHeaderText("Missing Information.");
                alert.setContentText("Please make sure all fields are properly\ncompleted before attempting to save.");
                alert.showAndWait();
                return;
            } else if (addapptET.getValue().isBefore(addapptST.getValue())) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Appointment not added.");
                error.setContentText("Please ensure the appointment start time is before the appointment end time.");
                error.showAndWait();
                return;
            } else if (addapptED.getValue().isBefore(addapptSD.getValue())) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Appointment not added.");
                error.setContentText("Please ensure the appointment start date is before the appointment end date.");
                error.showAndWait();
                return;
            } else if (addapptET.getValue().equals(addapptST.getValue())) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Appointment not added.");
                error.setContentText("Please ensure the appointment start and end times are different.");
                error.showAndWait();
                return;
            }

            LocalDate startDatePicked = LocalDate.from(addapptSD.getValue().atStartOfDay());
            LocalDateTime start = LocalDateTime.of(startDatePicked, addapptST.getValue());

            LocalDate endDatePicked = LocalDate.from(addapptED.getValue().atStartOfDay());
            LocalDateTime end = LocalDateTime.of(endDatePicked, addapptET.getValue());

            // If the appointment is outside of business hours (office hours), the user will receive an error
            // message, otherwise if no errors are received, the appointment is successfully added.
            // NOTE: Due to appointment times being controlled via combo boxes, users are unable to
            // schedule appointments outside of business hours, therefore this error does not occur.
            if (end.isBefore(start) || start.isAfter(end)) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error!");
                error.setHeaderText("Appointment not added.");
                error.setContentText("Appointment times must be within business hours.\nBusiness hours are currently 8AM-10PM ET.");
                error.showAndWait();
                return;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Appointments saveAppt = getAppt();
        if (ApptDAO.apptOverlap(saveAppt))
            return;

        try {
            // Use ApptDAO to add/save appointment to the database.
            ApptDAO.addAppt(saveAppt);
            // SQLException catch if an error occurs when accessing the database.
        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
        // If successfully saved, user is brought back to the main Appointments scene.
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Appointments.fxml")));
        Scene scene = new Scene(root, 900, 564);
        Stage window = (Stage) ((Node) appts.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * getAppt() method is used above to save the appointment to database.
     *
     * @return new Appointment.
     * @throws SQLException if an error occurs when accessing the database.
     */
    private Appointments getAppt() throws SQLException {
        // 1 is added to the highest appointment ID identified in creation of a new appointment to get an unused ID.
        int appointment_Id = ApptDAO.getHighestApptID() + 1;
        String appointmentTitle = addapptTitle.getText();
        String appointmentType = addapptType.getText();
        String appointmentDescription = addapptDesc.getText();
        String appointmentLocation = addapptLoc.getText();
        LocalDate appointmentStartDate = addapptSD.getValue();
        LocalDate appointmentEndDate = addapptED.getValue();
        LocalTime appointmentStartTime = addapptST.getValue();
        LocalTime appointmentEndTime = addapptET.getValue();
        LocalDateTime appointmentStartDateTime = LocalDateTime.of(appointmentStartDate, appointmentStartTime);
        LocalDateTime appointmentEndDateTime = LocalDateTime.of(appointmentEndDate, appointmentEndTime);
        Customer cust = addapptCustId.getValue();
        Users user = addapptUserId.getValue();
        Contacts contact = addapptContactId.getValue();
        return new Appointments(appointment_Id, appointmentTitle, appointmentDescription, appointmentLocation, appointmentType, appointmentStartDateTime, appointmentEndDateTime, cust.getId(), user.getUserID(), contact.getContactID());
    }

    /**
     * User selects the Exit button and exits the application (from the Add Appointments scene).
     * Please note, the user most confirm they wish to exit before they exit the application.
     *
     * @param event user selects the Exit button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void clicktoExitApplication(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Application");
        alert.setContentText("Do you wish to exit the Scheduling Application?");
        Optional<ButtonType> result = alert.showAndWait();
        // The user must confirm they wish to exit the application by selecting "OK".
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * If the user selects the cancel button, they are returned to the main Appointments scene
     * (from the Add Appointments scene).
     *
     * @param event user selects the cancel button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void clicktocanceladdAppointment(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Appointments.fxml")));
        Scene scene = new Scene(root, 900, 564);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * AddAppointmentController initialized. Appointment ID text field disabled so user is unable to
     * manually update it, rather it is automatically generated by adding 1 to the maximum
     * (highest) already existing Appointment ID. Combo box fields (Contact ID, Start/End Time,
     * Customer ID, User ID) are also filled.
     *
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addApptIdtxtfield.setDisable(true);
        try {
            addApptIdtxtfield.setText(String.valueOf(ApptDAO.getHighestApptID() + 1));
            // SQLException catch if an error occurs when accessing the database.
        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
        // Add Appointment scene start time, end time, customer ID, user ID, and contact ID
        // items are set upon initialization.
        addapptST.setItems(TimeConversion.getOpenOfficeHours());
        addapptET.setItems(TimeConversion.getOpenOfficeHours());
        addapptCustId.setItems(CustDAO.getEveryCust());
        addapptUserId.setItems(UserDAO.getEveryUser());
        addapptContactId.setItems(ContactsDAO.getEveryContact());
    }
}
