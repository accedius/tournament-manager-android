package fit.cvut.org.cz.tmlibrary.business.managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.TeamPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentPlayer;

/**
 * Created by kevin on 9.11.2016.
 */
abstract public class TeamManager extends TManager<Team> implements ITeamManager {
    @Override
    protected Class<Team> getMyClass() {
        return Team.class;
    }

    @Override
    public List<Team> getByTournamentId(long tournamentId) {
        try {
            List<Team> teams = managerFactory.getDaoFactory()
                    .getMyDao(Team.class).queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
            for (Team team : teams) {
                team.setPlayers(getTeamPlayers(team));
            }
            return new ArrayList<>(teams);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean delete(long id) {
        // Delete only if does not have matches
        Team team = getById(id);
        List<Match> matches;
        try {
            matches = managerFactory.getDaoFactory().getMyDao(Match.class).queryForEq(DBConstants.cTOURNAMENT_ID, team.getTournamentId());
            for (Match match : matches) {
                List<Participant> participants = managerFactory.getDaoFactory().getMyDao(Participant.class).queryForEq(DBConstants.cMATCH_ID, match.getId());
                for (Participant participant : participants)
                    if (participant.getParticipantId() == id)
                        return false;
            }
            // Delete team
            managerFactory.getDaoFactory().getMyDao(Team.class).deleteById(id);
            return true;
        }
        catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Team getById(long id) {
        try {
            Team team = managerFactory.getDaoFactory().getMyDao(Team.class).queryForId(id);
            if (team == null)
                return null;

            team.setPlayers(getTeamPlayers(team));
            return team;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Team> getAll() {
        try {
            List<Team> teams = managerFactory.getDaoFactory().getMyDao(Team.class).queryForAll();
            for (Team team : teams) {
                team.setPlayers(getTeamPlayers(team));
            }
            return teams;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean generateRosters(long competitionId, long tournamentId, int generatingType) {
        return false;
    }

    @Override
    public List<Player> getTeamPlayers(Team team) {
        Map<Long, Player> allPlayers = ((IPackagePlayerManager)managerFactory.getEntityManager(Player.class)).getMapAll();
        List<Player> res = new ArrayList<>();
        try {
            List<TeamPlayer> teamPlayers = managerFactory.getDaoFactory().getMyDao(TeamPlayer.class).queryForEq(DBConstants.cTEAM_ID, team.getId());
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
    public void addPlayer(Team team, Player player) {
        if (team == null || player == null)
            return;
        Dao<TeamPlayer, Long> teamPlayerDao = managerFactory.getDaoFactory().getMyDao(TeamPlayer.class);

        try {
            teamPlayerDao.create(new TeamPlayer(team.getId(), player.getId()));
        } catch (SQLException e) {}
    }

    @Override
    public void updatePlayersInTeam(long teamId, List<Player> players) {
        Team team = getById(teamId);
        try {
            DeleteBuilder<TeamPlayer, Long> teamBuilder = managerFactory.getDaoFactory().getMyDao(TeamPlayer.class).deleteBuilder();
            teamBuilder.where().eq(DBConstants.cTEAM_ID, teamId);
            teamBuilder.delete();
        } catch (SQLException e) {}

        for (Player player : players) {
            addPlayer(team, player);
        }
    }

    @Override
    public List<Player> getFreePlayers(long tournamentId) {
        try {
            List<TournamentPlayer> tournamentPlayers;
            List<Player> players = new ArrayList<>();
            Map<Long, Player> allPlayers = ((IPackagePlayerManager)managerFactory.getEntityManager(Player.class)).getMapAll();

            tournamentPlayers = managerFactory.getDaoFactory().getMyDao(TournamentPlayer.class).queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
            for (TournamentPlayer tournamentPlayer : tournamentPlayers) {
                players.add(allPlayers.get(tournamentPlayer.getPlayerId()));
            }

            HashMap<Long, Player> tournamentPlayersMap = new HashMap<>();
            for (Player player : players)
                tournamentPlayersMap.put(player.getId(), player);

            List<Team> teams = getByTournamentId(tournamentId);
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
