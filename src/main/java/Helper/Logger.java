package Helper;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * This class defines and manges the Logger.
 */
public class Logger {

    /**
     * Used by logger to record to file login_activity.txt
     */
    private static final String FILENAME = "login_activity.txt";

    /**
     * Record and add to login_activity.txt attempted login with timestamp, valid or invalid login, and user.
     */
    public static void recordLogin(String user, boolean valid) {
        LocalDateTime ts = LocalDateTime.now();
        String login = valid ? "Valid" : "Invalid";
        String logger = String.format("%s - %s: %s\n", ts, login, user);

        try {
            FileWriter FW = new FileWriter(FILENAME, true); // add login attempt to login_activity.txt file.
            FW.write(logger);
            FW.close();
            // IOException catch used in case an I/O error occurs.
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
