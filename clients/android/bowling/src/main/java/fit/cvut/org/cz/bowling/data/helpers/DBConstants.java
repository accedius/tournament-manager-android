package fit.cvut.org.cz.bowling.data.helpers;

import com.j256.ormlite.field.DatabaseField;

/**
 * Resource strings for database communication
 */
public class DBConstants extends fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants {
    // t prefix stands for table
    public static final String tCONFIGURATIONS = "configurations";
    public static final String tMATCH_FRAMES = "match_frames";
    public static final String tMATCH_FRAME_ROLLS = "match_frame_rolls";

    //c prefix stands for column
    public static final String cFRAMES_NUMBER = "frames_number";
    public static final String cFRAME_NUMBER = "frame_number";

    public static final String cSTRIKES = "strikes";
    public static final String cSPARES = "spares";
    public static final String cPOINTS = "points";

    public static final String cSCORE = "score";
    public static final String cFRAME = "frame";
    public static final String cFRAME_ID = "frame_id";
    public static final String cROLL_NUMBER = "roll_number";

    public static final String cSIDES_NUMBER = "sides_number";
    public static final String cPLACE_POINTS = "place_points";

    public static final String cVALID_FOR_STATS = "valid_for_stats";
}
