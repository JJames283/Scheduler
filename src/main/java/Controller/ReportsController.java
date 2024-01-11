package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * The ReportsController is a navigation menu to allow the user to go to each of the three
 * reports.
 */
public class ReportsController {

    /**
     * Button to go to Appointments by Contact report.
     */
    @FXML
    private Button AppointmentsbyContact;

    /**
     * Button to go to Appointments by Type and Month report.
     */
    @FXML
    private Button AppointmentsbyTypeMonth;

    /**
     * Button to go to Customers per Country report.
     */
    @FXML
    private Button CustomersbyCountry;

    /**
     * Button to exit application.
     */
    @FXML
    private Button exitApplicationbutton;

    /**
     * Button to go to Customers screen.
     */
    @FXML
    private Button gotoCustomersbutton;

    /**
     * Button to go to Appointments screen.
     */
    @FXML
    private Button gotoAppointmentsbutton;

    /**
     * Button to go to Main Menu.
     */
    @FXML
    private Button gotoMainMenubutton;


    /**
     * If the user selects the Report button associated with the Appointments by Contact report,
     * they are brought to the Appointments by Contact scene.
     *
     * @param event user selects the Report button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    public void gotoAppointmentsbyContact(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/ReportByContact.fxml")));
        Scene scene = new Scene(root, 801, 501);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * If the user selects the Report button associated with the Appointments by Type and Month
     * report, they are brought to the Appointments by Type and Month Report scene.
     *
     * @param event user selects the Report button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    public void gotoAppointmentsbyTypeMonth(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/ReportApptTypeMonth.fxml")));
        Scene scene = new Scene(root, 640, 501);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * If the user selects the Report button associated with the Customers by Country report, they
     * are brought to the Customers by Country Report scene.
     *
     * @param event user selects the Report button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    public void gotoCutsomersbyCoutry(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/ReportCountryCustomer.fxml")));
        Scene scene = new Scene(root, 588, 501);
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
        Scene scene = new Scene(root, 900, 600);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * User selects the Exit button and exits the application (from the Reports screen).
     * Please note, the user most confirm they wish to exit before they exit the application.
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