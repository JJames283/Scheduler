package Controller;

import DAO.CustDAO;
import Helper.CustomerByCountry;
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
 * ReportCustomerByCountryController class displays the Customers by Country report.
 */
public class ReportCustomerByCountryController {

    /**
     * Table view for Customers per Country.
     */
    @FXML
    private TableView<CustomerByCountry> customersperCountry;

    /**
     * Table column for Country Name.
     */
    @FXML
    private TableColumn<CustomerByCountry, String> CountryNameReportsColumn;

    /**
     * Table column for Number of Customers.
     */
    @FXML
    private TableColumn<CustomerByCountry, Integer> NumberofCustomersReportsColumn;

    /**
     * Button to go directly to Reports screen.
     */
    @FXML
    private Button gotoReportsbutton;

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
     * List of Customers by Country is generated, and is used to populate the table
     * view.
     *
     * @throws SQLException if an error occurs when accessing the database.
     */
    public void initialize() throws SQLException {

        CustDAO dao = new CustDAO();
        // List of Customer per Country data is retrieved from the database.
        List<CustomerByCountry> customerspercountry = dao.getCustomersByCountry();

        // Cell value factory is set for each column in the Customers by Country report.
        CountryNameReportsColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        NumberofCustomersReportsColumn.setCellValueFactory(new PropertyValueFactory<>("customerCount"));

        // Populate the table view with the list of customers per country data that was
        // retrieved from the database.
        customersperCountry.getItems().setAll(customerspercountry);
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
     * User selects the Exit button and exits the application (from the Customers by Country screen).
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
