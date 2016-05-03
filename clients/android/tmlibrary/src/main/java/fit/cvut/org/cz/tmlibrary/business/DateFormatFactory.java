package fit.cvut.org.cz.tmlibrary.business;

import java.text.SimpleDateFormat;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public class DateFormatFactory {
    private static DateFormatFactory ourInstance = new DateFormatFactory();

    public static DateFormatFactory getInstance() {
        return ourInstance;
    }

    private DateFormatFactory() {
    }

    public SimpleDateFormat getDateFormat(){return new SimpleDateFormat("yyyy-MM-dd");}
    public SimpleDateFormat getDateTimeFormat(){ return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");}
}
