package DAO;

import Helper.TimeConversion;
import Helper.JDBC;
import Helper.MonthReview;
import Model.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXML;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

/**
 * ApptDAO class manages the Appointment methods used to interact with the database.
 */
public class ApptDAO {

    /**
     * An Observable list is populated with all the appointments from the database.
     *
     * @return allAppts all appointments.
     */
    public static ObservableList<Appointments> getEveryAppt(String check) {
        ObservableList<Appointments> allAppts = FXCollections.observableArrayList();
        String getApptsSQL = "SELECT * FROM appointments";
        if (!check.isEmpty()) {
            getApptsSQL += " WHERE " + check;
        }
        try {
            PreparedStatement ps = JDBC.getConnection().prepareStatement(getApptsSQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String appointmentTitle = rs.getString("Title");
                String appointmentDesc = rs.getString("Description");
                String appointmentLoc = rs.getString("Location");
                String appointmentType = rs.getString("Type");
                LocalDateTime startDateTime = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = rs.getTimestamp("End").toLocalDateTime();
                int appointmentCust_Id = rs.getInt("Customer_ID");
                int appointmentUser_Id = rs.getInt("User_ID");
                int appointmentContact_Id = rs.getInt("Contact_ID");
                Appointments appts = new Appointments(appointmentID, appointmentTitle, appointmentDesc, appointmentLoc, appointmentType, startDateTime, endDateTime, appointmentCust_Id, appointmentUser_Id, appointmentContact_Id);
                allAppts.add(appts);
            }
            // SQLException catch used in case an error occurs when accessing the database.
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return allAppts;
    }

    /**
     * Method that adds an appointment to Appointment in database. If appointment has end date/time before
     * start date/time, appointment falls outside business hours, or appointment overlaps with an already
     * existing appointment, then user will receive an error message. If no errors received, then appointment
     * is added to database using a SQL insert statement.
     *
     * @param appts user selects the "add" button.
     * @return false if an Error message occurs.
     * @return addedRows if appointment is successfully added.
     * @throws SQLException if an error occurs when accessing the database.
     */
    @FXML
    public static boolean addAppt(Appointments appts) throws SQLException {
        Connection cn = JDBC.getConnection();
        // If no errors received, then new appointment is added to the database.
        String add = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = cn.prepareStatement(add);
        ps.setInt(1, appts.getAppointmentID());
        ps.setString(2, appts.getTitle());
        ps.setString(3, appts.getDescription());
        ps.setString(4, appts.getLocation());
        ps.setString(5, appts.getType());
        ps.setTimestamp(6, Timestamp.valueOf(appts.getStart()));
        ps.setTimestamp(7, Timestamp.valueOf(appts.getEnd()));
        ps.setInt(8, appts.getCustomerID());
        ps.setInt(8, appts.getCustomerID());
        ps.setInt(9, appts.getUserID());
        ps.setInt(10, appts.getContactID());

        int addedRows = ps.executeUpdate();

        return addedRows > 0;
    }

    /**
     * Using a SQL query to view all appointments by a Customer ID, the appointment to be modified is
     * compared to the user's existing appointments, to ensure it does not overlap. If user attempts
     * to add an appointment that overlaps an already existing appointment that the user already has,
     * then an error message will appear.
     *
     * @throws SQLException if an error occurs when accessing the database.
     * @return true if appointment overlaps an already existing appointment.
     * @return false if appointment does not overlap an already existing appointment.
     */
    public static boolean apptOverlap(Appointments appts) throws SQLException {
        Connection cn = JDBC.getConnection();
        String select = "SELECT * FROM appointments WHERE Customer_ID = ? AND (? BETWEEN Start AND End OR ? BETWEEN Start AND End)";
        PreparedStatement apptOL = cn.prepareStatement(select);
        apptOL.setInt(1, appts.getCustomerID());
        apptOL.setString(2, TimeConversion.changeUserTimeToUTCTime(appts.getStart(), ZoneId.systemDefault()).toString());
        apptOL.setString(3, TimeConversion.changeUserTimeToUTCTime(appts.getEnd(), ZoneId.systemDefault()).toString());
        ResultSet rs = apptOL.executeQuery();
        if (rs.next()) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error!");
            error.setHeaderText("Appointment not modified.");
            error.setContentText("The appointment cannot be modified due to the same customer\nalready having an appointment during the requested time.");
            error.showAndWait();
            return true;
        }
        return false;
    }

    /**
     * Get the highest (maximum) appointment ID from the database. When adding a new appointment,
     * the appointment ID is generated by getting the highest appointment ID and adding 1 to it.
     *
     * @throws SQLException if an error occurs when accessing the database.
     * @return highestId the highest (maximum) appointment ID.
     */
    public static int getHighestApptID() throws SQLException {
        int highestApptId = 0;
        try {
            String max = "SELECT MAX(Appointment_ID) AS Max_ID FROM appointments";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(max);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                highestApptId = rs.getInt("Max_ID");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return highestApptId;
    }

    /**
     * Method that modifies an existing appointment in the database. If modified appointment has end date/time
     * before start date/time, appointment falls outside business hours, or appointment overlaps with an
     * already existing appointment, then user will receive an error message. If no errors received, then
     * modified appointment replaces/updates existing appointment using a SQL update statement to the database.
     *
     * @param appts user selects the "modify" button.
     * @return false if an Error message occurs.
     * @return modifiedRows if appointment is successfully modified.
     * @throws SQLException if an error occurs when accessing the database.
     *
     */
    public static boolean modifyAppt(Appointments appts) throws SQLException {
        Connection cn = JDBC.getConnection();

        // If no errors received, then existing appointment information in the database is
        // replaced with the updated/modified appointment information.
        String string = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = NOW(), Last_Updated_By = USER(), Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement pstatement = JDBC.getConnection().prepareStatement(string);
        pstatement.setString(1, appts.getTitle());
        pstatement.setString(2, appts.getDescription());
        pstatement.setString(3, appts.getLocation());
        pstatement.setString(4, appts.getType());
        pstatement.setTimestamp(5, Timestamp.valueOf(appts.getStart()));
        pstatement.setTimestamp(6, Timestamp.valueOf(appts.getEnd()));
        pstatement.setInt(7, appts.getCustomerID());
        pstatement.setInt(8, appts.getUserID());
        pstatement.setInt(9, appts.getContactID());
        pstatement.setInt(10, appts.getAppointmentID());

        int modifiedRows = pstatement.executeUpdate();
        return modifiedRows > 0;
    }

    /**
     * Delete appointment by appointment ID. User receives an information alert if appointment
     * is able to be successfully deleted.
     *
     * @throws SQLException if an error occurs when accessing the database.
     * @return rowsDeleted if appointment is successfully deleted.
     */
    public static boolean deleteAppt(int appointmentID) throws SQLException {
        Appointments apptDelete = getAppt(appointmentID);
        String apptId = String.valueOf(appointmentID);
        assert apptDelete != null;
        String apptType = apptDelete.getType();
        String delete = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement pstmt = JDBC.getConnection().prepareStatement(delete);
        pstmt.setInt(1, appointmentID);
        int deletedAppt = pstmt.executeUpdate();

        // If appointment is successfully deleted, user receives a Information message to
        // confirm the appointment was deleted.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Deleted Appointment");
        alert.setHeaderText("Appointment deleted.");
        alert.setContentText(apptType + " appointment with ID " + apptId + " was successfully deleted.");
        alert.showAndWait();
        return deletedAppt > 0;
    }

    /**
     * Get appointment by appointment ID from database using a SQL query.
     *
     * @throws SQLException if an error occurs when accessing the database.
     * @return new appointment match found.
     * @return null appointment match not found.
     */
    public static Appointments getAppt(int appointmentID) throws SQLException {
        String select = "SELECT * FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement preparedstatement = JDBC.getConnection().prepareStatement(select);
        preparedstatement.setInt(1, appointmentID);
        ResultSet resultset = preparedstatement.executeQuery();
        if (resultset.next()) {
            String appointmentTitle = resultset.getString("Title");
            String appointmentDesc = resultset.getString("Description");
            String appointmentLoc = resultset.getString("Location");
            String appointmentType = resultset.getString("Type");
            LocalDateTime appointmentStartDateTime = resultset.getTimestamp("Start").toLocalDateTime();
            LocalDateTime appointmentEndDateTime = resultset.getTimestamp("End").toLocalDateTime();
            int cust_Id = resultset.getInt("Customer_ID");
            int user_Id = resultset.getInt("User_ID");
            int contact_Id = resultset.getInt("Contact_ID");

            return new Appointments(appointmentID, appointmentTitle, appointmentDesc, appointmentLoc, appointmentType, appointmentStartDateTime, appointmentEndDateTime, cust_Id, user_Id, contact_Id);
        }
        return null;
    }

    /**
     * Upon login, all appointments are checked to see if the user has any upcoming appointments
     * within the next 15 minutes. If they do or do not have an upcoming appointment, the user
     * will receive a warning alert (if they do have an upcoming appointment) or an information
     * alert (if they do not have an upcoming appointment).
     *
     */
    public static void reviewClosestAppts() {
        ObservableList<Appointments> appts = getEveryAppt("");
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime currentPlusFifteen = current.plusMinutes(15);
        boolean apptsSoon = false;

        for (Appointments appt : appts) {
            if (appt.getStart().isBefore(currentPlusFifteen) && appt.getStart().isAfter(current)) {
                // Upon login, if user has an appointment within 15 minutes, they will receive
                // a warning message.
                apptsSoon = true;
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Warning!");
                warning.setHeaderText("There is an appointment soon.");
                warning.setContentText("Appointment ID " + appt.getAppointmentID() + " with the title of " + appt.getTitle() + " is within \n15 minutes at " + appt.getStart().toLocalTime() + " today " + appt.getStart().toLocalDate() + ".");
                Optional<ButtonType> result = warning.showAndWait();
                break;
            }
        }
        if (!apptsSoon) {
            Alert information = new Alert(Alert.AlertType.INFORMATION);
            // Upon login, if user doesn't have an appointment within 15 minutes, they will
            // receive an information message.
            information.setTitle("Information");
            information.setHeaderText("No appointments scheduled.");
            information.setContentText("No appointments are scheduled during the next 15 minutes.");
            Optional<ButtonType> result = information.showAndWait();
        }
    }

    /**
     * Used by the Customers by Contact report, where an observable list is
     * generated with all appointments for a contact ID.
     *
     * @throws SQLException if an error occurs when accessing the database.
     * @return appts all appointments for a Contact ID.
     */
    public static ObservableList<Appointments> getCustAppts(int contactID) {
        ObservableList<Appointments> appts = FXCollections.observableArrayList();
        String select = "SELECT * FROM appointments WHERE Contact_ID = ?";
        try {
            PreparedStatement psment = JDBC.getConnection().prepareStatement(select);
            psment.setInt(1, contactID);
            ResultSet resultst = psment.executeQuery();
            while (resultst.next()) {
                int apptID = resultst.getInt("Appointment_ID");
                String appointmentTitle = resultst.getString("Title");
                String appointmentDesc = resultst.getString("Description");
                String appointmentLoc = resultst.getString("Location");
                String appointmentType = resultst.getString("Type");
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                Timestamp tsStart = resultst.getTimestamp("Start", calendar);
                Timestamp tsEnd = resultst.getTimestamp("End", calendar);
                // LocalDateTime startDateTime = TimeConversion.changeUTCTimeToUserTime(tsStart);
                // LocalDateTime endDateTime = TimeConversion.changeUTCTimeToUserTime(tsEnd);
                LocalDateTime startDateTime = resultst.getTimestamp("Start").toLocalDateTime();
                LocalDateTime endDateTime = resultst.getTimestamp("End").toLocalDateTime();
                int cust_Id = resultst.getInt("Customer_ID");
                int user_Id = resultst.getInt("User_ID");
                int contact_Id = resultst.getInt("Contact_ID");
                Appointments appt = new Appointments(apptID, appointmentTitle, appointmentDesc, appointmentLoc, appointmentType, startDateTime, endDateTime, cust_Id, user_Id, contact_Id);
                appts.add(appt);
            }
            // SQLException catch if an error occurs when accessing the database.
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return appts;
    }

    /**
     * For the appointment by month and type report, appointment data for month and type is gathered
     * from the database.
     *
     * @throws SQLException if an error occurs when accessing the database.
     * @return monthReview appointments by month, type and total.
     */
    public List<MonthReview> getApptsMonthReview() throws SQLException {
        List<MonthReview> monthReview = new ArrayList<>();
        Connection cn = JDBC.getConnection();
        String select = "SELECT MONTH(start) as month, type, COUNT(*) as total FROM appointments GROUP BY MONTH(start), type";

        try (PreparedStatement ps = cn.prepareStatement(select);
             ResultSet resultS = ps.executeQuery()) {

            while (resultS.next()) {
                int month = resultS.getInt("month");
                String appointmentMonth = Month.of(month).toString();
                String appointmentType = resultS.getString("type");
                int appointmentTotal = resultS.getInt("total");
                monthReview.add(new MonthReview(appointmentMonth, appointmentType, appointmentTotal));
            }
        }
        return monthReview;
    }

    /**
     * Method used when attempting to save or add an appointment to check to ensure the
     * appointment falls within business hours (8am to 10pm Eastern Time).
     * @return insideOpenHours whether appointment is or is not within open office hours.
     */
    public static boolean openHoursChecker(LocalDateTime startTime, LocalDateTime endTime){
        boolean insideOpenHours = false;
        LocalTime openTime = LocalTime.of(8,00);
        LocalTime closeTime = LocalTime.of(22, 00);

        // Update user's local time to Eastern Time Zone time.
        ZonedDateTime appointmentESTimeZoneStart = TimeConversion.LocalUserTimeToESTime(LocalDateTime.from(startTime));
        LocalTime appointmentStartTime = appointmentESTimeZoneStart.toLocalTime();

        ZonedDateTime appointmentESTimeZoneEnd = TimeConversion.LocalUserTimeToESTime((LocalDateTime.from(endTime)));
        LocalTime appointmentEndTime = appointmentESTimeZoneEnd.toLocalTime();

        if(appointmentStartTime.isBefore(openTime) || appointmentEndTime.isAfter(closeTime)){
            insideOpenHours = false;
        }
        else{
            insideOpenHours = true;
        }
        return insideOpenHours;
    }

}
