package fit.cvut.org.cz.squash.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.HashMap;

import fit.cvut.org.cz.tmlibrary.data.helpers.CPConstants;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

/**
 * This provider allows core to get hold of data about competitions
 * only query is implemented
 * Created by Vaclav on 25. 3. 2016.
 */
public class CompetitionCP extends ContentProvider {
    public static final String AUTHORITY = "fit.cvut.org.cz.squash.data";
    public static final String BADMINTON_NAME = "Badminton";
    public static final String BEACH_NAME = "Beach";
    public static final String SQUASH_NAME = "Squash";
    public static final String TENNIS_NAME = "Tennis";
    public static final String VOLLEYBALL_NAME = "Volleyball";

    private SquashDAOFactory helper;

    private static final int COMPETITION_ONE = 1;
    private static final int EMPTY_COMPETITION_ONE = 2;
    private static final int COMPETITIONS_ALL = 3;
    private static final int COMPETITIONS_BY_PLAYER = 4;
    private static final int DELETE_COMPETITION = 5;

    private static final int SEGMENT_ID = 1;

    private static final int BADMINTON = 100;
    private static final int BEACH = 200;
    private static final int SQUASH = 300;
    private static final int TENNIS = 400;
    private static final int VOLLEYBALL = 500;

    private static final HashMap<String, Integer> sport_contexts = new HashMap<String, Integer>(){{
        put(BADMINTON_NAME, BADMINTON);
        put(BEACH_NAME, BEACH);
        put(SQUASH_NAME, SQUASH);
        put(TENNIS_NAME, TENNIS);
        put(VOLLEYBALL_NAME, VOLLEYBALL);
    }};

    private String getSport(int uriID) {
        if (uriID > VOLLEYBALL)
            return VOLLEYBALL_NAME;
        else if (uriID > TENNIS)
            return TENNIS_NAME;
        else if (uriID > SQUASH)
            return SQUASH_NAME;
        else if (uriID > BEACH)
            return BEACH_NAME;
        else if (uriID > BADMINTON)
            return BADMINTON_NAME;
        else
            return "";
    }

    private static final UriMatcher matcher ;
    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        for (HashMap.Entry<String, Integer> sport : sport_contexts.entrySet()) {
            matcher.addURI(AUTHORITY, sport.getKey()+CPConstants.uCompetitions, COMPETITIONS_ALL + sport.getValue());
            matcher.addURI(AUTHORITY, sport.getKey()+CPConstants.uDeleteCompetition + "#", DELETE_COMPETITION + sport.getValue());
            matcher.addURI(AUTHORITY, sport.getKey()+CPConstants.uEmptyCompetition + "#", EMPTY_COMPETITION_ONE + sport.getValue());
            matcher.addURI(AUTHORITY, sport.getKey()+CPConstants.uCompetitionsByPlayer + "#", COMPETITIONS_BY_PLAYER + sport.getValue());
        }
        matcher.addURI(AUTHORITY, CPConstants.uCompetitions + "#", COMPETITION_ONE);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = matcher.match(uri);
        if (uriType == matcher.NO_MATCH)
            return null;

        String sport = getSport(uriType);
        uriType %= 100;
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        if (uriType == COMPETITIONS_ALL) {
            helper = new SquashDAOFactory(getContext(), sport);
            builder.setTables(DBConstants.tCOMPETITIONS);
        } else if (uriType == COMPETITIONS_BY_PLAYER) {
            helper = new SquashDAOFactory(getContext(), sport);
            String playerID = uri.getPathSegments().get(SEGMENT_ID);
            builder.setTables(
                    DBConstants.tPLAYERS_IN_COMPETITION + " join " + DBConstants.tCOMPETITIONS + " ON " +
                            DBConstants.tPLAYERS_IN_COMPETITION + "." + DBConstants.cCOMPETITION_ID + " = " +
                            DBConstants.tCOMPETITIONS + "." + DBConstants.cID);

            projection = new String[]{DBConstants.tCOMPETITIONS + ".*"};
            selection = DBConstants.tPLAYERS_IN_COMPETITION + "." + DBConstants.cPLAYER_ID + " = " + playerID;
        } else if (uriType == EMPTY_COMPETITION_ONE) {
            helper = new SquashDAOFactory(getContext(), sport);
            String competitionID = uri.getPathSegments().get(SEGMENT_ID);
            builder.setTables(
                    DBConstants.tCOMPETITIONS +
                            " left join " + DBConstants.tTOURNAMENTS + " ON " +
                            DBConstants.tCOMPETITIONS + "." + DBConstants.cID + " = " +
                            DBConstants.tTOURNAMENTS + "." + DBConstants.cCOMPETITION_ID +
                            " left join " + DBConstants.tPLAYERS_IN_COMPETITION + " ON " +
                            DBConstants.tPLAYERS_IN_COMPETITION + "." + DBConstants.cCOMPETITION_ID + " = " +
                            DBConstants.tCOMPETITIONS + "." + DBConstants.cID);

            projection = new String[]{DBConstants.tCOMPETITIONS + ".*"};
            selection = DBConstants.tCOMPETITIONS + "." + DBConstants.cID + " = " + competitionID + " and " +
                    DBConstants.tTOURNAMENTS + "." + DBConstants.cCOMPETITION_ID + " IS NULL and " +
                    DBConstants.tPLAYERS_IN_COMPETITION + "." + DBConstants.cCOMPETITION_ID + " IS NULL";
        }

        Cursor cursor = builder.query(helper.getWritableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = matcher.match(uri);
        String sport = getSport(uriType);
        uriType %= 100;
        if (uriType == DELETE_COMPETITION) {
            helper = new SquashDAOFactory(getContext(), sport);
            String competitionID = uri.getPathSegments().get(SEGMENT_ID);
            String where = DBConstants.tCOMPETITIONS + "." + DBConstants.cID + " = " + competitionID;
            return helper.getWritableDatabase().delete(DBConstants.tCOMPETITIONS, where, null);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
