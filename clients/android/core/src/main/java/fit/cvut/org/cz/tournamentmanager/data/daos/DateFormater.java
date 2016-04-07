package fit.cvut.org.cz.tournamentmanager.data.daos;

import java.text.SimpleDateFormat;

/**
 * Created by kevin on 4. 4. 2016.
 */

// TODO maybe move to tmlibrary?
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
