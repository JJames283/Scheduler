package Controller;

import DAO.ContactsDAO;
import Model.Appointments;
import Model.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import static DAO.ApptDAO.getCustAppts;

/**
 * ReportApptByContactController class displays the Appointments by Contact report.
 */
public class ReportApptByContactController {

    /**
     * Tabble view for Appointments by Contact.
     */
    @FXML
    private TableView<Appointments> apptsbyContact;

    /**
     * Table column for Report ID.
     */
    @FXML
    private TableColumn<Appointments, Integer> reportsIDtablecolumn;

    /**
     * Table column for Report Title.
     */
    @FXML
    private TableColumn<Appointments, String> reportsTitletablecolumn;

    /**
     * Table column for Report Type.
     */
    @FXML
    private TableColumn<Appointments, String> reportsTypetablecolumn;

    /**
     * Table column for Report Description.
     */
    @FXML
    private TableColumn<Appointments, String> reportsDesctablecolumn;

    /**
     * Table column for Report Location.
     */
    @FXML
    private TableColumn<Appointments, String> reportsLoctablecolumn;

    /**
     * Table column for Report Start Date and Time.
     */
    @FXML
    private TableColumn<Appointments, LocalDateTime> reportsStartDateTimetablecolumn;

    /**
     * Table column for Report End Date and Time.
     */
    @FXML
    private TableColumn<Appointments, LocalDateTime> reportsEndDateTimetablecolumn;

    /**
     * Table column for Customer ID.
     */
    @FXML
    private TableColumn<Appointments, Integer> reportsCustomerIDtablecolumn;

    /**
     * Combo box for Contacts.
     */
    @FXML
    private ComboBox<Contacts> reportsContactcombobox;

    /**
     * Button to return back to Reports screen.
     */
    @FXML
    private Button backtoReportsbutton;

    /**
     * Button to exit application.
     */
    @FXML
    private Button exitApplicationbutton;

    /**
     * Button to go directly to Customers screen.
     */
    @FXML
    private Button gotoCustomersbutton;

    /**
     * Button to go directly to Appointments screen.
     */
    @FXML
    private Button gotoAppointmentsbutton;

    /**
     * Button to go directly to Main Menu.
     */
    @FXML
    private Button gotoMainMenubutton;


    /**
     * Set up and fill a table view with Appointments by Contact data. Combo box is filled with each
     * contact, so user is able to select different contacts to view their appointments.
     *
     * @throws SQLException if an error occurs when accessing the database.
     */
    public void initialize() throws SQLException {

        // Fill in contact combo box with all contacts in the database.
        ContactsDAO dao = new ContactsDAO();
        ObservableList<Contacts> allContactscombobox = FXCollections.observableArrayList(ContactsDAO.getEveryContact());
        reportsContactcombobox.setItems(allContactscombobox);

        ObservableList<Appointments> apptsList = FXCollections.observableArrayList();

        // Based on the contact selected by the user, the contact's appointments are loaded.
        if (!reportsContactcombobox.getItems().isEmpty()) {
            reportsContactcombobox.setValue(reportsContactcombobox.getItems().get(0));
            Contacts chosenContact = reportsContactcombobox.getValue();
            int contactID = chosenContact.getContactID();
            ObservableList<Appointments> appts = getCustAppts(contactID);
            apptsList.setAll(appts);
        }

        // The cell value factory is set for each Appointment by Contact column.
        reportsIDtablecolumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        reportsTitletablecolumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        reportsTypetablecolumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        reportsDesctablecolumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        reportsLoctablecolumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        reportsStartDateTimetablecolumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        reportsEndDateTimetablecolumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        reportsCustomerIDtablecolumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        // The appointment table view is filled with the contact's appointments.
        apptsbyContact.setItems(apptsList);

        // Event handler is added so that if user changes contact in contact combo box, the appointment
        // table view is updated with the chosen contact's appointments.
        reportsContactcombobox.setOnAction(event -> {
            Contacts chosenContact = reportsContactcombobox.getValue();
            int contactID = chosenContact.getContactID();
            ObservableList<Appointments> appt = getCustAppts(contactID);
            apptsList.setAll(appt);
            apptsbyContact.setItems(apptsList);
        });

    }

    /**
     * User selects the Reports button and are brought to the Reports scene.
     *
     * @param event user selects the Reports button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    public void gobacktoReportsMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Reports.fxml")));
        Scene scene = new Scene(root, 492, 332);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * User selects the Main Menu button and are brought back to the Main Menu scene.
     *
     * @param event user selects the Main Menu button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    public void clickandgotoMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/MainMenu.fxml")));
        Scene scene = new Scene(root, 250, 225);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * User selects the Customers button and are brought to the Customers scene.
     *
     * @param event user selects the Customers button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    public void clickandgotoCustomers(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Customer.fxml")));
        Scene scene = new Scene(root, 900, 564);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * User selects the Appointments button and are brought to the Appointments scene.
     *
     * @param event user selects the Appointments button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    public void clickandgotoAppointments(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Appointments.fxml")));
        Scene scene = new Scene(root, 801, 501);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * User selects the Exit button and exits the application (from the Appointments by Contact
     * screen). Please note, the user most confirm they wish to exit before they exit the application.
     *
     * @param event user selects the Exit button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void clicktoExitApplication(ActionEvent event) throws IOException {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setContentText("Do you wish to exit the Schedule Application?");
        Optional<ButtonType> result = confirmation.showAndWait();
        // The user must confirm they wish to exit the application by selecting "OK".
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}