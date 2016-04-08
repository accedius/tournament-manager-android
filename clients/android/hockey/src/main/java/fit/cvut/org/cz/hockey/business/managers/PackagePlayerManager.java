package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class PackagePlayerManager implements IPackagePlayerManager {
    @Override
    public void addPlayerToCompetition(Context context, long playerId, long competitionId) {
        DAOFactory.getInstance().packagePlayerDAO.addPlayerToCompetition( context, playerId, competitionId);
    }

    @Override
    public void addPlayerToTournament(Context context, long playerId, long tournamentId) {
        DAOFactory.getInstance().packagePlayerDAO.addPlayerToTournament( context, playerId, tournamentId );
    }

    @Override
    public void addPlayerToMatch(Context context, long playerId, long matchId) {
        DAOFactory.getInstance().packagePlayerDAO.addPlayerToMatch( context, playerId, matchId );
    }

    @Override
    public void deletePlayerFromCompetition(Context context, long playerId, long competitionId) {
        DAOFactory.getInstance().packagePlayerDAO.deletePlayerFromCompetition( context, playerId, competitionId );
    }

    @Override
    public void deletePlayerFromTournament(Context context, long playerId, long tournamentId) {
        DAOFactory.getInstance().packagePlayerDAO.deletePlayerFromTournament( context, playerId, tournamentId );
    }

    @Override
    public void deletePlayerFromMatch(Context context, long playerId, long matchId) {
        DAOFactory.getInstance().packagePlayerDAO.deletePlayerFromMatch( context, playerId, matchId );
    }

    @Override
    public ArrayList<Player> getPlayersByCompetition(Context context, long competitionId) {
        ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByCompetition( context, competitionId );
        ArrayList<Player> res = new ArrayList<>();
        //TODO fill res from Core content provider
        return res;
    }

    @Override
    public ArrayList<Player> getPlayersByTournament(Context context, long tournamentId) {
        ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByTournament( context, tournamentId );
        ArrayList<Player> res = new ArrayList<>();
        //TODO fill res from Core content provider
        return res;
    }

    @Override
    public ArrayList<Player> getPlayersByMatch(Context context, long matchId) {
        ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByMatch( context, matchId );
        ArrayList<Player> res = new ArrayList<>();
        //TODO fill res from Core content provider
        return res;
    }

    @Override
    public ArrayList<Player> getAllPlayers(Context context) {
        ArrayList<DPlayer> dPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers( context );
        ArrayList<Player> res = new ArrayList<>();
        for ( DPlayer dt: dPlayers )
        {
            res.add( new Player(dt) );
        }
        return res;
    }
}
