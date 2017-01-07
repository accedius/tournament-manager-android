package fit.cvut.org.cz.squash.data;

import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.util.HashMap;

import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.tmlibrary.data.helpers.CPConstants;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

/**
 * This provider allows core to get competitions by player.
 */
public class CompetitionCP extends fit.cvut.org.cz.tmlibrary.data.CompetitionCP {
    public static final String AUTHORITY = "fit.cvut.org.cz.squash.data";

    private static final int BADMINTON = 100;
    private static final int BEACH = 200;
    private static final int SQUASH = 300;
    private static final int TENNIS = 400;
    private static final int VOLLEYBALL = 500;

    protected static final HashMap<String, Integer> sportContexts = new HashMap<String, Integer>(){{
        put(SquashPackage.BADMINTON_NAME, BADMINTON);
        put(SquashPackage.BEACH_NAME, BEACH);
        put(SquashPackage.SQUASH_NAME, SQUASH);
        put(SquashPackage.TENNIS_NAME, TENNIS);
        put(SquashPackage.VOLLEYBALL_NAME, VOLLEYBALL);
    }};

    protected String getSport(int uriID) {
        if (uriID > VOLLEYBALL)
            return SquashPackage.VOLLEYBALL_NAME;
        else if (uriID > TENNIS)
            return SquashPackage.TENNIS_NAME;
        else if (uriID > SQUASH)
            return SquashPackage.SQUASH_NAME;
        else if (uriID > BEACH)
            return SquashPackage.BEACH_NAME;
        else if (uriID > BADMINTON)
            return SquashPackage.BADMINTON_NAME;
        return null;
    }

    @Override
    protected SQLiteDatabase getWritableDatabase(Context context, String sport) {
        return new SquashDAOFactory(getContext(), sport).getWritableDatabase();
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