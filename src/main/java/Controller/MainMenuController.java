package Controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * MainMenuController class initialized. The MainMenuController is a navigation menu to
 * go to Appointments, Customers, Reports, return to the Login screen, or exit the application.
 */
public class MainMenuController implements Initializable {

    /**
     * Label to show "Main Menu".
     */
    @FXML
    private Label mainmenuLabel;

    /**
     * Button to go from Main Menu to Customers.
     */
    @FXML
    private Button mainmenuGoToCustomersbtn;

    /**
     * Button to go from Main Menu to Reports.
     */
    @FXML
    private Button mainmenuGoToReportsbtn;

    /**
     * Button to go from Main Menu to Appointments.
     */
    @FXML
    private Button mainmenuGoToAppointmentsbtn;

    /**
     * Button to exit application.
     */
    @FXML
    private Button mainmenuExitProgrambtn;

    /**
     * Button to go back to Login screen.
     */
    @FXML
    private Button mainmenuGoToLoginbtn;


    /**
     * User selects the Customers button and are brought to the Customers scene.
     *
     * @param event user selects Customers button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void gotoCustomers(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Customer.fxml")));
        Scene scene = new Scene(root, 900, 564);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * User selects the Appointments button and are brought to the Appointments scene.
     *
     * @param event user selects Appointments button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void gotoAppointments(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Appointments.fxml")));
        Scene scene = new Scene(root, 900, 600);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * User selects the Reports button and are brought to the Reports scene.
     *
     * @param event user selects the Reports button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void gotoReports(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Reports.fxml")));
        Scene scene = new Scene(root, 492, 332);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * User selects the Login button and are brought back to the Login scene.
     *
     * @param event user selects the Login button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void clickGoToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Login.fxml")));
        Scene scene = new Scene(root, 426, 400);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }


    /**
     * User selects the Exit button and exits the application (from the Main Menu screen).
     * Please note, the user most confirm they wish to exit before they exit the application.
     *
     * @param event user selects the Exit button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void exitProgram(ActionEvent event) throws IOException {
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
     * Main Menu initialized.
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}