package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.DTeam;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class PackagePlayerManager implements IPackagePlayerManager {
    @Override
    public void addPlayerToCompetition(Context context, long playerId, long competitionId) {
        DAOFactory.getInstance().packagePlayerDAO.addPlayerToCompetition(context, playerId, competitionId);
    }

    @Override
    public void addPlayerToTournament(Context context, long playerId, long tournamentId) {
        DAOFactory.getInstance().packagePlayerDAO.addPlayerToTournament(context, playerId, tournamentId);
    }


    @Override
    public void deletePlayerFromCompetition(Context context, long playerId, long competitionId) {
        DAOFactory.getInstance().packagePlayerDAO.deletePlayerFromCompetition(context, playerId, competitionId);
    }

    @Override
    public void deletePlayerFromTournament(Context context, long playerId, long tournamentId) {
        DAOFactory.getInstance().packagePlayerDAO.deletePlayerFromTournament(context, playerId, tournamentId);
    }


    @Override
    public ArrayList<Player> getPlayersByCompetition(Context context, long competitionId) {
        ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByCompetition(context, competitionId);
        Map<Long, DPlayer> allPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers(context);
        ArrayList<Player> res = new ArrayList<>();
        for (Long pId : playerIds) {
            res.add(new Player(allPlayers.get(pId)));
        }

        return res;
    }

    @Override
    public ArrayList<Player> getPlayersByTournament(Context context, long tournamentId) {
        ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByTournament(context, tournamentId);
        Map<Long, DPlayer> allPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers(context);
        ArrayList<Player> res = new ArrayList<>();
        for (Long pId : playerIds) {
            res.add(new Player(allPlayers.get(pId)));
        }

        return res;
    }

    @Override
    public ArrayList<Player> getPlayersByParticipant(Context context, long participantId) {
        return null;
    }

    @Override
    public ArrayList<Player> getPlayersByTeam(Context context, long teamId) {
        ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByTeam(context, teamId);
        Map<Long, DPlayer> allPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers(context);
        ArrayList<Player> res = new ArrayList<>();
        for (Long pId : playerIds) {
            res.add( new Player(allPlayers.get( pId )) );
        }

        return res;

    }

    @Override
    public ArrayList<Player> getAllPlayers(Context context) {
        Map<Long, DPlayer> dPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers(context);
        ArrayList<Player> res = new ArrayList<>();
        for (Map.Entry<Long, DPlayer> entry : dPlayers.entrySet()) {
            res.add(new Player(entry.getValue()));
        }
        return res;
    }

    @Override
    public ArrayList<Player> getPlayersNotInCompetition(Context context, long competitionId) {
        ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByCompetition(context, competitionId);
        Map<Long, DPlayer> allPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers(context);
        ArrayList<Player> res = new ArrayList<>();
        for (Long pId : playerIds) {
            allPlayers.remove(pId);
        }
        for (Map.Entry<Long, DPlayer> entry : allPlayers.entrySet()) {
            res.add(new Player(entry.getValue()));
        }
        return res;
    }

    @Override
    public ArrayList<Player> getPlayersNotInTournament(Context context, long tournamentId) {
        Tournament t = ManagerFactory.getInstance().tournamentManager.getById( context, tournamentId );

        ArrayList<Player> compPlayer = getPlayersByCompetition(context, t.getCompetitionId());
        ArrayList<Player> tourPlayer = getPlayersByTournament( context, tournamentId );

        for( Player p : tourPlayer ) compPlayer.remove( p );

        return compPlayer;
    }

    @Override
    public void updatePlayersInTeam(Context context, long teamId, ArrayList<Player> players) {
        DAOFactory.getInstance().packagePlayerDAO.deleteAllPlayersFromTeam(context, teamId);

        for( Player pl : players )
            DAOFactory.getInstance().packagePlayerDAO.addPlayerToTeam( context, pl.getId(), teamId );

    }

    @Override
    public ArrayList<Player> getPlayersNotInTeams(Context context, long tournamentId) {

        ArrayList<Team> teams = ManagerFactory.getInstance().teamManager.getByTournamentId( context, tournamentId );
        ArrayList<Player> players = getPlayersByTournament( context, tournamentId );

        for (Team t : teams)
            for (Player p : t.getPlayers())
                players.remove(p);

        return players;
    }

    @Override
    public void updatePlayersInParticipant(Context context, long participantId, long competitionId, long tournamentId, ArrayList<Player> players) {

    }

    @Override
    public ArrayList<Player> getPlayersNotInParticipant(Context context, long participantId) {
        return null;
    }
}


