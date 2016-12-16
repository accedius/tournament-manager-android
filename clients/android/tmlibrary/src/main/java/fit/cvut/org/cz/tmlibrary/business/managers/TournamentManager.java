package fit.cvut.org.cz.tmlibrary.business.managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.TeamPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;

/**
 * Created by kevin on 30. 3. 2016.
 */
abstract public class TournamentManager extends TManager<Tournament> implements ITournamentManager {
    @Override
    protected Class<Tournament> getMyClass() {
        return Tournament.class;
    }

    @Override
    public boolean delete(long id) {
        try {
            List<Team> teams = managerFactory.getDaoFactory()
                    .getMyDao(Team.class).queryForEq(DBConstants.cTOURNAMENT_ID, id);
            if (!teams.isEmpty())
                return false;

            List<TournamentPlayer> players = managerFactory.getDaoFactory()
                    .getMyDao(TournamentPlayer.class).queryForEq(DBConstants.cTOURNAMENT_ID, id);
            if (!players.isEmpty())
                return false;

            managerFactory.getDaoFactory().getMyDao(Tournament.class).deleteById(id);
            managerFactory.getDaoFactory().getMyDao(PointConfiguration.class).deleteById(id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<Tournament> getByCompetitionId(long competitionId) {
        try {
            return managerFactory.getDaoFactory().getMyDao(Tournament.class).queryForEq(DBConstants.cCOMPETITION_ID, competitionId);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Player> getTournamentPlayers(long tournamentId) {
        Map<Long, Player> allPlayers = ((ICorePlayerManager)managerFactory.getEntityManager(Player.class)).getMapAll();
        List<Player> res = new ArrayList<>();
        try {
            List<TournamentPlayer> tournamentPlayers = managerFactory.getDaoFactory().getMyDao(TournamentPlayer.class).queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
            for (TournamentPlayer tournamentPlayer : tournamentPlayers) {
                res.add(allPlayers.get(tournamentPlayer.getPlayerId()));
            }
            return res;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return res;
        }
    }

    @Override
    public List<Tournament> getByPlayer(long playerId) {
        try {
            List<TournamentPlayer> tournamentPlayers = managerFactory.getDaoFactory().getMyDao(TournamentPlayer.class).queryForEq(DBConstants.cPLAYER_ID, playerId);
            List<Tournament> tournaments = new ArrayList<>();
            for (TournamentPlayer player : tournamentPlayers) {
                tournaments.add(getById(player.getTournamentId()));
            }
            return tournaments;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Player> getTournamentPlayersComplement(long tournamentId) {
        Tournament tournament = getById(tournamentId);
        try {
            List<CompetitionPlayer> competitionPlayers = managerFactory.getDaoFactory().getMyDao(CompetitionPlayer.class)
                    .queryForEq(DBConstants.cCOMPETITION_ID, tournament.getCompetitionId());
            Map<Long, Player> allPlayers = ((ICorePlayerManager)managerFactory.getEntityManager(Player.class)).getMapAll();
            Map<Long, Player> allCompetitionPlayers = new HashMap<>();
            for (CompetitionPlayer competitionPlayer : competitionPlayers) {
                allCompetitionPlayers.put(competitionPlayer.getPlayerId(), allPlayers.get(competitionPlayer.getPlayerId()));
            }
            List<TournamentPlayer> tournamentPlayers = managerFactory.getDaoFactory().getMyDao(TournamentPlayer.class).queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
            for (TournamentPlayer competitionPlayer : tournamentPlayers) {
                allCompetitionPlayers.remove(competitionPlayer.getPlayerId());
            }
            return new ArrayList<>(allCompetitionPlayers.values());
        }
        catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    List<Player> getTeamPlayers(Team team) {
        try {
            List<TeamPlayer> teamPlayers;
            List<Player> players = new ArrayList<>();
            Map<Long, Player> allPlayers = ((ICorePlayerManager)managerFactory.getEntityManager(Player.class)).getMapAll();

            teamPlayers = managerFactory.getDaoFactory().getMyDao(TeamPlayer.class).queryForEq(DBConstants.cTEAM_ID, team.getId());
            for (TeamPlayer teamPlayer : teamPlayers) {
                players.add(allPlayers.get(teamPlayer.getPlayerId()));
            }
            return players;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean removePlayerFromTournament(long playerId, long tournamentId) {
        try {
            Tournament tournament = managerFactory.getDaoFactory().getMyDao(Tournament.class).queryForId(tournamentId);
            Competition competition = managerFactory.getDaoFactory().getMyDao(Competition.class).queryForId(tournament.getCompetitionId());
            competition.setType(CompetitionTypes.competitionTypes()[competition.getTypeId()]);

            if (CompetitionTypes.teams().equals(competition.getType())) {
                // Check if some team contains player
                List<Team> teams = managerFactory.getDaoFactory().getMyDao(Team.class).queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
                for (Team team : teams) {
                    List<Player> teamPlayers = getTeamPlayers(team);
                    for (Player player : teamPlayers)
                        if (player.getId() == playerId)
                            return false;
                }
            }

            // Check if player participates in match
            List<Match> matches = managerFactory.getDaoFactory().getMyDao(Match.class).queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
            for (Match match : matches) {
                List<Participant> matchParticipants = managerFactory.getDaoFactory().getMyDao(Participant.class).queryForEq(DBConstants.cMATCH_ID, match.getId());
                for (Participant participant : matchParticipants) {
                    // Check if some participant is player
                    if (CompetitionTypes.individuals().equals(competition.getType()) && participant.getParticipantId() == playerId)
                        return false;

                    List<PlayerStat> playerStats = managerFactory.getDaoFactory().getMyDao(PlayerStat.class).queryForEq(DBConstants.cPARTICIPANT_ID, participant.getId());
                    for (PlayerStat playerStat : playerStats)
                        if (playerStat.getPlayerId() == playerId)
                            return false;
                }
            }

            // Remove player
            Dao<TournamentPlayer, Long> tournamentPlayerDao = managerFactory.getDaoFactory().getMyDao(TournamentPlayer.class);
            DeleteBuilder<TournamentPlayer, Long> tournamentPlayerBuilder = tournamentPlayerDao.deleteBuilder();
            tournamentPlayerBuilder.where()
                    .eq(DBConstants.cTOURNAMENT_ID, tournamentId).and()
                    .eq(DBConstants.cPLAYER_ID, playerId);
            tournamentPlayerBuilder.delete();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void addPlayer(long playerId, long tournamentId) {
        Dao<TournamentPlayer, Long> tournamentPlayerDao = managerFactory.getDaoFactory().getMyDao(TournamentPlayer.class);
        try {
            tournamentPlayerDao.create(new TournamentPlayer(tournamentId, playerId));
        } catch (SQLException e) {}
    }
}
