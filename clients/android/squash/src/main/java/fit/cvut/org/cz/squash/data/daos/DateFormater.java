package fit.cvut.org.cz.squash.data.daos;

import java.text.SimpleDateFormat;

/**
 * Created by Vaclav on 4. 4. 2016.
 */
public class DateFormater {
    private static DateFormater ourInstance = new DateFormater();

    public static DateFormater getInstance() {
        return ourInstance;
    }

    public SimpleDateFormat getFormat(){ return new SimpleDateFormat("yyyy-MM-dd");}
    public SimpleDateFormat getDateTimeFormat(){ return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");}

    private DateFormater() {
    }
}
