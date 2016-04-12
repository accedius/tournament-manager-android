package fit.cvut.org.cz.squash.data.daos;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.data.CPConstants;
import fit.cvut.org.cz.tmlibrary.data.CursorParser;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPackagePlayerDAO;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public class PlayerDAO implements IPackagePlayerDAO {

    @Override
    public void addPlayerToCompetition(Context context, long playerId, long competitionId) {

    }

    @Override
    public void addPlayerToTournament(Context context, long playerId, long tournamentId) {

    }

    @Override
    public void addPlayerToMatch(Context context, long playerId, long matchId) {

    }

    @Override
    public void deletePlayerFromCompetition(Context context, long playerId, long competitionId) {

    }

    @Override
    public void deletePlayerFromTournament(Context context, long playerId, long tournamentId) {

    }

    @Override
    public void deletePlayerFromMatch(Context context, long playerId, long matchId) {

    }

    @Override
    public ArrayList<Long> getPlayerIdsByCompetition(Context context, long competitionId) {
        return null;
    }

    @Override
    public ArrayList<Long> getPlayerIdsByTournament(Context context, long tournamentId) {
        return null;
    }

    @Override
    public ArrayList<Long> getPlayerIdsByMatch(Context context, long matchId) {
        return null;
    }


    @Override
    public ArrayList<DPlayer> getAllPlayers(Context context) {

        PackageManager pm = context.getPackageManager();
        ApplicationInfo ai = null;
        try {
            ai = pm.getApplicationInfo("fit.cvut.org.cz.tournamentmanager", PackageManager.GET_META_DATA);
            String cpUri = ai.metaData.getString("player_cp_authority");

            String[] projection = new String[] {DBConstants.cID, DBConstants.cNAME, DBConstants.cETAG, DBConstants.cUID, DBConstants.cLASTMODIFIED, DBConstants.cEMAIL, DBConstants.cNOTE};

            Cursor c = context.getContentResolver().query(Uri.parse("content://" + cpUri + "/" + CPConstants.uPlayers), projection, null, null, null);
            ArrayList<DPlayer> players = new ArrayList<>();
            while (c.moveToNext()){
                players.add(CursorParser.getInstance().parseDPlayer(c));
            }

            return players;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
