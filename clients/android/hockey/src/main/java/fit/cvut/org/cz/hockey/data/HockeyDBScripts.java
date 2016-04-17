package fit.cvut.org.cz.hockey.data;

import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by atgot_000 on 11. 4. 2016.
 */
public class HockeyDBScripts {

    public static final String CREATE_TABLE_CONFIGURATIONS = "create table " + HockeyDBConstants.tCONFIGURATIONS + " ("
            + DBConstants.cID + " INTEGER PRIMARY KEY, "
            + HockeyDBConstants.cNTW + " INTEGER NOT NULL, "
            + HockeyDBConstants.cNTD + " INTEGER NOT NULL, "
            + HockeyDBConstants.cNTL + " INTEGER NOT NULL, "
            + HockeyDBConstants.cOTW + " INTEGER NOT NULL, "
            + HockeyDBConstants.cOTD + " INTEGER NOT NULL, "
            + HockeyDBConstants.cOTL + " INTEGER NOT NULL, "
            + HockeyDBConstants.cSOW + " INTEGER NOT NULL, "
            + HockeyDBConstants.cSOL + " INTEGER NOT NULL, "
            + HockeyDBConstants.cTOURNAMENTID + " INTEGER NOT NULL, "
            + "FOREIGN KEY ( " + HockeyDBConstants.cTOURNAMENTID + " ) REFERENCES " + DBConstants.tTOURNAMENTS + " ( " + DBConstants.cID + " ));";

    public static final String CREATE_TABLE_PARTICIPANT_SCORE = "create table " + HockeyDBConstants.tPARTICIPANT_SCORE + " ("
            + DBConstants.cPARTICIPANT_ID + " INTEGER PRIMARY KEY, "
            + HockeyDBConstants.cSCORE + " INTEGER, "
            + "FOREIGN KEY ( " + DBConstants.cPARTICIPANT_ID + " ) REFERENCES " + DBConstants.tPARTICIPANTS + " ( " + DBConstants.cID + " ));";
}
