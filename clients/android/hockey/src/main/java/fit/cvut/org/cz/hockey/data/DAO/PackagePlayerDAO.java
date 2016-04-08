package fit.cvut.org.cz.hockey.data.DAO;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IPackagePlayerDAO;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class PackagePlayerDAO implements IPackagePlayerDAO {
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
        return null;
    }
}
