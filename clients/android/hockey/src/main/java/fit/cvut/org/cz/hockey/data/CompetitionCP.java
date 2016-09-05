package fit.cvut.org.cz.hockey.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import fit.cvut.org.cz.tmlibrary.data.CPConstants;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by Vaclav on 25. 3. 2016.
 *
 * Content provider for Core
 */
public class CompetitionCP extends ContentProvider {

    public static final String AUTHORITY = "fit.cvut.org.cz.hockey.data";

    private HockeyDBHelper helper;

    private static final int COMPETITIONS_ALL = 0;
    private static final int COMPETITION_ONE = 1;
    private static final int DELETE_COMPETITION = 2;
    private static final int EMPTY_COMPETITION_ONE = 3;
    private static final int COMPETITIONS_BY_PLAYER = 4;

    private static final int SEGMENT_ID = 1;

    private static final UriMatcher matcher ;
    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, CPConstants.uCompetitions, COMPETITIONS_ALL);
        matcher.addURI(AUTHORITY, CPConstants.uCompetitions + "#", COMPETITION_ONE);
        matcher.addURI(AUTHORITY, CPConstants.uDeleteCompetition + "#", DELETE_COMPETITION);
        matcher.addURI(AUTHORITY, CPConstants.uEmptyCompetition + "#", EMPTY_COMPETITION_ONE);
        matcher.addURI(AUTHORITY, CPConstants.uCompetitionsByPlayer + "#", COMPETITIONS_BY_PLAYER);
    }

    @Override
    public boolean onCreate() {
        helper = new HockeyDBHelper(getContext());
        return true;
    }



    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = matcher.match(uri);

        if (uriType == matcher.NO_MATCH) {
            return null;
        }

        if (uriType == DELETE_COMPETITION) {
            String competitionID = uri.getPathSegments().get(SEGMENT_ID);
            String where = DBConstants.tCOMPETITIONS + "." + DBConstants.cID + " = " + competitionID;
            helper.getWritableDatabase().delete(DBConstants.tCOMPETITIONS, where, null);
            return null;
        }

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        if (uriType == COMPETITIONS_ALL) {
            builder.setTables(DBConstants.tCOMPETITIONS);
        } else if (uriType == COMPETITIONS_BY_PLAYER) {
            String playerID = uri.getPathSegments().get(SEGMENT_ID);
            builder.setTables(
                    DBConstants.tPLAYERS_IN_COMPETITION + " join " + DBConstants.tCOMPETITIONS + " ON " +
                    DBConstants.tPLAYERS_IN_COMPETITION + "." + DBConstants.cCOMPETITIONID + " = " +
                    DBConstants.tCOMPETITIONS + "." + DBConstants.cID);

            projection = new String[]{DBConstants.tCOMPETITIONS + ".*"};
            selection = DBConstants.tPLAYERS_IN_COMPETITION + "." + DBConstants.cPLAYER_ID + " = " + playerID;
        } else if (uriType == EMPTY_COMPETITION_ONE) {
            String competitionID = uri.getPathSegments().get(SEGMENT_ID);
            builder.setTables(
                    DBConstants.tCOMPETITIONS +
                            " left join " + DBConstants.tTOURNAMENTS + " ON " +
                                DBConstants.tCOMPETITIONS + "." + DBConstants.cID + " = " +
                                DBConstants.tTOURNAMENTS + "." + DBConstants.cCOMPETITIONID +
                            " left join " + DBConstants.tPLAYERS_IN_COMPETITION + " ON " +
                                DBConstants.tPLAYERS_IN_COMPETITION + "." + DBConstants.cCOMPETITIONID + " = " +
                                DBConstants.tCOMPETITIONS + "." + DBConstants.cID);

            projection = new String[]{DBConstants.tCOMPETITIONS + ".*"};
            selection = DBConstants.tCOMPETITIONS + "." + DBConstants.cID + " = " + competitionID + " and " +
                        DBConstants.tTOURNAMENTS + "." + DBConstants.cCOMPETITIONID + " IS NULL and " +
                        DBConstants.tPLAYERS_IN_COMPETITION + "." + DBConstants.cCOMPETITIONID + " IS NULL";
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
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
