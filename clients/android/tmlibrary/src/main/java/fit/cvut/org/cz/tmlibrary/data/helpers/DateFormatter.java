package fit.cvut.org.cz.tmlibrary.data.helpers;

import java.text.SimpleDateFormat;

/**
 * Helper class for Date formatting.
 */
public class DateFormatter {
    private static DateFormatter ourInstance = new DateFormatter();

    /**
     * Get formatter instance.
     * @return singleton instance.
     */
    public static DateFormatter getInstance() {
        return ourInstance;
    }

    /**
     * Get date format for displaying.
     * @return date format
     */
    public SimpleDateFormat getDisplayDateFormat() { return new SimpleDateFormat("dd. MM. yyyy");}

    /**
     * Get database date format.
     * @return date format
     */
    public SimpleDateFormat getDBDateFormat() { return new SimpleDateFormat("yyyy-MM-dd");}

    /**
     * Get database date and time format.
     * @return date time format
     */
    public SimpleDateFormat getDBDateTimeFormat() { return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");}

    private DateFormatter() {
    }
}
