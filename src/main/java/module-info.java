module project.c195pa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports Controller;
    opens Controller to javafx.fxml;
    exports Main;
    opens Main to javafx.fxml;
    exports Model;
    opens Model to javafx.fxml;
    exports DAO;
    opens DAO to javafx.fxml;
    exports Helper;
    opens Helper to javafx.fxml;
}