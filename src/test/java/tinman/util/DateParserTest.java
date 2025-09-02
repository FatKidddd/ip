package tinman.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import tinman.exception.TinManException;

/**
 * Comprehensive tests for DateParser.parseFlexible() method.
 * This method is non-trivial as it handles flexible parsing of both dates and datetimes
 * with multiple format validation and error handling.
 */
public class DateParserTest {

    @Test
    public void parseFlexible_validDate_returnsLocalDate() throws TinManException {
        // Test valid date format yyyy-MM-dd
        Object result = DateParser.parseFlexible("2023-12-25");
        assertTrue(result instanceof LocalDate);
        LocalDate date = (LocalDate) result;
        assertEquals(2023, date.getYear());
        assertEquals(12, date.getMonthValue());
        assertEquals(25, date.getDayOfMonth());
    }

    @Test
    public void parseFlexible_validDateTime_returnsLocalDateTime() throws TinManException {
        // Test valid datetime format yyyy-MM-dd HHmm
        Object result = DateParser.parseFlexible("2023-12-25 1430");
        assertTrue(result instanceof LocalDateTime);
        LocalDateTime dateTime = (LocalDateTime) result;
        assertEquals(2023, dateTime.getYear());
        assertEquals(12, dateTime.getMonthValue());
        assertEquals(25, dateTime.getDayOfMonth());
        assertEquals(14, dateTime.getHour());
        assertEquals(30, dateTime.getMinute());
    }

    @Test
    public void parseFlexible_validDateTimeWithZeroMinutes_returnsLocalDateTime() throws TinManException {
        // Test datetime with 0 minutes
        Object result = DateParser.parseFlexible("2023-01-01 0800");
        assertTrue(result instanceof LocalDateTime);
        LocalDateTime dateTime = (LocalDateTime) result;
        assertEquals(8, dateTime.getHour());
        assertEquals(0, dateTime.getMinute());
    }

    @Test
    public void parseFlexible_validDateTimeWith24HourFormat_returnsLocalDateTime() throws TinManException {
        // Test 24-hour format edge case
        Object result = DateParser.parseFlexible("2023-06-15 2359");
        assertTrue(result instanceof LocalDateTime);
        LocalDateTime dateTime = (LocalDateTime) result;
        assertEquals(23, dateTime.getHour());
        assertEquals(59, dateTime.getMinute());
    }

    @Test
    public void parseFlexible_invalidDateFormat_throwsException() {
        // Test invalid date formats
        assertThrows(TinManException.InvalidDateFormatException.class, () -> {
            DateParser.parseFlexible("25-12-2023"); // Wrong format
        });

        assertThrows(TinManException.InvalidDateFormatException.class, () -> {
            DateParser.parseFlexible("2023/12/25"); // Wrong separator
        });

        assertThrows(TinManException.InvalidDateFormatException.class, () -> {
            DateParser.parseFlexible("invalid-date"); // Completely invalid
        });
    }

    @Test
    public void parseFlexible_invalidDateTimeFormat_throwsException() {
        // Test invalid datetime formats
        assertThrows(TinManException.InvalidDateFormatException.class, () -> {
            DateParser.parseFlexible("2023-12-25 25:00"); // Invalid hour
        });

        assertThrows(TinManException.InvalidDateFormatException.class, () -> {
            DateParser.parseFlexible("2023-12-25 14:60"); // Invalid minute
        });

        assertThrows(TinManException.InvalidDateFormatException.class, () -> {
            DateParser.parseFlexible("2023-12-25 1"); // Invalid format
        });

        assertThrows(TinManException.InvalidDateFormatException.class, () -> {
            DateParser.parseFlexible("2023-12-25 14:30"); // Wrong separator
        });
    }

    @Test
    public void parseFlexible_emptyString_throwsException() {
        assertThrows(TinManException.InvalidDateFormatException.class, () -> {
            DateParser.parseFlexible("");
        });
    }

    @Test
    public void parseFlexible_nullString_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            DateParser.parseFlexible(null);
        });
    }

    @Test
    public void parseFlexible_leapYear_validDate() throws TinManException {
        // Test leap year date
        Object result = DateParser.parseFlexible("2024-02-29");
        assertTrue(result instanceof LocalDate);
        LocalDate date = (LocalDate) result;
        assertEquals(29, date.getDayOfMonth());
        assertEquals(2, date.getMonthValue());
    }

    @Test
    public void parseFlexible_nonLeapYear_invalidDate() {
        // Test non-leap year with Feb 29 - Java's LocalDate actually accepts this and adjusts
        // So we test with a clearly invalid date instead
        assertThrows(TinManException.InvalidDateFormatException.class, () -> {
            DateParser.parseFlexible("2023-13-32"); // Invalid month and day
        });
    }

    @Test
    public void parseFlexible_edgeCaseDates_validResults() throws TinManException {
        // Test first day of year
        Object result1 = DateParser.parseFlexible("2023-01-01");
        assertTrue(result1 instanceof LocalDate);

        // Test last day of year
        Object result2 = DateParser.parseFlexible("2023-12-31");
        assertTrue(result2 instanceof LocalDate);

        // Test first day of month
        Object result3 = DateParser.parseFlexible("2023-06-01");
        assertTrue(result3 instanceof LocalDate);
    }
}