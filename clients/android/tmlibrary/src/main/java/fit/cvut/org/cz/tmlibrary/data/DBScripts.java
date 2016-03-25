package fit.cvut.org.cz.tmlibrary.data;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class DBScripts {

    public static final String CREATE_TABLE_COMPETITIONS =
            String.format("create table %s ( %s INTEGER PRIMARY KEY, %s  TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT );",
                    DBConstants.tCOMPETITIONS, DBConstants.CID, DBConstants.CNAME, DBConstants.cSTART, DBConstants.cEND, DBConstants.cTYPE, DBConstants.cNOTE);
}
