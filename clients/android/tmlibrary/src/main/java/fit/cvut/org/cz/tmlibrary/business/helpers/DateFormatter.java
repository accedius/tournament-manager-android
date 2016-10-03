package fit.cvut.org.cz.tmlibrary.business.helpers;

import java.text.SimpleDateFormat;

/**
 * Created by kevin on 4. 4. 2016.
 */

public class DateFormatter {
    private static DateFormatter ourInstance = new DateFormatter();

    public static DateFormatter getInstance() {
        return ourInstance;
    }

    public SimpleDateFormat getDisplayDateFormat() { return new SimpleDateFormat("dd. MM. yyyy");}
    public SimpleDateFormat getDBDateFormat() { return new SimpleDateFormat("yyyy-MM-dd");}
    public SimpleDateFormat getDBDateTimeFormat() { return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");}

    private DateFormatter() {
    }
}
