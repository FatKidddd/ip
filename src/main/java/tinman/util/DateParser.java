package tinman.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import tinman.exception.TinManException;

public class DateParser {
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");
    
    // DateTime formats
    private static final DateTimeFormatter DATETIME_INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter DATETIME_OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");
    
    public static LocalDate parseDate(String dateString) throws TinManException {
        try {
            return LocalDate.parse(dateString, INPUT_FORMAT);
        } catch (DateTimeParseException e) {
            throw new TinManException.InvalidDateFormatException();
        }
    }
    
    public static String formatDate(LocalDate date) {
        return date.format(OUTPUT_FORMAT);
    }
    
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
    
    // Utility method to try parsing either date or datetime
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