package fit.cvut.org.cz.hockey.data;

/**
 * Created by atgot_000 on 11. 4. 2016.
 *
 * static class with database constants for hockey
 */
public class HockeyDBConstants {
    // t prefix stands for table
    public static final String tCONFIGURATIONS = "configurations";
    public static final String tPARTICIPANT_SCORE = "participants_score";
    public static final String tMATCH_SCORE = "match_score";

    //c prefix stands for column
    public static final String cID = "_id";
    public static final String cTOURNAMENTID = "tournament_id";
    public static final String cNTW = "normal_time_win";
    public static final String cNTD = "normal_time_draw";
    public static final String cNTL = "normal_time_loss";
    public static final String cOTW = "overtime_win";
    public static final String cOTD = "overtime_draw";
    public static final String cOTL = "overtime_loss";
    public static final String cSOW = "shootout_win";
    public static final String cSOL = "shootout_loss";
    public static final String cSCORE = "score";
    public static final String cSHOOTOUTS = "shootouts";
    public static final String cOVERTIME = "overtime";
}
