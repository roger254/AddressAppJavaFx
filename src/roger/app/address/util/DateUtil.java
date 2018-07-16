package roger.app.address.util;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    //Helper functions for handling dates

    //the date pattern that is used for conversion.
    private static final String DATE_PATTERN = "dd.MM.yyyy";

    //date formatter
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /*
    Returns the given date as well formatted String
    {@link DateUtil#DATE_PATTERN} is used

    @param date the date to be returned as a string
    @return formatted string
     */

    public static String format(LocalDate date) {
        if (date == null)
            return null;
        return DATE_TIME_FORMATTER.format(date);
    }

    /*
    Converts a string in the format of the defined {@link DateUtil#DATE_PATTERN}
    to a {@link LocalDate} object
    Returns null if the String could not be converted
    @param dateString the date as String
    @return the date object or null if it could not be converted
     */

    public static LocalDate parse(String dateString) {
        try {
            return DATE_TIME_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeException e) {
            return null;
        }
    }

    /*
    Checks the String whether it is a valid date.

    @param dateString
    @return true if the String is a valid date
     */
    public static boolean validDate(String dateString) {
        //try to parse the String
        return DateUtil.parse(dateString) != null;
    }
}
