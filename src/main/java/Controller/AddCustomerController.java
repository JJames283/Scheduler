package Controller;

import DAO.CountriesDAO;
import DAO.CustDAO;
import DAO.FirstLevelDivisionsDAO;
import Model.Countries;
import Model.Customer;
import Model.FirstLevelDivisions;
import javafx.collections.ObservableList;
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
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AddCustomerController class initialized. The AddCustomerController adds Customer data.
 */
public class AddCustomerController implements Initializable {

    /**
     * Text field for Customer ID.
     */
    @FXML
    private TextField addCustIdField;

    /**
     * Text field for Customer Name.
     */
    @FXML
    private TextField addCustNameField;

    /**
     * Text field for Customer Address.
     */
    @FXML
    private TextField addCustAddressField;

    /**
     * Text field for Customer Postal Code.
     */
    @FXML
    private TextField addCustPostalCodeField;

    /**
     * Combo box for Countries.
     */
    @FXML
    private ComboBox<Countries> addCustCountryComboBox;

    /**
     * ComboBox for First Level Divisions (States/Provinces).
     */
    @FXML
    private ComboBox<FirstLevelDivisions> addCustStateComboBox;

    /**
     * Text field for Customer Phone (number).
     */
    @FXML
    private TextField addCustPhoneField;

    /**
     * Button to be used when user wants to save (add) a customer.
     */
    @FXML
    private Button newCustomerSave;

    /**
     * Button to be used when user wants to cancel adding a customer.
     */
    @FXML
    private Button newCustomerCancel;

    /**
     * Button to be used if user wants to exit application.
     */
    @FXML
    private Button exitApplicationbtn;

    /**
     * If user selects the Save button, the customer is checked to ensure that all fields are
     * properly filled out, otherwise the user will receive an error pop up message. If all fields
     * are filled out correctly, and no errors occur when accessing the database, then the
     * customer will be saved and the user is returned to the main Customers scene.
     *
     * @param event user selects the save button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void saveAddCustomer(ActionEvent event) throws IOException, SQLException {
        // If a user leaves any of the fields in the add customer scene blank, they will receive
        // an error message.
        if (addCustNameField.getText().isEmpty() || addCustAddressField.getText().isEmpty() || addCustPostalCodeField.getText().isEmpty() || addCustCountryComboBox.getValue() == null || addCustStateComboBox.getValue() == null || addCustPhoneField.getText().isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error!");
            error.setHeaderText("Missing Information.");
            error.setContentText("Please make sure all fields are properly\ncompleted before attempting to save.");
            error.showAndWait();
            return;
        }
        Customer saveCust = getCust();
        try {
            // Use CustDAO to add/save appointment to the database.
            CustDAO.addCust(saveCust);
            // SQLException catch if an error occurs when accessing the database.
        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
        // If successfully saved, user is brought back to the main Customer scene.
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Customer.fxml")));
        Scene scene = new Scene(root, 900, 564);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();;
    }

    /**
     * getCust method is used above to save the customer to database.
     *
     * @throws SQLException if an error occurs when accessing the database.
     * @return new Customer.
     */
    private Customer getCust() throws SQLException {
        // 1 is added to the highest customer ID identified in creation of a new customer to get an unused ID.
        int customer_Id = CustDAO.getHighestCustId() + 1;
        String custName = addCustNameField.getText();
        String custAdd = addCustAddressField.getText();
        String custPhone = addCustPhoneField.getText();
        FirstLevelDivisions FLDiv = addCustStateComboBox.getSelectionModel().getSelectedItem();
        int div_Id = FLDiv.getDivisionID();
        String custPostalCode = addCustPostalCodeField.getText();
        return new Customer(customer_Id, custName, custAdd, custPostalCode, custPhone, div_Id, FLDiv);
    }

    /**
     * If the user selects the cancel button, they are returned to the main Customers scene
     * (from the Add Customer scene).
     *
     * @param event user selects the cancel button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void cancelgobacktoCustomer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Customer.fxml")));
        Scene scene = new Scene(root, 900, 564);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * User selects the Exit button and exits the application (from the Add Customer scene).
     * Please note, the user most confirm they wish to exit before they exit the application.
     *
     * @param event user selects the Exit button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void clickexitApplicationbtn(ActionEvent event) throws IOException {
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
     * AddCustomerController initialized. Customer ID text field disabled so user is unable to
     * manually update it, rather it is automatically generated by adding 1 to the maximum
     * (highest) already existing Customer ID. Combo box fields (Country and State/Province)
     * are also filled.
     *
     * @param url url.
     * @param resourceBundle resourceBundle.
     * @throws RuntimeException exceptions that can be thrown during the normal operation of the Java Virtual Machine.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addCustIdField.setDisable(true);
        try {
            addCustIdField.setText(String.valueOf(CustDAO.getHighestCustId() + 1));
            // SQLException catch if an error occurs when accessing the database.
        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getMessage());
        }

        ObservableList<Countries> everyCountry = CountriesDAO.getEveryCountry();
        addCustCountryComboBox.setItems(everyCountry);

        // Lambda function used as a listener (addListener) added to the Country combo box so that the division
        // (state/province) combo box is filled once user makes a Country combo box selection.
        addCustCountryComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, formerCountry, updatedCountry) -> {
            ObservableList<FirstLevelDivisions> FLDiv = FirstLevelDivisionsDAO.FirstLevelDivUsingCountry_ID(updatedCountry.getCountryID());
            addCustStateComboBox.setItems(FLDiv);
        });
    }
}