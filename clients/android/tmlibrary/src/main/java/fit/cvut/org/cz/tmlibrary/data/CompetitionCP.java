package fit.cvut.org.cz.tmlibrary.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;


/**
 * Abstract Content Provider.
 */
abstract public class CompetitionCP extends ContentProvider {
    protected static final int COMPETITIONS_BY_PLAYER = 1;
    protected static final int SEGMENT_ID = 1;

    abstract protected String getSport(int uriID);
    abstract protected SQLiteDatabase getWritableDatabase(Context context, String sport);

    @Override
    public boolean onCreate() {
        return true;
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