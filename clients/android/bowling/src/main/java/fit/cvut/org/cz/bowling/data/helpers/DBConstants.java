package fit.cvut.org.cz.bowling.data.helpers;

/**
 * Resource strings for database communication
 */
public class DBConstants extends fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants {
    // t prefix stands for table
    public static final String tCONFIGURATIONS = "configurations";

    //c prefix stands for column
    public static final String cNTW = "normal_time_win";
    public static final String cNTD = "normal_time_draw";
    public static final String cNTL = "normal_time_loss";
    public static final String cOTW = "overtime_win";
    public static final String cOTD = "overtime_draw";
    public static final String cOTL = "overtime_loss";
    public static final String cSOW = "shootout_win";
    public static final String cSOL = "shootout_loss";

    public static final String cGOALS = "goals";
    public static final String cASSISTS = "assists";
    public static final String cPLUS_MINUS = "plus_minus";
    public static final String cSAVES = "saves";

    public static final String cSCORE = "score";
    public static final String cSHOOTOUTS = "shootouts";
    public static final String cOVERTIME = "overtime";

    public static final String cSIDES_NUMBER = "sides_number";
}
