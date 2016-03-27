package fit.cvut.org.cz.squash.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import fit.cvut.org.cz.tmlibrary.data.CPConstants;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.DBScripts;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class CompetitionCP extends ContentProvider {

    public static final String AUTHORITY = "fit.cvut.org.cz.squash.data";

    private SquashDBHelper helper;

    private static final int COMPETITIONS_ALL = 0;
    private static final int COMPETITION_ONE = 1;

    private static final UriMatcher matcher ;
    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, CPConstants.uCompetitions, COMPETITIONS_ALL);
        matcher.addURI(AUTHORITY, CPConstants.uCompetitions + "#", COMPETITION_ONE);
    }

    @Override
    public boolean onCreate() {
        helper = new SquashDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = matcher.match(uri);

        if (uriType != COMPETITIONS_ALL) return null;

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DBConstants.tCOMPETITIONS);

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
