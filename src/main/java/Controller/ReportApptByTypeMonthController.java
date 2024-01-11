package Controller;

import DAO.ApptDAO;
import Helper.MonthReview;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * ReportApptByTypeMonthController class displays the Appointments by Type and Month report.
 */
public class ReportApptByTypeMonthController {

    /**
     * Table view for Appointments by Type and Month.
     */
    @FXML
    private TableView<MonthReview> apptsbyType;

    /**
     * Table column for Appointment Month.
     */
    @FXML
    private TableColumn<MonthReview, String> AppointmentMonthreportstablecolumn;

    /**
     * Table column for Appointment Type.
     */
    @FXML
    private TableColumn<MonthReview, String> AppointmentTypereportstablecolumn;

    /**
     * Table column for Appointment Total.
     */
    @FXML
    private TableColumn<MonthReview, Integer> AppointmentMonthTypeTotaltablecolumn;

    /**
     * Button to go to Reports.
     */
    @FXML
    private Button gotoReportsbutton;

    /**
     * Button to exit application.
     */
    @FXML
    private Button exitApplicationbutton;

    /**
     * Button to go to Customers.
     */
    @FXML
    private Button gotoCustomersbutton;

    /**
     * Button to go to Appointments.
     */
    @FXML
    private Button gotoAppointmentsbutton;

    /**
     * Button to go to the Main Menu.
     */
    @FXML
    private Button gotoMainMenubutton;


    /**
     * List of Appointments by Month and Type is generated, and is used to populate the table
     * view.
     *
     * @throws SQLException if an error occurs when accessing the database.
     */
    public void initialize() throws SQLException {
        try {
            ApptDAO apptsDAO = new ApptDAO();
            // Using apptsDAO, the database is accessed and a list of month appointment data is retrieved.
            List<MonthReview> appointmentMonthReview = apptsDAO.getApptsMonthReview();

            // Cell value factory is set for each column in the Appointment by Month and Type report.
            AppointmentMonthreportstablecolumn.setCellValueFactory(new PropertyValueFactory<>("month"));
            AppointmentTypereportstablecolumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
            AppointmentMonthTypeTotaltablecolumn.setCellValueFactory(new PropertyValueFactory<>("total"));

            // Fill the table view with the list of month appointment data that was
            // retrieved from the database.
            apptsbyType.getItems().setAll(appointmentMonthReview);

            // SQLException catch in case there is an issue interacting with the database.
        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
    }

    /**
     * User selects the Reports button and are brought to the Reports scene.
     *
     * @param event user selects the Reports button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    public void backtoReportsMenu(ActionEvent event) throws IOException {
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
     * User selects the Exit button and exits the application (from the Appointments by Type and
     * Month screen). Please note, the user most confirm they wish to exit before they exit the
     * application.
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