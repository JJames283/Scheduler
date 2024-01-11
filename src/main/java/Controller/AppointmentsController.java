package Controller;

import DAO.ApptDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AppointmentsController class initialized. The AppointmentsController controls the
 * functionality and features of the Appointments scene.
 */
public class AppointmentsController implements Initializable {

    /**
     * Allows for a selected appointment to be modified, deleted, etc.
     */
    private static Model.Appointments selectAppt;

    /**
     * Table view for Appointment Table.
     */
    @FXML
    private TableView<Model.Appointments> apptTable;

    /**
     * Table column for Appointment ID.
     */
    @FXML
    private TableColumn<Model.Appointments, Integer> appointmentIdcolumn;

    /**
     * Table column for Appointment Title.
     */
    @FXML
    private TableColumn<Model.Appointments, String> appointmentTitlecolumn;

    /**
     * Table column for Appointment Type.
     */
    @FXML
    private TableColumn<Model.Appointments, String> appointmentTypecolumn;

    /**
     * Table column for Appointment Description.
     */
    @FXML
    private TableColumn<Model.Appointments, String> appointmentDesccolumn;

    /**
     * Table column for Appointment Location.
     */
    @FXML
    private TableColumn<Model.Appointments, String> appointmentLoccolumn;

    /**
     * Table column for Appointment Start (date/time).
     */
    @FXML
    private TableColumn<Model.Appointments, LocalDateTime> appointmentStartcolumn;

    /**
     * Table column for Appointment End (date/time).
     */
    @FXML
    private TableColumn<Model.Appointments, LocalDateTime> appointmentEndcolumn;

    /**
     * Table column for Customer ID.
     */
    @FXML
    private TableColumn<Model.Appointments, Integer> appointmentCustomerIdcolumn;

    /**
     * Table column for User ID.
     */
    @FXML
    private TableColumn<AppointmentsController, Integer> appointmentUserIdcolumn;

    /**
     * Table column for Contact ID.
     */
    @FXML
    private TableColumn<Model.Appointments, String> appointmentContactIdcolumn;

    /**
     * Radio button to view appointments by week.
     */
    @FXML
    private RadioButton appointmentsViewThisWeekradiobtn;

    /**
     * Radio button to view all appointments by month.
     */
    @FXML
    private RadioButton appointmentsViewThisMonthradiobtn;

    /**
     * Radio button to view all appointments.
     */
    @FXML
    private RadioButton appointmentsViewAllradiobtn;

    /**
     * Button to add a new Appointment.
     */
    @FXML
    private Button addnewAppointmentbutton;

    /**
     * Button to delete an existing Appointment.
     */
    @FXML
    private Button deleteAppointmentbutton;

    /**
     * Button to modify an existing Appointment.
     */
    @FXML
    private Button modifyAppointmentbutton;

    /**
     * Button to return user to the Main Menu.
     */
    @FXML
    private Button clickMainMenubutton;

    /**
     * Button to allow user to go directly to the Customers scene (from Appointments).
     */
    @FXML
    private Button clickCustomersbutton;

    /**
     * Button to allow user to go directly to Reports scene (from Appointments).
     */
    @FXML
    private Button clickReportsbutton;

    /**
     * Button used to exit application.
     */
    @FXML
    private Button exitAppButton;


    /**
     * All existing appointments are loaded to the appointment table.
     *
     * @param load user accesses Appointments scene.
     */
    private void apptsLoader(String load) {
        ApptDAO dao = new ApptDAO();
        apptTable.setItems(ApptDAO.getEveryAppt(load));
    }

    /**
     * Displays all appointments if user selects View All radio button.
     *
     * @param event user selects the save button.
     */
    @FXML
    void appointmentsViewAll(ActionEvent event) {
        apptsLoader("");
    }

    /**
     * Displays appointments this week if user selects View This Week radio button.
     *
     * @param event user selects the save button.
     */
    @FXML
    void appointmentsViewThisWeek(ActionEvent event) {
        LocalDateTime now = LocalDateTime.now();
        String thisWeek = "WEEK(start) = WEEK('" + now + "')";
        apptsLoader(thisWeek);
    }

    /**
     * Displays appointments this month if user selects View This Month radio button.
     *
     * @param event user selects the save button.
     */
    @FXML
    void appointmentsViewThisMonth(ActionEvent event) {
        int month = LocalDateTime.now().getMonthValue();
        String thisMonth = "MONTH(start) = " + month;
        apptsLoader(thisMonth);
    }

    /**
     * If user selects the Add button, they are brought ot the Add Appointments scene to
     * add an Appointment.
     *
     * @param event user selects the Add button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void clickandgotoAddAppointmentscene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/AddAppointment.fxml")));
        Scene scene = new Scene(root, 338, 507);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * If user selects the Delete button, they must first confirm they wish to delete the
     * appointment. If they confirm "OK", and there are no errors when accessing the database,
     * the appointment is deleted. If the user selects the Delete button without first selecting
     * an existing Appointment, they will receive an error message.
     *
     * @param event user selects the Delete button.
     * @throws SQLException if an error occurs when accessing the database.
     */
    @FXML
    void clickdeleteAppointmentbutton(ActionEvent event) throws SQLException {
        selectAppt = apptTable.getSelectionModel().getSelectedItem();
        // If user selects the delete button without selecting an appointment to delete,
        // they will receive an error message.
        if (selectAppt == null) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error!");
            error.setContentText("You must choose an appointment to delete.");
            error.showAndWait();
        } else {
            // The user must confirm they wish to delete the appointment by selecting "OK".
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setContentText("Do you wish to delete this appointment?");
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ApptDAO.deleteAppt(selectAppt.getAppointmentID());
                appointmentsViewAllradiobtn.setSelected(true);
                apptTable.setItems(ApptDAO.getEveryAppt(""));
            }
        }
    }

    /**
     * If user selects the Modify button, they are brought ot the Modify Appointment scene to
     * modify an already existing Appointment. However, if the user selects the Modify button without
     * first selecting an existing Appointment, they will receive an error message.
     *
     * @param event user selects the Modify button.
     * @throws IOException from FXMLLoader.
     * @throws SQLException if an error occurs when accessing the database.
     */
    @FXML
    void clickandgotoModifyAppointment(ActionEvent event) throws IOException, SQLException {
        selectAppt = apptTable.getSelectionModel().getSelectedItem();
        if (selectAppt == null) {
            // If user selects the modify button without selecting an appointment to modify,
            // they will receive an error message.
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error!");
            error.setContentText("You must choose an appointment to modify.");
            error.showAndWait();
        } else {
            // User is brought to the Modify Appointment scene.
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/View/modifyAppointment.fxml"));
            fxmlLoader.load();

            ModifyAppointmentController ma = fxmlLoader.getController();
            ma.forwardAppt(apptTable.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * If the user selects the Main Menu button, they are brought to the Main Menu (from Appointments).
     *
     * @param event user selects the Main Menu button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    public void gotomainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/MainMenu.fxml")));
        Scene scene = new Scene(root, 250, 225);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * If the user selects the Customers button, they are brought to the Customers scene
     * (from Appointments).
     *
     * @param event user selects the Customers button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    public void gotoCustomers(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Customer.fxml")));
        Scene scene = new Scene(root, 900, 564);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * User selects Reports button and goes to the Reports scene (from Appointments).
     *
     * @param event user selects the Reports button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    public void gotoReports(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Reports.fxml")));
        Scene scene = new Scene(root, 492, 332);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * User selects the Exit button and exits the application (from the Appointments scene).
     * Please note, the user most confirm they wish to exit before they exit the application.
     *
     * @param event user selects the Exit button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void clicktoExitApp(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setContentText("Do you wish to exit the Schedule Application?");
        Optional<ButtonType> result = alert.showAndWait();
        // The user must confirm they wish to exit the application by selecting "OK".
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Appointments initialized and appointments table loaded with appointments.
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentsViewAllradiobtn.setSelected(true);
        appointmentIdcolumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitlecolumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentDesccolumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLoccolumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentTypecolumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStartcolumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEndcolumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentCustomerIdcolumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appointmentUserIdcolumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        appointmentContactIdcolumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        apptsLoader("");
    }
}
