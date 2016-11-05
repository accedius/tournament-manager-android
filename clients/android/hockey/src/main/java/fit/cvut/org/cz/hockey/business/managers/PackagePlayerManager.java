package fit.cvut.org.cz.hockey.business.managers;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.Map;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.hockey.data.StatsEnum;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DStat;
import fit.cvut.org.cz.tmlibrary.data.entities.DTournament;

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
    public boolean deletePlayerFromCompetition(Context context, long playerId, long competitionId) {
        ArrayList<DTournament> tournaments = DAOFactory.getInstance().tournamentDAO.getByCompetitionId(context, competitionId);

        //Check if player is not in any tournament
        for (DTournament tourn : tournaments) {
            ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByTournament(context, tourn.getId());
            if (playerIds.contains(playerId)) return false;
        }

        DAOFactory.getInstance().packagePlayerDAO.deletePlayerFromCompetition(context, playerId, competitionId);

        return true;
    }

    @Override
    public boolean deletePlayerFromTournament(Context context, long playerId, long tournamentId) {
        ArrayList<Player> notInTeams = ManagerFactory.getInstance().packagePlayerManager.getPlayersNotInTeams(context, tournamentId);
        boolean flag = false;
        for (Player p : notInTeams) {
            if (p.getId() == playerId) {
                flag = true;
                break;
            }
        }
        if (!flag) return false;

        ArrayList<DStat> tourStats = DAOFactory.getInstance().statDAO.getStatsByTournamentId(context, tournamentId);

        for (DStat stat : tourStats) {
            if (stat.getPlayerId() == playerId) return false;
        }
        DAOFactory.getInstance().packagePlayerDAO.deletePlayerFromTournament(context, playerId, tournamentId);

        return true;
    }

    @Override
    public ArrayList<Player> getPlayersByCompetition(Context context, long competitionId) {
        ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByCompetition(context, competitionId);
        Map<Long, Player> allPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers(context);
        ArrayList<Player> res = new ArrayList<>();
        for (Long pId : playerIds) {
            res.add(allPlayers.get(pId));
        }

        return res;
    }

    @Override
    public ArrayList<Player> getPlayersByTournament(Context context, long tournamentId) {
        ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByTournament(context, tournamentId);
        Map<Long, Player> allPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers(context);
        ArrayList<Player> res = new ArrayList<>();
        for (Long pId : playerIds) {
            res.add(allPlayers.get(pId));
        }

        return res;
    }

    @Override
    public ArrayList<Player> getPlayersByParticipant(Context context, long participantId) {
        ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByParticipant(context, participantId);
        Map<Long, Player> allPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers(context);
        ArrayList<Player> res = new ArrayList<>();
        for (Long pId : playerIds) {
            res.add(allPlayers.get(pId));
        }

        return res;
    }

    @Override
    public ArrayList<Player> getPlayersByTeam(Context context, long teamId) {
        ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByTeam(context, teamId);
        Map<Long, Player> allPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers(context);
        ArrayList<Player> res = new ArrayList<>();
        for (Long pId : playerIds) {
            res.add(allPlayers.get(pId));
        }

        return res;
    }

    @Override
    public Player getPlayerById(Context context, long playerId) {
        Map<Long, Player> players = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers(context);
        return players.get(playerId);
    }

    @Override
    public ArrayList<Player> getAllPlayers(Context context) {
        Map<Long, Player> dPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers(context);
        ArrayList<Player> res = new ArrayList<>();
        for (Map.Entry<Long, Player> entry : dPlayers.entrySet()) {
            res.add(entry.getValue());
        }
        return res;
    }

    @Override
    public long insertPlayer(Context context, Player player) {
        ContentValues values = new ContentValues();
        values.put("email", player.getEmail());
        values.put("name", player.getName());
        values.put("note", player.getNote());
        return DAOFactory.getInstance().packagePlayerDAO.insertPlayer(context, values);
    }

    @Override
    public void updatePlayer(Context context, Player player) {
        ContentValues values = new ContentValues();
        values.put("email", player.getEmail());
        values.put("name", player.getName());
        values.put("note", player.getNote());
        DAOFactory.getInstance().packagePlayerDAO.updatePlayer(context, values);
    }

    @Override
    public ArrayList<Player> getPlayersNotInCompetition(Context context, long competitionId) {
        ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByCompetition(context, competitionId);
        Map<Long, Player> allPlayers = DAOFactory.getInstance().packagePlayerDAO.getAllPlayers(context);
        ArrayList<Player> res = new ArrayList<>();
        for (Long pId : playerIds) {
            allPlayers.remove(pId);
        }
        for (Map.Entry<Long, Player> entry : allPlayers.entrySet()) {
            res.add(entry.getValue());
        }
        return res;
    }

    @Override
    public ArrayList<Player> getPlayersNotInTournament(Context context, long tournamentId) {
        Tournament t = ManagerFactory.getInstance().tournamentManager.getById(context, tournamentId);

        ArrayList<Player> compPlayer = getPlayersByCompetition(context, t.getCompetitionId());
        ArrayList<Player> tourPlayer = getPlayersByTournament(context, tournamentId);

        for (Player p : tourPlayer) compPlayer.remove(p);

        return compPlayer;
    }

    @Override
    public void updatePlayersInTeam(Context context, long teamId, ArrayList<Player> players) {
        DAOFactory.getInstance().packagePlayerDAO.deleteAllPlayersFromTeam(context, teamId);

        for (Player pl : players)
            DAOFactory.getInstance().packagePlayerDAO.addPlayerToTeam(context, pl.getId(), teamId);
    }

    @Override
    public ArrayList<Player> getPlayersNotInTeams(Context context, long tournamentId) {
        ArrayList<Team> teams = ManagerFactory.getInstance().teamManager.getByTournamentId(context, tournamentId);
        ArrayList<Player> players = getPlayersByTournament(context, tournamentId);

        for (Team t : teams)
            for (Player p : t.getPlayers())
                players.remove(p);

        return players;
    }

    @Override
    public void updatePlayersInParticipant(Context context, long participantId, long competitionId, long tournamentId, ArrayList<Player> players) {
        ArrayList<DStat> partStats = DAOFactory.getInstance().statDAO.getStatsByParticipantId(context, participantId);
        ArrayList<Long> playerIds = new ArrayList<>();
        for (Player p : players) playerIds.add(p.getId());

        ArrayList<Long> currentPlayers = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByParticipant(context, participantId);

        ArrayList<Long> toDelete = new ArrayList<>(currentPlayers);
        toDelete.removeAll(playerIds);

        ArrayList<Long> toAdd = new ArrayList<>(playerIds);
        toAdd.removeAll(currentPlayers);

        for (Long id : toAdd) {
            createStatistics(context, id, participantId, tournamentId, competitionId);
        }
        for (Long id : toDelete) {
            removeStatistics(context, id, partStats);
        }
    }

    private void createStatistics(Context context, long playerId, long participantId, long tournamentId, long competitionId) {
        for (StatsEnum statEn : StatsEnum.values()) {
            if (!statEn.isForPlayer()) continue;
            DStat toInsert = new DStat(-1, playerId, participantId, statEn.toString(), tournamentId, competitionId, String.valueOf(0));
            DAOFactory.getInstance().statDAO.insert(context, toInsert);
        }
    }

    private void removeStatistics(Context context, long playerId, ArrayList<DStat> partStats) {
        for (DStat stat : partStats) {
            if (stat.getPlayerId() == playerId)
                DAOFactory.getInstance().statDAO.delete(context, stat.getId());
        }
    }

    @Override
    public ArrayList<Player> getPlayersNotInParticipant(Context context, long participantId) {
        //Not used
        return null;
    }
}

