package Controller;

import DAO.CustDAO;
import Model.Customer;
import Model.FirstLevelDivisions;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

/**
 * CustomersController class initialized. The CustomersController controls the functionality
 * and features of the Customers scene.
 */
public class CustomersController implements Initializable {

    /**
     * Allows for a selected Customer to be modified, deleted, etc.
     */
    private static Customer selectCust;

    /**
     * Table view for Customers.
     */
    @FXML
    private TableView<Customer> customerTable;

    /**
     * Table column for Customer ID.
     */
    @FXML
    private TableColumn<Customer, Integer> custIdcolumn;

    /**
     * Table column for Customer Name.
     */
    @FXML
    private TableColumn<Customer, String> custNamecolumn;

    /**
     * Table column for Customer Address.
     */
    @FXML
    private TableColumn<Customer, String> custAddcolumn;

    /**
     * Table column for Customer Phone.
     */
    @FXML
    private TableColumn<Customer, String> custPhonecolumn;

    /**
     * Table column for Customer State(or Province).
     */
    @FXML
    private TableColumn<Customer, String> custStatecolumn;

    /**
     * Table column for Customer Postal Code.
     */
    @FXML
    private TableColumn<Customer, String> custPostalCodecolumn;

    /**
     * Button to add a Customer.
     */
    @FXML
    private Button addnewCustButton;

    /**
     * Button to delete an existing Customer.
     */
    @FXML
    private Button deleteCustButton;

    /**
     * Button to modify an existing Customer.
     */
    @FXML
    private Button modifyCustButton;

    /**
     * Button to return to the Main Menu.
     */
    @FXML
    private Button gotoMainMenubutton;

    /**
     * Button to go directly to Appointments scene (from Customers).
     */
    @FXML
    private Button gotoAppointmentsbutton;

    /**
     * Button to go directly to Reports scene (from Customers).
     */
    @FXML
    private Button gotoReportsbutton;

    /**
     * Button to exit application.
     */
    @FXML
    private Button exitAppButton;


    /**
     * If user selects the Add button, they are brought ot the Add Customer scene to
     * add a Customer.
     *
     * @param event user selects the Add button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void clickandgotoaddnewCust(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/AddCustomer.fxml")));
        Scene scene = new Scene(root, 600, 600);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * If user selects the Delete button, they must first confirm they wish to delete the
     * Customer. If they confirm "OK", and there are no errors when accessing the database,
     * the Customer is deleted. If the user selects the Delete button without first selecting
     * an existing Customer, they will receive an error message.
     *
     * @param event user selects the Delete button.
     * @throws SQLException if an error occurs when accessing the database.
     */
    @FXML
    void clickdeleteCustButton(ActionEvent event) throws SQLException {
        selectCust = customerTable.getSelectionModel().getSelectedItem();
        if (selectCust == null) {
            // If user selects the delete button without selecting a customer to delete,
            // they will receive an error message.
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error!");
            error.setContentText("You must choose a customer to delete.");
            error.showAndWait();
        } else {
            // The user must confirm they wish to delete the appointment by selecting "OK".
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setContentText("Do you wish to delete this customer?");
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                CustDAO.removeCust(selectCust.getId());
                customerTable.setItems(CustDAO.getEveryCust());
            }
        }
    }

    /**
     * If user selects the Modify button, they are brought ot the Modify Customer scene to
     * modify an already existing Customer. However, if the user selects the Modify button without
     * first selecting an existing Customer, they will receive an error message.
     *
     * @param event user selects the Modify button.
     * @throws IOException from FXMLLoader.
     * @throws SQLException if an error occurs when accessing the database.
     */
    @FXML
    void clickandgotomodifyCust (ActionEvent event) throws IOException, SQLException {
        selectCust = customerTable.getSelectionModel().getSelectedItem();
        if (selectCust == null) {
            // If user selects the modify button without selecting a customer to modify,
            // they will receive an error message.
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error!");
            error.setContentText("You must choose a customer to modify.");
            error.showAndWait();
        } else {
            // User is brought to the Modify Customer scene.
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/View/ModifyCustomer.fxml"));
            fxmlLoader.load();

            ModifyCustomerController mc = fxmlLoader.getController();
            mc.forwardCust(customerTable.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Parent scene = fxmlLoader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * If the user selects the Main Menu button, they are brought to the Main Menu (from Customers).
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
     * If the User selects the Appointments button, they are brought to the Appointments scene
     * (from Customers).
     *
     * @param event user selects the Appointments button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    public void clickandgotoAppointments(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Appointments.fxml")));
        Scene scene = new Scene(root, 900, 584);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * If the user selects the Reports button, they are brought to the Reports scene (from Customers).
     *
     * @param event user selects the Reports button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    public void clickandgotoReports(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Reports.fxml")));
        Scene scene = new Scene(root, 492, 332);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * User selects the Exit button and exits the application (from the Customers scene).
     * Please note, the user most confirm they wish to exit before they exit the application.
     *
     * @param event user selects the Exit button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void clicktoExitApp(ActionEvent event) throws IOException {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setContentText("Do you wish to exit the Scheduling Application?");
        Optional<ButtonType> result = confirmation.showAndWait();
        // The user must confirm they wish to exit the application by selecting "OK".
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Customers initialized and customers table filled. A lambda expression is used to set the
     * cell value factory for the customer's State (or province if outside the United States).
     * @param url url.
     * @param resourceBundle resourceBundle.
     * @throws RuntimeException exceptions that can be thrown during the normal operation of the Java Virtual Machine.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CustDAO dao = new CustDAO();
        customerTable.setItems(CustDAO.getEveryCust());
        custIdcolumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        custNamecolumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        custAddcolumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPostalCodecolumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        custPhonecolumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        // A lambda expression is used for the custStatecolumn below, to setCellValueFactory for
        // the State (or Province if outside the United States).
        custStatecolumn.setCellValueFactory(cellData -> {
            SimpleStringProperty ssp = new SimpleStringProperty();
            FirstLevelDivisions FLDiv = null;
            try {
                FLDiv = cellData.getValue().getDivision();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (FLDiv != null) {
                ssp.setValue(FLDiv.getDivisionName());
            } else {
                ssp.setValue("");
            }
            return ssp;
        });

    }

}
