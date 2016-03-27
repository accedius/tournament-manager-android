package fit.cvut.org.cz.tmlibrary.data;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class DBScripts {

    public static final String CREATE_TABLE_COMPETITIONS =
            String.format("create table %s ( %s INTEGER PRIMARY KEY, %s TEXT, %s  TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT );",
                    DBConstants.tCOMPETITIONS, DBConstants.CID, DBConstants.UID, DBConstants.CNAME, DBConstants.cSTART, DBConstants.cEND, DBConstants.cTYPE, DBConstants.cNOTE);

    public static final String INSERT_INTO_COMPETITIONS =
            String.format("insert into %s VALUES( '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                    DBConstants.tCOMPETITIONS, 14, "41212", "firstname", "2017-01-01", "2017-01-21", "single", "---");
    public static final String INSERT_INTO_COMPETITIONS_1 =
            String.format("insert into %s VALUES( '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                    DBConstants.tCOMPETITIONS, 22, "90833", "lastname", "2017-06-01", "2017-07-01", "team", "---");
    public static final String INSERT_INTO_COMPETITIONS_2 =
            String.format("insert into %s VALUES( '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                    DBConstants.tCOMPETITIONS, 33, "18742", "surname", "2016-01-01", "2016-02-29", "single", "no, its team :D");
}
