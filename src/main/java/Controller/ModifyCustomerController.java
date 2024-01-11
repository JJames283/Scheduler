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
 * ModifyCustomerController class initialized. The ModifyCustomerController modifies
 * Customer data.
 */
public class ModifyCustomerController implements Initializable {

    /**
     * Text field for Customer ID.
     */
    @FXML
    private TextField modifyCustIdTextField;

    /**
     * Text field for Customer Name.
     */
    @FXML
    private TextField modifyCustNameTextField;

    /**
     * Text field for Customer Address.
     */
    @FXML
    private TextField modifyCustAddTextField;

    /**
     * Text field for Customer Phone (number).
     */
    @FXML
    private TextField modifyCustPhoneTextField;

    /**
     * Combo box for Customer Country.
     */
    @FXML
    private ComboBox<Countries> modifyCustCountryComboBox;

    /**
     * Combo box for First Level Divisions (Customer State/Province).
     */
    @FXML
    private ComboBox<FirstLevelDivisions> modifyCustStateComboBox;

    /**
     * Text field for Customer Postal Code.
     */
    @FXML
    private TextField modifyCustPostalCodeTextField;

    /**
     * Button to save modified customer.
     */
    @FXML
    private Button modifycustomerSavebtn;

    /**
     * Button to cancel modifying customer.
     */
    @FXML
    private Button modifycustomerCancelbtn;

    /**
     * Button to exit application
     */
    @FXML
    private Button exitApplicationbtn;


    /**
     * If user selects the Save button, the modified Customer is checked to ensure that all
     * fields are properly filled out, otherwise the user will receive an error pop up message.
     * If all fields are filled out correctly, and no errors occur when accessing the database,
     * then the modified Customer will be saved and the user is returned to the main Customers
     * scene.
     *
     * @param event user selects the Save button.
     * @throws IOException from FXMLLoader.
     * @throws SQLException if an error occurs when accessing the database.
     */
    @FXML
    void saveModifiedCust(ActionEvent event) throws IOException, SQLException {
        // If a user leaves any of the fields in the add appointment scene blank, they will receive
        // an error message.
        if (modifyCustNameTextField.getText().isEmpty() || modifyCustAddTextField.getText().isEmpty() || modifyCustPhoneTextField.getText().isEmpty() || modifyCustCountryComboBox.getValue() == null || modifyCustStateComboBox.getValue() == null || modifyCustPostalCodeTextField.getText().isEmpty()) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error!");
            error.setHeaderText("Missing Information");
            error.setContentText("Please fill in all information before attempting to save.");
            error.showAndWait();
            return;
        }
        String custName = modifyCustNameTextField.getText();
        int customer_Id = Integer.parseInt(modifyCustIdTextField.getText());
        String custAddress = modifyCustAddTextField.getText();
        String custPostalCode = modifyCustPostalCodeTextField.getText();
        String custPhone = modifyCustPhoneTextField.getText();
        int custDiv_Id = modifyCustStateComboBox.getSelectionModel().getSelectedItem().getDivisionID();

        // The database is accessed with the CustDAO, the already existing customer is retrieved and
        // then updated with the new modified information.
        Customer cust = CustDAO.getCustUsingCustID(customer_Id);
        assert cust != null;
        cust.setName(custName);
        cust.setAddress(custAddress);
        cust.setPostalCode(custPostalCode);
        cust.setPhone(custPhone);
        cust.setDivisionID(custDiv_Id);
        try {
            // Use CustDAO to modify existing customer to the database.
            CustDAO.modifyCust(cust);
            // SQLException catch in case there is an issue interacting with the database.
        } catch (SQLException exception) {
            System.out.println("Error: " + exception.getMessage());
        }
        // If successfully modified/saved, user is brought back to the main Customer scene.
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Customer.fxml")));
        Scene scene = new Scene(root, 900, 564);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * If the user selects the cancel button, they are returned to the main Customers scene
     * (from the Modify Customer scene).
     *
     * @param event user selects the Cancel button.
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
     * User selects the Exit button and exits the application (from the Modify Customer screen).
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
     * If user selects to modify a customer, the existing customer data is "sent" from
     * the Customers scene, to the Modify Customer scene.
     *
     * @param cust user selects a customer and clicks the Modify button.
     * @throws SQLException if an error occurs when accessing the database.
     */
    @FXML
    public void forwardCust(Customer cust) throws SQLException {
        modifyCustNameTextField.setText(cust.getName());
        modifyCustIdTextField.setText(String.valueOf(cust.getId()));
        modifyCustAddTextField.setText(cust.getAddress());
        modifyCustPhoneTextField.setText(cust.getPhone());
        modifyCustPostalCodeTextField.setText(cust.getPostalCode());

        // Country combo box is filled with all the countries in the database.
        ObservableList<Countries> combobox = CountriesDAO.getEveryCountry();
        modifyCustCountryComboBox.setItems(combobox);

        // Set value for the country the customer selects.
        modifyCustCountryComboBox.setValue(cust.getCountry());

        // Lambda function used as a listener (addListener) added to the Country combo box so that the division
        // (state/province) combo box is filled once user makes a Country combo box selection.
        modifyCustCountryComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldCountry, newCountry) -> {
            ObservableList<FirstLevelDivisions> FLDiv = FirstLevelDivisionsDAO.FirstLevelDivUsingCountry_ID(newCountry.getCountryID());
            modifyCustStateComboBox.setItems(FLDiv);

            // If selected country has state/provinces included in database, the state/provinces are
            // displayed. If selected country does not have state/provinces included, then nothing is
            // displayed in the State (or Province) combo box.
            try {
                if (cust.getCountry().equals(newCountry) && cust.getDivision() != null && FLDiv.contains(cust.getDivision())) {
                    modifyCustStateComboBox.setValue(cust.getDivision());
                } else {
                    modifyCustStateComboBox.setValue(FLDiv.get(0));
                }
            } catch (SQLException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        });

        // The customer state (or province) combo box is filled based on which country is selected.
        ObservableList<FirstLevelDivisions> fldiv = FirstLevelDivisionsDAO.FirstLevelDivUsingCountry_ID(cust.getCountry().getCountryID());
        modifyCustStateComboBox.setItems(fldiv);

        // The division that the user selects is set.
        modifyCustStateComboBox.getSelectionModel().select(cust.getDivision());
    }

    /**
     * Modify Customer initialized. Customer ID text field disabled so that users are not able
     * to manually modify customer IDs.
     *
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifyCustIdTextField.setDisable(true);
    }
}