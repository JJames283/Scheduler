package Main;

import Helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Main class extends Application class.
 */
public class Main extends Application {

    /**
     * Main class loads Login scene when application is first opened.
     * @param stage main stage of the application.
     * @throws IOException from FXMLLoader.
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/View/Login.fxml")));
        Scene scene = new Scene(root, 426, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Using JDBC Helper, a connection is opened to the database.
     *
     * @throws SQLException if an error occurs when accessing the database.
     */
    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();
        launch();
    }
}
