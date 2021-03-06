package fit.cvut.org.cz.hockey.data;

import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.HashMap;

import fit.cvut.org.cz.hockey.presentation.HockeyPackage;
import fit.cvut.org.cz.tmlibrary.data.helpers.CPConstants;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

/**
 * This provider allows core to get competitions by player.
 */
public class CompetitionCP extends fit.cvut.org.cz.tmlibrary.data.CompetitionCP {
    public static final String AUTHORITY = "fit.cvut.org.cz.hockey.data";

    private static final int FLOORBALL = 100;
    private static final int HOCKEY = 200;

    protected static final HashMap<String, Integer> sportContexts = new HashMap<String, Integer>() {{
        put(HockeyPackage.FLOORBALL_NAME, FLOORBALL);
        put(HockeyPackage.HOCKEY_NAME, HOCKEY);
    }};

    protected String getSport(int uriID) {
        if (uriID > HOCKEY)
            return HockeyPackage.HOCKEY_NAME;
        else if (uriID > FLOORBALL)
            return HockeyPackage.FLOORBALL_NAME;
        return null;
    }

    @Override
    protected SQLiteDatabase getWritableDatabase(Context context, String sport) {
        return new HockeyDAOFactory(getContext(), sport).getWritableDatabase();
    }

    private static final UriMatcher matcher;
    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        for (HashMap.Entry<String, Integer> sport : sportContexts.entrySet()) {
            matcher.addURI(AUTHORITY, sport.getKey() + CPConstants.uCompetitionsByPlayer + "#", COMPETITIONS_BY_PLAYER + sport.getValue());
        }
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

        if (uriType == COMPETITIONS_BY_PLAYER) {
            String playerID = uri.getPathSegments().get(SEGMENT_ID);
            builder.setTables(
                    DBConstants.tPLAYERS_IN_COMPETITION + " join " + DBConstants.tCOMPETITIONS + " ON " +
                            DBConstants.tPLAYERS_IN_COMPETITION + "." + DBConstants.cCOMPETITION_ID + " = " +
                            DBConstants.tCOMPETITIONS + "." + DBConstants.cID);

            projection = new String[]{DBConstants.tCOMPETITIONS + ".*"};
            selection = DBConstants.tPLAYERS_IN_COMPETITION + "." + DBConstants.cPLAYER_ID + " = " + playerID;
        }

        Cursor cursor = builder.query(getWritableDatabase(getContext(), sport), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
}