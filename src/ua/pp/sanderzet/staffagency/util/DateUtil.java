package ua.pp.sanderzet.staffagency.util;

/**
 * Created by alzet on 25.04.17.
 */

import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Helper functions for handling dates.
 *
 * @author Marco Jakob
 */


public class DateUtil {
    /**
     * The date pattern that is used for conversion. Change as you wish.
     */

    private static final String DATE_PATTERN = "dd.MM.yyyy";

    /**
     * The date formatter.
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /**
     * Returns the given date as a well formatted String. The above defined
     * {@link DateUtil#DATE_PATTERN} is used.
     *
     * @param date the date to be returned as a string
     * @return formatted string
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMATTER.format(date);
    }

    /**
     * Converts a String in the format of the defined {@link DateUtil#DATE_PATTERN}
     * to a {@link LocalDate} object.
     * <p>
     * Returns null if the String could not be converted.
     *
     * @param dateString the date as String
     * @return the date object or null if it could not be converted
     */

    public static LocalDate parse(String dateString) {

        try {
            if(dateString == null) {
                return null;
            }
            return DATE_FORMATTER.parse(dateString, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Checks the String whether it is a valid date.
     *
     * @param dateString
     * @return true if the String is a valid date
     */

    public static boolean validDate(String dateString) {
        return DateUtil.parse(dateString) != null;
    }


//        Converter from LocalDate to String. Will be necessary for TextFieldTableCell.

    public static StringConverter<LocalDate> localDateStringConverter = new StringConverter<LocalDate>() {
        @Override
        public String toString(LocalDate object) {
            return DateUtil.format(object);
        }

        @Override
        public LocalDate fromString(String string) {
            return DateUtil.parse(string);
        }
    };
}