package tinman.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import tinman.exception.TinManException;

/**
 * Utility class for parsing and formatting dates and date-times.
 * Provides methods to handle both date-only and date-time formats with flexible parsing.
 */
public class DateParser {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");
    
    // DateTime formats
    private static final DateTimeFormatter DATETIME_INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter DATETIME_OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");
    
    /**
     * Parses a date string into a LocalDate object.
     *
     * @param dateString The date string in yyyy-MM-dd format.
     * @return LocalDate object parsed from the string.
     * @throws TinManException If the date format is invalid.
     */
    public static LocalDate parseDate(String dateString) throws TinManException {
        try {
            return LocalDate.parse(dateString, INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new TinManException.InvalidDateFormatException();
        }
    }
    
    /**
     * Formats a LocalDate object into a readable string.
     *
     * @param date The LocalDate to format.
     * @return Formatted date string.
     */
    public static String formatDate(LocalDate date) {
        return date.format(OUTPUT_FORMAT);
    }
    
    /**
     * Checks if a string matches the valid date format.
     *
     * @param dateString The string to validate.
     * @return True if the format is valid, false otherwise.
     */
    public static boolean isValidDateFormat(String dateString) {
        try {
            LocalDate.parse(dateString, INPUT_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    public static String dateToSaveFormat(LocalDate date) {
        return date.format(INPUT_FORMAT);
    }
    
    public static LocalDate dateFromSaveFormat(String dateString) throws TinManException {
        return parseDate(dateString);
    }
    
    // DateTime methods
    /**
     * Parses a date-time string into a LocalDateTime object.
     *
     * @param dateTimeString The date-time string in yyyy-MM-dd HHmm format.
     * @return LocalDateTime object parsed from the string.
     * @throws TinManException If the date-time format is invalid.
     */
    public static LocalDateTime parseDateTime(String dateTimeString) throws TinManException {
        try {
            return LocalDateTime.parse(dateTimeString, DATETIME_INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new TinManException.InvalidDateFormatException();
        }
    }
    
    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATETIME_OUTPUT_FORMAT);
    }
    
    /**
     * Checks if a string is in valid date-time format.
     *
     * @param dateTimeString The string to validate.
     * @return True if the format is valid, false otherwise.
     */
    public static boolean isValidDateTimeFormat(String dateTimeString) {
        try {
            LocalDateTime.parse(dateTimeString, DATETIME_INPUT_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
    
    public static String dateTimeToSaveFormat(LocalDateTime dateTime) {
        return dateTime.format(DATETIME_INPUT_FORMAT);
    }
    
    public static LocalDateTime dateTimeFromSaveFormat(String dateTimeString) throws TinManException {
        return parseDateTime(dateTimeString);
    }
    
    /**
     * Attempts to parse the input string as either a date or date-time.
     * Tries date-time format first, then falls back to date format.
     *
     * @param input String to parse in format "yyyy-MM-dd" or "yyyy-MM-dd HHmm".
     * @return LocalDate if input is date format, LocalDateTime if input is date-time format.
     * @throws TinManException If input does not match any valid format.
     */
    public static Object parseFlexible(String input) throws TinManException {
        // Try datetime first (more specific)
        if (isValidDateTimeFormat(input)) {
            return parseDateTime(input);
        }
        // Fall back to date
        if (isValidDateFormat(input)) {
            return parseDate(input);
        }
        // Neither format matches
        throw new TinManException.InvalidDateFormatException();
    }
}
