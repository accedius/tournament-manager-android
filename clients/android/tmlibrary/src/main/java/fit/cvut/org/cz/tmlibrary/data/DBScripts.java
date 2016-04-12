package fit.cvut.org.cz.tmlibrary.data;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class DBScripts {
    public static final String CREATE_TABLE_PLAYERS =
            String.format("create table %s ( %s INTEGER PRIMARY KEY, %s TEXT, %s  TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
                    DBConstants.tPLAYERS, DBConstants.cID, DBConstants.cNAME, DBConstants.cEMAIL, DBConstants.cNOTE, DBConstants.cUID, DBConstants.cETAG, DBConstants.cLASTMODIFIED);

    public static final String CREATE_TABLE_COMPETITIONS =
            String.format("create table %s ( %s INTEGER PRIMARY KEY, %s TEXT, %s  TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
                    DBConstants.tCOMPETITIONS, DBConstants.cID, DBConstants.cNAME, DBConstants.cSTART, DBConstants.cEND, DBConstants.cTYPE, DBConstants.cNOTE, DBConstants.cUID, DBConstants.cETAG, DBConstants.cLASTMODIFIED);




    public static final String CREATE_TABLE_TOURNAMENTS = "create table " + DBConstants.tTOURNAMENTS + " ("
            + DBConstants.cID + " INTEGER PRIMARY KEY, "
            + DBConstants.cNAME + " TEXT NOT NULL, "
            + DBConstants.cSTART + " TEXT, "
            + DBConstants.cEND + " TEXT, "
            + DBConstants.cNOTE + " TEXT, "
            + DBConstants.cUID + " TEXT, "
            + DBConstants.cETAG + " TEXT, "
            + DBConstants.cLASTMODIFIED + " TEXT, "
            + DBConstants.cCOMPETITIONID + " INTEGER NOT NULL, "
            + "FOREIGN KEY ( " + DBConstants.cCOMPETITIONID + " ) REFERENCES " + DBConstants.tCOMPETITIONS + " ( " + DBConstants.cID + " ));";


    public static final String CREATE_TABLE_PLAYERS_IN_COMPETITION = "create table " + DBConstants.tPLAYERS_IN_COMPETITION + " ("
            + DBConstants.cCOMPETITIONID + " INTEGER NOT NULL, "
            + DBConstants.cPLAYER_ID + " INTEGER NOT NULL, "
            + "PRIMARY KEY ( " + DBConstants.cCOMPETITIONID + ", " + DBConstants.cPLAYER_ID + " ) "
            + "FOREIGN KEY ( " + DBConstants.cCOMPETITIONID + " ) REFERENCES " + DBConstants.tCOMPETITIONS + " ( " + DBConstants.cID + " ));";

    public static final String CREATE_TABLE_PLAYERS_IN_TOURNAMENT = "create table " + DBConstants.tPLAYERS_IN_TOURNAMENT + " ("
            + DBConstants.cTOURNAMENT_ID + " INTEGER NOT NULL, "
            + DBConstants.cPLAYER_ID + " INTEGER NOT NULL, "
            + "PRIMARY KEY ( " + DBConstants.cTOURNAMENT_ID + ", " + DBConstants.cPLAYER_ID + " ) "
            + "FOREIGN KEY ( " + DBConstants.cTOURNAMENT_ID + " ) REFERENCES " + DBConstants.tTOURNAMENTS + " ( " + DBConstants.cID + " ));";


















    public static final String INSERT_INTO_COMPETITIONS =
            String.format("insert into %s VALUES( '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                    DBConstants.tCOMPETITIONS, 14, "41212", "WC in Squash", "2017-01-01", "2017-01-21", "single", "---");
    public static final String INSERT_INTO_COMPETITIONS_1 =
            String.format("insert into %s VALUES( '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                    DBConstants.tCOMPETITIONS, 22, "90833", "EURO squash", "2017-06-01", "2017-07-01", "team", "---");
    public static final String INSERT_INTO_COMPETITIONS_2 =
            String.format("insert into %s VALUES( '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                    DBConstants.tCOMPETITIONS, 33, "18742", "Singles", "2016-01-01", "2016-02-29", "single", "no, its team :D");

    public static final String INSERT_INTO_COMPETITIONS_H =
            String.format("insert into %s VALUES( '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                    DBConstants.tCOMPETITIONS, 14, "41212", "Euro Hockey Tour", "2017-01-01", "2017-01-21", "team", "---");
    public static final String INSERT_INTO_COMPETITIONS_H1 =
            String.format("insert into %s VALUES( '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                    DBConstants.tCOMPETITIONS, 22, "90833", "IIHF WC 2015", "2017-06-01", "2017-07-01", "team", "---");
    public static final String INSERT_INTO_COMPETITIONS_H2 =
            String.format("insert into %s VALUES( '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                    DBConstants.tCOMPETITIONS, 33, "18742", "NHL", "2016-01-01", "2016-02-29", "team", "no, its team :D");

    public static final String INSERT_INTO_PLAYERS =
            String.format("insert into %s VALUES( '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                    DBConstants.tPLAYERS, 1, "Martin Hovorka", "martin@seznam.cz", "", "abcde", "efgh", "2016-04-04");
    public static final String INSERT_INTO_PLAYERS_1 =
            String.format("insert into %s VALUES( '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                    DBConstants.tPLAYERS, 2, "Aleš Valenta", "ales@valenta.cz", "", "abcde", "efgh", "2016-04-04");
    public static final String INSERT_INTO_PLAYERS_2 =
            String.format("insert into %s VALUES( '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                    DBConstants.tPLAYERS, 3, "Jaroslav Frk", "jarda@frk.cz", "", "abcde", "efgh", "2016-04-04");
}
