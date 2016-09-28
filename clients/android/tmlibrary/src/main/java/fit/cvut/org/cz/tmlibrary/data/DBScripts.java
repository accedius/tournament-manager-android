package fit.cvut.org.cz.tmlibrary.data;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class DBScripts {
    public static String CREATE_TABLE_PLAYERS = "create table " + DBConstants.tPLAYERS + " ("
            + DBConstants.cID + " INTEGER PRIMARY KEY, "
            + DBConstants.cNAME + " TEXT, "
            + DBConstants.cEMAIL + " TEXT, "
            + DBConstants.cNOTE  + " TEXT, "
            + DBConstants.cUID + " TEXT, "
            + DBConstants.cETAG + " TEXT, "
            + DBConstants.cLASTMODIFIED + " TEXT, "
            + DBConstants.cLASTSYNCHRONIZED + " TEXT);";

    public static final String CREATE_TABLE_COMPETITIONS = "create table " + DBConstants.tCOMPETITIONS + " ("
            + DBConstants.cID + " INTEGER PRIMARY KEY, "
            + DBConstants.cNAME + " TEXT NOT NULL, "
            + DBConstants.cSTART + " TEXT, "
            + DBConstants.cEND + " TEXT, "
            + DBConstants.cTYPE + " INTEGER, "
            + DBConstants.cNOTE + " TEXT, "
            + DBConstants.cUID + " TEXT, "
            + DBConstants.cETAG + " TEXT, "
            + DBConstants.cLASTMODIFIED + " TEXT, "
            + DBConstants.cLASTSYNCHRONIZED + " TEXT);";

    public static final String CREATE_TABLE_TOURNAMENTS = "create table " + DBConstants.tTOURNAMENTS + " ("
            + DBConstants.cID + " INTEGER PRIMARY KEY, "
            + DBConstants.cNAME + " TEXT NOT NULL, "
            + DBConstants.cSTART + " TEXT, "
            + DBConstants.cEND + " TEXT, "
            + DBConstants.cNOTE + " TEXT, "
            + DBConstants.cUID + " TEXT, "
            + DBConstants.cETAG + " TEXT, "
            + DBConstants.cLASTMODIFIED + " TEXT, "
            + DBConstants.cLASTSYNCHRONIZED + " TEXT, "
            + DBConstants.cCOMPETITIONID + " INTEGER NOT NULL, "
            + "FOREIGN KEY (" + DBConstants.cCOMPETITIONID + ") REFERENCES " + DBConstants.tCOMPETITIONS + " (" + DBConstants.cID + "));";

    public static final String CREATE_TABLE_PLAYERS_IN_COMPETITION = "create table " + DBConstants.tPLAYERS_IN_COMPETITION + " ("
            + DBConstants.cCOMPETITIONID + " INTEGER NOT NULL, "
            + DBConstants.cPLAYER_ID + " INTEGER NOT NULL, "
            + "PRIMARY KEY (" + DBConstants.cCOMPETITIONID + ", " + DBConstants.cPLAYER_ID + ") "
            + "FOREIGN KEY (" + DBConstants.cCOMPETITIONID + ") REFERENCES " + DBConstants.tCOMPETITIONS + " (" + DBConstants.cID + "));";

    public static final String CREATE_TABLE_PLAYERS_IN_TOURNAMENT = "create table " + DBConstants.tPLAYERS_IN_TOURNAMENT + " ("
            + DBConstants.cTOURNAMENT_ID + " INTEGER NOT NULL, "
            + DBConstants.cPLAYER_ID + " INTEGER NOT NULL, "
            + "PRIMARY KEY (" + DBConstants.cTOURNAMENT_ID + ", " + DBConstants.cPLAYER_ID + ") "
            + "FOREIGN KEY (" + DBConstants.cTOURNAMENT_ID + ") REFERENCES " + DBConstants.tTOURNAMENTS + " (" + DBConstants.cID + "));";

    public static final String CREATE_TABLE_TEAMS = "create table " + DBConstants.tTEAMS + " ("
            + DBConstants.cID + " INTEGER PRIMARY KEY, "
            + DBConstants.cUID + " TEXT, "
            + DBConstants.cETAG + " TEXT, "
            + DBConstants.cLASTMODIFIED + " TEXT, "
            + DBConstants.cLASTSYNCHRONIZED + " TEXT, "
            + DBConstants.cNAME + " TEXT NOT NULL, "
            + DBConstants.cTOURNAMENT_ID + " INTEGER NOT NULL, "
            + "FOREIGN KEY (" + DBConstants.cTOURNAMENT_ID + ") REFERENCES " + DBConstants.tTOURNAMENTS + " (" + DBConstants.cID + "));";

    public static final String CREATE_TABLE_PLAYERS_IN_TEAM = "create table " + DBConstants.tPLAYERS_IN_TEAM + " ("
            + DBConstants.cTEAM_ID + " INTEGER NOT NULL, "
            + DBConstants.cPLAYER_ID + " INTEGER NOT NULL, "
            + "PRIMARY KEY (" + DBConstants.cTEAM_ID + ", " + DBConstants.cPLAYER_ID + ") "
            + "FOREIGN KEY (" + DBConstants.cTEAM_ID + ") REFERENCES " + DBConstants.tTEAMS + " (" + DBConstants.cID + "));";

    public static final String CREATE_TABLE_MATCHES = "create table " + DBConstants.tMATCHES + " ("
            + DBConstants.cID + " INTEGER PRIMARY KEY, "
            + DBConstants.cUID + " TEXT, "
            + DBConstants.cETAG + " TEXT, "
            + DBConstants.cLASTMODIFIED + " TEXT, "
            + DBConstants.cLASTSYNCHRONIZED + " TEXT, "
            + DBConstants.cDATE + " TEXT, "
            + DBConstants.cNOTE + " TEXT, "
            + DBConstants.cPLAYED + " INTEGER, "
            + DBConstants.cROUND + " INTEGER NOT NULL, "
            + DBConstants.cPERIOD + " INTEGER NOT NULL, "
            + DBConstants.cTOURNAMENT_ID + " INTEGER NOT NULL, "
            + "FOREIGN KEY (" + DBConstants.cTOURNAMENT_ID + ") REFERENCES " + DBConstants.tTOURNAMENTS + " (" + DBConstants.cID + "));";

    public static final String CREATE_TABLE_PARTICIPANTS = "create table " + DBConstants.tPARTICIPANTS + " ("
            + DBConstants.cID + " INTEGER PRIMARY KEY, "
            + DBConstants.cUID + " TEXT, "
            + DBConstants.cETAG + " TEXT, "
            + DBConstants.cLASTMODIFIED + " TEXT, "
            + DBConstants.cLASTSYNCHRONIZED + " TEXT, "
            + DBConstants.cTEAM_ID + " INTEGER, "
            + DBConstants.cROLE + " TEXT, "
            + DBConstants.cMATCH_ID + " INTEGER NOT NULL, "
            + "FOREIGN KEY (" + DBConstants.cTEAM_ID + ") REFERENCES " + DBConstants.tTEAMS + " (" + DBConstants.cID + ") "
            + "FOREIGN KEY (" + DBConstants.cMATCH_ID + ") REFERENCES " + DBConstants.tMATCHES + " (" + DBConstants.cID + "));";

    public static final String CREATE_TABLE_STATS = "create table " + DBConstants.tSTATS + " ("
            + DBConstants.cID + " INTEGER PRIMARY KEY, "
            + DBConstants.cPARTICIPANT_ID + " INTEGER NOT NULL, "
            + DBConstants.cSTATS_ENUM_ID + " TEXT, "
            + DBConstants.cPLAYER_ID + " INTEGER, "
            + DBConstants.cTOURNAMENT_ID + " INTEGER NOT NULL, "
            + DBConstants.cCOMPETITIONID + " INTEGER NOT NULL, "
            + DBConstants.cVALUE + " TEXT, "
            + "FOREIGN KEY (" + DBConstants.cTOURNAMENT_ID + ") REFERENCES " + DBConstants.tTOURNAMENTS + " (" + DBConstants.cID + ") "
            + "FOREIGN KEY (" + DBConstants.cCOMPETITIONID + ") REFERENCES " + DBConstants.tCOMPETITIONS + " (" + DBConstants.cID + "));";

    public static final String INSERT_INTO_PLAYERS =
            String.format("insert into %s VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                    DBConstants.tPLAYERS, 1, "Martin", "martin@seznam.cz", "", "abcde", "efgh", "2016-04-04 10:12:14", "2016-04-04 10:12:14");
    public static final String INSERT_INTO_PLAYERS_1 =
            String.format("insert into %s VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                    DBConstants.tPLAYERS, 2, "Aleš", "ales@valenta.cz", "", "abcde", "efgh", "2016-04-04 10:12:14", "2016-04-04 10:12:14");
    public static final String INSERT_INTO_PLAYERS_2 =
            String.format("insert into %s VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                    DBConstants.tPLAYERS, 3, "Jarda", "jarda@frk.cz", "", "abcde", "efgh", "2016-04-04 10:12:14", "2016-04-04 10:12:14");
    public static final String INSERT_INTO_PLAYERS_3 =
            String.format("insert into %s VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                    DBConstants.tPLAYERS, 4, "Pavel", "pavel@frk.cz", "", "abcde", "efgh", "2016-04-04 10:12:14", "2016-04-04 10:12:14");
    public static final String INSERT_INTO_PLAYERS_4 =
            String.format("insert into %s VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                    DBConstants.tPLAYERS, 5, "Honza", "honza@frk.cz", "", "abcde", "efgh", "2016-04-04 10:12:14", "2016-04-04 10:12:14");
    public static final String INSERT_INTO_PLAYERS_5 =
            String.format("insert into %s VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                    DBConstants.tPLAYERS, 6, "Zdeněk", "zdenda@frk.cz", "", "abcde", "efgh", "2016-04-04 10:12:14", "2016-04-04 10:12:14");
    public static final String INSERT_INTO_PLAYERS_6 =
            String.format("insert into %s VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                    DBConstants.tPLAYERS, 7, "Tomáš", "tomnas@frk.cz", "", "abcde", "efgh", "2016-04-04 10:12:14", "2016-04-04 10:12:14");
    public static final String INSERT_INTO_PLAYERS_7 =
            String.format("insert into %s VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                    DBConstants.tPLAYERS, 8, "Radek", "Radek@frk.cz", "", "abcde", "efgh", "2016-04-04 10:12:14", "2016-04-04 10:12:14");

    public static final String INSERT_SQUASH_COMPETITIONS = "insert into " + DBConstants.tCOMPETITIONS + " values('1','SQ T CMP 1','2001-01-01','2001-12-12','1','Pozn1','asdf','lkjh','2002-01-01 10:10:10','2002-01-01 10:10:10');";
    public static final String INSERT_SQUASH_COMPETITIONS_1 = "insert into " + DBConstants.tCOMPETITIONS + " values('2','SQ I CMP 2','2002-01-01','2002-12-12','0','Pozn2','asdf','lkjh','2003-01-01 10:10:10','2003-01-01 10:10:10');";
    public static final String INSERT_SQUASH_COMPETITIONS_2 = "insert into " + DBConstants.tCOMPETITIONS + " values('3','SQ I CMP 3','2003-01-01','2003-12-12','0','Pozn3','asdf','lkjh','2004-01-01 10:10:10','2004-01-01 10:10:10');";

    public static final String INSERT_SQUASH_TOURNAMENTS = "insert into " + DBConstants.tTOURNAMENTS + " values('1','SQ TOUR 1','2001-01-01','2001-12-12','PoznTour1','asdf','lkjh','2002-01-01 10:10:10','2002-01-01 10:10:10', '1');";
    public static final String INSERT_SQUASH_TOURNAMENTS_1 = "insert into " + DBConstants.tTOURNAMENTS+ " values('2','SQ TOUR 2','2001-01-01','2001-12-12','PoznTour2','asdf','lkjh','2002-01-01 10:10:10','2002-01-01 10:10:10', '1');";
    public static final String INSERT_SQUASH_TOURNAMENTS_2 = "insert into " + DBConstants.tTOURNAMENTS + " values('3','SQ TOUR 3','2002-01-01','2002-12-12','PoznTour3','asdf','lkjh','2003-01-01 10:10:10','2003-01-01 10:10:10', '2');";

    public static final String INSERT_HOCKEY_COMPETITIONS = "insert into " + DBConstants.tCOMPETITIONS + " values('1','H CMP 1','2001-01-01','2001-12-12','1','Pozn1','asdf','lkjh','2002-01-01 10:10:10','2002-01-01 10:10:10');";
    public static final String INSERT_HOCKEY_COMPETITIONS_1 = "insert into " + DBConstants.tCOMPETITIONS + " values('2','H CMP 2','2002-01-01','2002-12-12','1','Pozn2','asdf','lkjh','2003-01-01 10:10:10','2003-01-01 10:10:10');";
    public static final String INSERT_HOCKEY_COMPETITIONS_2 = "insert into " + DBConstants.tCOMPETITIONS + " values('3','H CMP 3','2003-01-01','2003-12-12','1','Pozn3','asdf','lkjh','2004-01-01 10:10:10','2004-01-01 10:10:10');";

    public static final String INSERT_HOCKEY_TOURNAMENTS = "insert into " + DBConstants.tTOURNAMENTS + " values('1','H TOUR 1','2001-01-01','2001-12-12','PoznTour1','asdf','lkjh','2002-01-01 10:10:10','2002-01-01 10:10:10', '1');";
    public static final String INSERT_HOCKEY_TOURNAMENTS_1 = "insert into " + DBConstants.tTOURNAMENTS + " values('2','H TOUR 2','2001-01-01','2001-12-12','PoznTour2','asdf','lkjh','2002-01-01 10:10:10','2002-01-01 10:10:10', '1');";
    public static final String INSERT_HOCKEY_TOURNAMENTS_2 = "insert into " + DBConstants.tTOURNAMENTS + " values('3','H TOUR 3','2002-01-01','2002-12-12','PoznTour3','asdf','lkjh','2003-01-01 10:10:10','2003-01-01 10:10:10', '2');";

    public static final String INSERT_PLAYER_COMPETITION = "insert into " + DBConstants.tPLAYERS_IN_COMPETITION + " values('1', '1');";
    public static final String INSERT_PLAYER_COMPETITION_1 = "insert into " + DBConstants.tPLAYERS_IN_COMPETITION + " values('1', '2');";
    public static final String INSERT_PLAYER_COMPETITION_2 = "insert into " + DBConstants.tPLAYERS_IN_COMPETITION + " values('1', '3');";
    public static final String INSERT_PLAYER_COMPETITION_3 = "insert into " + DBConstants.tPLAYERS_IN_COMPETITION + " values('1', '4');";
    public static final String INSERT_PLAYER_COMPETITION_4 = "insert into " + DBConstants.tPLAYERS_IN_COMPETITION + " values('1', '5');";
    public static final String INSERT_PLAYER_COMPETITION_5 = "insert into " + DBConstants.tPLAYERS_IN_COMPETITION + " values('1', '6');";
    public static final String INSERT_PLAYER_COMPETITION_6 = "insert into " + DBConstants.tPLAYERS_IN_COMPETITION + " values('1', '7');";
    public static final String INSERT_PLAYER_COMPETITION_7 = "insert into " + DBConstants.tPLAYERS_IN_COMPETITION + " values('1', '8');";

    public static final String INSERT_PLAYER_TOURNAMENT = "insert into " + DBConstants.tPLAYERS_IN_TOURNAMENT + " values('1', '1');";
    public static final String INSERT_PLAYER_TOURNAMENT_1 = "insert into " + DBConstants.tPLAYERS_IN_TOURNAMENT + " values('1', '2');";
    public static final String INSERT_PLAYER_TOURNAMENT_2 = "insert into " + DBConstants.tPLAYERS_IN_TOURNAMENT + " values('1', '3');";
    public static final String INSERT_PLAYER_TOURNAMENT_3 = "insert into " + DBConstants.tPLAYERS_IN_TOURNAMENT + " values('1', '4');";
    public static final String INSERT_PLAYER_TOURNAMENT_4 = "insert into " + DBConstants.tPLAYERS_IN_TOURNAMENT + " values('1', '5');";
    public static final String INSERT_PLAYER_TOURNAMENT_5 = "insert into " + DBConstants.tPLAYERS_IN_TOURNAMENT + " values('1', '6');";
    public static final String INSERT_PLAYER_TOURNAMENT_6 = "insert into " + DBConstants.tPLAYERS_IN_TOURNAMENT + " values('1', '7');";
    public static final String INSERT_PLAYER_TOURNAMENT_7 = "insert into " + DBConstants.tPLAYERS_IN_TOURNAMENT + " values('1', '8');";

    public static final String INSERT_TOURNAMENT_TEAMS = "insert into " + DBConstants.tTEAMS + " values('1', 'asdf', 'lkjh', '2010-01-01 10:10:10', '2010-01-01 10:10:10', 'A Team', '1');";
    public static final String INSERT_TOURNAMENT_TEAMS_1 = "insert into " + DBConstants.tTEAMS + " values('2', 'asdf', 'lkjh', '2010-01-01 10:10:10', '2010-01-01 10:10:10', 'B Team', '1');";
    public static final String INSERT_TOURNAMENT_TEAMS_2 = "insert into " + DBConstants.tTEAMS + " values('3', 'asdf', 'lkjh', '2010-01-01 10:10:10', '2010-01-01 10:10:10', 'C Team', '1');";
    public static final String INSERT_TOURNAMENT_TEAMS_3 = "insert into " + DBConstants.tTEAMS + " values('4', 'asdf', 'lkjh', '2010-01-01 10:10:10', '2010-01-01 10:10:10', 'D Team', '1');";

    public static final String INSERT_PLAYER_TEAMS = "insert into " + DBConstants.tPLAYERS_IN_TEAM + " values('1', '1');";
    public static final String INSERT_PLAYER_TEAMS_1 = "insert into " + DBConstants.tPLAYERS_IN_TEAM + " values('1', '2');";
    public static final String INSERT_PLAYER_TEAMS_2 = "insert into " + DBConstants.tPLAYERS_IN_TEAM + " values('2', '3');";
    public static final String INSERT_PLAYER_TEAMS_3 = "insert into " + DBConstants.tPLAYERS_IN_TEAM + " values('2', '4');";
    public static final String INSERT_PLAYER_TEAMS_4 = "insert into " + DBConstants.tPLAYERS_IN_TEAM + " values('3', '5');";
    public static final String INSERT_PLAYER_TEAMS_5 = "insert into " + DBConstants.tPLAYERS_IN_TEAM + " values('3', '6');";
    public static final String INSERT_PLAYER_TEAMS_6 = "insert into " + DBConstants.tPLAYERS_IN_TEAM + " values('4', '7');";
    public static final String INSERT_PLAYER_TEAMS_7 = "insert into " + DBConstants.tPLAYERS_IN_TEAM + " values('4', '8');";
}
