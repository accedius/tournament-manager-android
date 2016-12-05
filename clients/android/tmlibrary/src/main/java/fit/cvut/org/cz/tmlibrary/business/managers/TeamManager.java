package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.TeamPlayer;
import fit.cvut.org.cz.tmlibrary.business.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 9.11.2016.
 */
abstract public class TeamManager extends BaseManager<Team> implements ITeamManager {
    public TeamManager(ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
    }

    @Override
    public List<Team> getByTournamentId(Context context, long tournamentId) {
        try {
            List<Team> teams = getDao(context).queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
            for (Team team : teams) {
                team.setPlayers(getTeamPlayers(context, team));
            }
            return new ArrayList<>(teams);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean delete(Context context, long id) {
        // Delete only if does not have matches
        Team team = getById(context, id);
        List<Match> matches;
        try {
            matches = sportDBHelper.getMatchDAO().queryForEq(DBConstants.cTOURNAMENT_ID, team.getTournamentId());
            for (Match match : matches) {
                List<Participant> participants = sportDBHelper.getParticipantDAO()
                        .queryForEq(DBConstants.cMATCH_ID, match.getId());
                for (Participant participant : participants)
                    if (participant.getParticipantId() == id)
                        return false;
            }
            // Delete team
            getDao(context).delete(getDao(context).queryForId(id));
            return true;
        }
        catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Team getById(Context context, long id) {
        try {
            Team team = getDao(context).queryForId(id);
            team.setPlayers(getTeamPlayers(context, team));
            return team;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Team> getAll(Context context) {
        try {
            List<Team> teams = getDao(context).queryForAll();
            for (Team team : teams) {
                team.setPlayers(getTeamPlayers(context, team));
            }
            return teams;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean generateRosters(Context context, long competitionId, long tournamentId, int generatingType) {
        return false;
    }

    @Override
    public List<Player> getTeamPlayers(Context context, Team team) {
        Map<Long, Player> allPlayers = corePlayerManager.getAllPlayers(context);
        List<Player> res = new ArrayList<>();
        try {
            List<TeamPlayer> teamPlayers = sportDBHelper.getTeamPlayerDAO()
                    .queryForEq(DBConstants.cTEAM_ID, team.getId());
            for (TeamPlayer teamPlayer : teamPlayers) {
                res.add(allPlayers.get(teamPlayer.getPlayerId()));
            }
            return res;
        }
        catch (SQLException e) {
            return res;
        }
    }

    @Override
    public void addPlayer(Context context, Team team, Player player) {
        if (team == null || player == null)
            return;
        Dao<TeamPlayer, Long> teamPlayerDao = sportDBHelper.getTeamPlayerDAO();

        try {
            teamPlayerDao.create(new TeamPlayer(team.getId(), player.getId()));
        } catch (SQLException e) {}
    }

    @Override
    public void updatePlayersInTeam(Context context, long teamId, List<Player> players) {
        Team team = getById(context, teamId);
        try {
            DeleteBuilder<TeamPlayer, Long> teamBuilder = sportDBHelper.getTeamPlayerDAO().deleteBuilder();
            teamBuilder.where().eq(DBConstants.cTEAM_ID, teamId);
            teamBuilder.delete();
        } catch (SQLException e) {}

        for (Player player : players) {
            addPlayer(context, team, player);
        }
    }

    @Override
    public List<Player> getFreePlayers(Context context, long tournamentId) {
        try {
            List<TournamentPlayer> tournamentPlayers;
            List<Player> players = new ArrayList<>();
            Map<Long, Player> allPlayers = corePlayerManager.getAllPlayers(context);

            tournamentPlayers = sportDBHelper.getTournamentPlayerDAO()
                    .queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
            for (TournamentPlayer tournamentPlayer : tournamentPlayers) {
                players.add(allPlayers.get(tournamentPlayer.getPlayerId()));
            }

            HashMap<Long, Player> tournamentPlayersMap = new HashMap<>();
            for (Player player : players)
                tournamentPlayersMap.put(player.getId(), player);

            List<Team> teams = getByTournamentId(context, tournamentId);
            for (Team team : teams) {
                for (Player player : team.getPlayers()) {
                    tournamentPlayersMap.remove(player.getId());
                }
            }
            return new ArrayList<>(tournamentPlayersMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
