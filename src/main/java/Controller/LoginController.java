package Controller;

import DAO.ApptDAO;
import DAO.LoginDAO;
import Helper.Logger;
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
import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * LoginController initialized. The LoginController controls the functionality
 * and features of the Login scene.
 */
public class LoginController implements Initializable {

    /**
     * Label to display "Login" text.
     */
    @FXML
    private Label mainLoginTitle;

    /**
     * Label to indicate where to enter a username.
     */
    @FXML
    private Label usernameLoginSceneLabel;

    /**
     * Text field for a username.
     */
    @FXML
    private TextField usernameloginField;

    /**
     * Label to indicate where to enter a password.
     */
    @FXML
    private Label PWLoginLabel;

    /**
     * Text field for a password.
     */
    @FXML
    private TextField PWLoginField;

    /**
     * Label to display "Location" (Time Zone).
     */
    @FXML
    private Label loginscreenlocLabel;

    /**
     * Label for user's Location (Time Zone).
     */
    @FXML
    private Label locationTZLabel;

    /**
     * Button for user to select to log in.
     */
    @FXML
    private Button loginscreenLoginBtn;

    /**
     * Button to exit application.
     */
    @FXML
    private Button loginscreenExitBtn;


    /**
     * User selects the Exit button and exits the application (from the Login scene).
     * Please note, the user most confirm they wish to exit before they exit the application.
     *
     * @param event user selects the Exit button.
     * @throws IOException from FXMLLoader.
     */
    @FXML
    void clicktoexitapplication(ActionEvent event) throws IOException {
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
     * The user logs into the scheduling application by submitting their username and password. If
     * the username and password match the database, then they are brought to the application's
     * Main Menu. If they do not match, an error message appears, and the user is able to attempt
     * to log in again. In addition, either the English or French alerts bundle is retrieved
     * based on user's location.
     *
     * @param event user selects the save button.
     * @throws IOException from FXMLLoader.
     * @throws SQLException if an error occurs when accessing the database.
     */
    @FXML
    void logintoapplication(ActionEvent event) throws IOException, SQLException {
        String loginName = usernameloginField.getText();
        String loginPW = PWLoginField.getText();

        LoginDAO dao = new LoginDAO();
        boolean validLogin = LoginDAO.comparePW(loginName, loginPW);

        // Logger records login.
        Logger.recordLogin(loginName, validLogin);

        // If valid login, user is brought to the Main Menu scene.
        if (validLogin) {
            ApptDAO.reviewClosestAppts();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/MainMenu.fxml")));
            Scene scene = new Scene(root, 250, 225);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            // Depending on user's location, get either the English or French language alerts.
            ResourceBundle languageAlert;
            if (Locale.getDefault().getLanguage().equals("fr")) {
                languageAlert = ResourceBundle.getBundle("alerts_fr");
            } else {
                languageAlert = ResourceBundle.getBundle("alerts_en");
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(languageAlert.getString("alertHeader"));
            alert.setContentText(languageAlert.getString("alertBody"));
            alert.showAndWait();
        }
    }

    /**
     * Login initialized and either English or French bundle is loaded based on where the user
     * is located at login.
     *
     * @param url url.
     * @param resourceBundle resourceBundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle bundle = ResourceBundle.getBundle("login");

        // Get where the user is located (specifically, their time zone).
        Locale userTZ = Locale.getDefault();

        // Get either the English or French bundle based on where the user is located (time zone).
        ResourceBundle timezone;
        if (userTZ.getLanguage().equals("fr")) {
            timezone = ResourceBundle.getBundle("login_fr", userTZ);
        } else {
            timezone = ResourceBundle.getBundle("login_en", userTZ);
        }

        // Set either the French or English translation for each part of the Login scene.
        mainLoginTitle.setText(timezone.getString("mainLoginTitle"));
        usernameLoginSceneLabel.setText(timezone.getString("usernameLoginSceneLabel"));
        PWLoginLabel.setText(timezone.getString("PWLoginLabel"));
        loginscreenlocLabel.setText(timezone.getString("getlocationLabel"));
        loginscreenLoginBtn.setText(timezone.getString("loginscreenLoginBtn"));
        loginscreenExitBtn.setText(timezone.getString("loginscreenExitBtn"));
        ZoneId zid = ZoneId.systemDefault();
        String zone = String.valueOf(zid);
        locationTZLabel.setText(zone);
    }
}