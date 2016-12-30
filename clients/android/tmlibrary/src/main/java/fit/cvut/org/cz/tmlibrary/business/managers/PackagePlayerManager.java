package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.helpers.CPConstants;
import fit.cvut.org.cz.tmlibrary.data.helpers.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

/**
 * Created by kevin on 2.12.2016.
 */
public class PackagePlayerManager implements IPackagePlayerManager {
    protected Context context;

    public PackagePlayerManager(Context context) {
        this.context = context;
    }

    @Override
    public List<Player> getAll() {
        Map<Long, Player> players = getMapAll();
        List<Player> playerList = new ArrayList<>();
        playerList.addAll(players.values());
        return playerList;
    }

    @Override
    public Map<Long, Player> getMapAll() {
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
                Player player = CursorParser.parsePlayer(c);
                players.put(player.getId(), player);
            }
            c.close();

            return players;
        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Player getById(long playerId) {
        Map<Long, Player> players = getMapAll();
        return players.get(playerId);
    }

    @Override
    public void insert(Player player) {
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
    public void update(Player player) {
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

    @Override
    public List<Player> getByColumn(String column, Object value) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }

    public static void orderPlayers(List<Player> players) {
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player lp, Player rp) {
                if (!rp.getName().equals(lp.getName()))
                    return lp.getName().compareTo(rp.getName());
                return lp.getEmail().compareTo(rp.getEmail());
            }
        });
    }
}
