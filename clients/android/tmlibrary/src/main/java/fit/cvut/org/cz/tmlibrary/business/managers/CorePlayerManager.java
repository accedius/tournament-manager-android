package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.CPConstants;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by kevin on 2.12.2016.
 */
public class CorePlayerManager implements ICorePlayerManager {
    protected Context context;

    public CorePlayerManager(Context context) {
        this.context = context;
    }

    @Override
    public Map<Long, Player> getAllPlayers() {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai = null;
        try {
            ai = pm.getApplicationInfo("fit.cvut.org.cz.tournamentmanager", PackageManager.GET_META_DATA);
            String cpUri = ai.metaData.getString("player_cp_authority");

            String[] projection = new String[] {DBConstants.cID, DBConstants.cNAME, DBConstants.cETAG, DBConstants.cUID, DBConstants.cLASTMODIFIED, DBConstants.cLASTSYNCHRONIZED, DBConstants.cEMAIL, DBConstants.cNOTE};

            Uri uri = Uri.parse("content://" + cpUri + "/" + CPConstants.uPlayers);

            Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
            Map<Long, Player> players = new HashMap<>();
            while (c.moveToNext()){
                Player player = CursorParser.getInstance().parsePlayer(c);
                players.put(player.getId(), player);
            }
            c.close();

            return players;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Player getPlayerById(long playerId) {
        Map<Long, Player> players = getAllPlayers();
        return players.get(playerId);
    }

    @Override
    public void insertPlayer(Player player) {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo("fit.cvut.org.cz.tournamentmanager", PackageManager.GET_META_DATA);
            String cpUri = ai.metaData.getString("player_cp_authority");

            Uri uri = Uri.parse("content://" + cpUri + "/" + CPConstants.uPlayers);
            Uri insertUri = context.getContentResolver().insert(uri, player.getContentValues());
            player.setId(ContentUris.parseId(insertUri));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePlayer(Player player) {
        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo("fit.cvut.org.cz.tournamentmanager", PackageManager.GET_META_DATA);
            String cpUri = ai.metaData.getString("player_cp_authority");

            ContentValues values = player.getContentValues();
            String where = DBConstants.tPLAYERS + "." + DBConstants.cEMAIL + " = '" + values.getAsString("email")+"'";
            Uri uri = Uri.parse("content://" + cpUri + "/" + CPConstants.uPlayerUpdate);
            context.getContentResolver().update(uri, values, where, null);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
