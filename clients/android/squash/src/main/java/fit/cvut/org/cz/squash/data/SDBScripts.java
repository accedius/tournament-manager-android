package fit.cvut.org.cz.squash.data;

import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by Vaclav on 19. 4. 2016.
 */
public class SDBScripts {

    public static final String CREATE_TABLE_POINT_CONFIG = "create table " + SDBConstants.tPOINT_CONFIG + " ("
            + DBConstants.cTOURNAMENT_ID + " INTEGER PRIMARY KEY, "
            + SDBConstants.cWIN + " INTEGER NOT NULL, "
            + SDBConstants.cDRAW + " INTEGER NOT NULL, "
            + SDBConstants.cLOSS + " INTEGER NOT NULL, "
            + "FOREIGN KEY ( " + DBConstants.cTOURNAMENT_ID + " ) REFERENCES " + DBConstants.tTOURNAMENTS + " ( " + DBConstants.cID + " ));";
}
