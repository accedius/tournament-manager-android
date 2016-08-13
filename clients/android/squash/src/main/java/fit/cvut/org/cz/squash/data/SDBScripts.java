package fit.cvut.org.cz.squash.data;

import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Thia class contains scripts to create db tables specific for this modules
 * Created by Vaclav on 19. 4. 2016.
 */
public class SDBScripts {

    public static final String CREATE_TABLE_POINT_CONFIG = "create table " + SDBConstants.tPOINT_CONFIG + " ("
            + DBConstants.cTOURNAMENT_ID + " INTEGER PRIMARY KEY, "
            + SDBConstants.cWIN + " INTEGER NOT NULL, "
            + SDBConstants.cDRAW + " INTEGER NOT NULL, "
            + SDBConstants.cLOSS + " INTEGER NOT NULL, "
            + "FOREIGN KEY ( " + DBConstants.cTOURNAMENT_ID + " ) REFERENCES " + DBConstants.tTOURNAMENTS + " ( " + DBConstants.cID + " ));";

    public static final String CREATE_TABLE_STATS_ENUM = "create table " + SDBConstants.tSTATS_ENUM + " ("
            + SDBConstants.cTYPE + " TEXT PRIMARY KEY  );";

    public static final String CREATE_TABLE_STATS = "create table " + SDBConstants.tSTATS + " ("
            + DBConstants.cID + " INTEGER PRIMARY KEY, "
            + DBConstants.cPLAYER_ID + " INTEGER , "
            + DBConstants.cCOMPETITIONID + " INTEGER NOT NULL, "
            + DBConstants.cPARTICIPANT_ID + " INTEGER , "
            + DBConstants.cTOURNAMENT_ID + " INTEGER NOT NULL, "
            + SDBConstants.cTYPE + " TEXT NOT NULL, "
            + SDBConstants.cSTATUS + " INTEGER NOT NULL, "
            + SDBConstants.cLOSTVALUE + " INTEGER, "
            + SDBConstants.cVALUE + " INTEGER, "
            + "FOREIGN KEY ( " + DBConstants.cTOURNAMENT_ID + " ) REFERENCES " + DBConstants.tTOURNAMENTS + " ( " + DBConstants.cID + " ) "
            + "FOREIGN KEY ( " + DBConstants.cCOMPETITIONID + " ) REFERENCES " + DBConstants.tCOMPETITIONS + " ( " + DBConstants.cID + " ) "
            + "FOREIGN KEY ( " + DBConstants.cPARTICIPANT_ID + " ) REFERENCES " + DBConstants.tPARTICIPANTS + " ( " + DBConstants.cID + " ) "
            + "FOREIGN KEY ( " + SDBConstants.cTYPE + " ) REFERENCES " + SDBConstants.tSTATS_ENUM + " ( " + SDBConstants.cTYPE + " ));";

    public static final String INSERT_POINT_CONFIG = "insert into " + SDBConstants.tPOINT_CONFIG + " values('1', '2', '1', '0');";
    public static final String INSERT_POINT_CONFIG_1 = "insert into " + SDBConstants.tPOINT_CONFIG + " values('2', '2', '1', '0');";
    public static final String INSERT_POINT_CONFIG_2 = "insert into " + SDBConstants.tPOINT_CONFIG + " values('3', '2', '1', '0');";

}
