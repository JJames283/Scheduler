package Helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Timestamp;
import java.time.*;

/**
 * This class defines and manges Time Conversions.
 */
public class TimeConversion {

    /**
     * Change (convert) UTC time to user time (user's local time).
     */
    public static LocalDateTime changeUTCTimeToUserTimeoriginal(Timestamp tstmp) {
        ZoneId utcZID = ZoneId.systemDefault();
        Instant time = tstmp.toInstant();
        LocalDateTime userDT = LocalDateTime.ofInstant(time, utcZID);
        ZonedDateTime zonedDT = userDT.atZone(utcZID);
        return zonedDT.toLocalDateTime();
    }

    public static LocalDateTime changeUTCTimeToUserTime(Timestamp tstmp) {
        LocalDateTime ldtUTC = tstmp.toLocalDateTime();
        ZonedDateTime zdtUTC = ldtUTC.atZone(ZoneId.systemDefault());
        ZonedDateTime zdtLocal = zdtUTC.withZoneSameInstant(ZoneId.of("UTC"));
        LocalDateTime ldtLocal = zdtLocal.toLocalDateTime();
        Timestamp localTime = Timestamp.valueOf(ldtLocal);
        return localTime.toLocalDateTime();
    }



    /**
     * Convert user time (user's local time) to UTC.
     */
    public static LocalDateTime changeUserTimeToUTCTime(LocalDateTime userDT, ZoneId userZID) {
        Instant userTimeInst = userDT.atZone(userZID).toInstant();
        Instant utcTimeInst = userTimeInst.atZone(ZoneId.of("UTC")).toInstant();
        return LocalDateTime.ofInstant(utcTimeInst, ZoneId.systemDefault());
    }

    /**
     * Coverts office hours (business hours) to user's time zone.
     */
    public static ObservableList<LocalTime> getOfficeHoursUserTZ(ZoneId ZID) {
        ObservableList<LocalTime> officeHours = FXCollections.observableArrayList();
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(22, 0);
        LocalDate userDate = LocalDate.now(ZID);
        while (startTime.isBefore(endTime)) {
            LocalDateTime userDT = LocalDateTime.of(userDate, startTime);
            ZonedDateTime zdt = ZonedDateTime.of(userDT, ZID);
            officeHours.add(zdt.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
            startTime = startTime.plusMinutes(15);
        }
        return officeHours;
    }

    /**
     * Establishes office hours as being 8:00 am to 10:00 pm (22:00) eastern time.
     */
    public static ObservableList<LocalTime> getOpenOfficeHours() {
        ObservableList<LocalTime> officeHours = FXCollections.observableArrayList();
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(22, 0);
        ZoneId EST = ZoneId.of("America/New_York");
        LocalDate userDate = LocalDate.now();
        while (startTime.isBefore(endTime)) {
            LocalDateTime LDT = LocalDateTime.of(userDate, startTime);
            ZonedDateTime ZDT = ZonedDateTime.of(LDT, EST);
            officeHours.add(ZDT.withZoneSameInstant(ZoneId.systemDefault()).toLocalTime());
            startTime = startTime.plusMinutes(15);
        }
        return officeHours;
    }

    /**
     * This method converts a time from user to est timezone
     *
     * @param ldt the localdatetime to convert
     * @return estTime the converted time
     */
    public static ZonedDateTime LocalUserTimeToESTime(LocalDateTime ldt) {
        ZoneId localZoneId = ZoneId.systemDefault();
        ZonedDateTime userTime = ZonedDateTime.of(ldt, localZoneId);
        ZoneId estZoneId = ZoneId.of("US/Eastern");
        ZonedDateTime estTime = ZonedDateTime.ofInstant(userTime.toInstant(), estZoneId);
        return estTime;
    }

}