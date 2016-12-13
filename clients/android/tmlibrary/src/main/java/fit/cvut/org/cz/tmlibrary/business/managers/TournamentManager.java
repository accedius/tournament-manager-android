package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.CompetitionPlayer;
import fit.cvut.org.cz.tmlibrary.business.entities.Match;
import fit.cvut.org.cz.tmlibrary.business.entities.Participant;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.business.entities.Team;
import fit.cvut.org.cz.tmlibrary.business.entities.TeamPlayer;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 30. 3. 2016.
 */
abstract public class TournamentManager extends BaseManager<Tournament> implements ITournamentManager {
    public TournamentManager(Context context, ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(context, corePlayerManager, sportDBHelper);
    }

    @Override
    public boolean delete(long id) {
        try {
            List<Team> teams = sportDBHelper.getTeamDAO().queryForEq(DBConstants.cTOURNAMENT_ID, id);
            if (!teams.isEmpty())
                return false;

            List<TournamentPlayer> players = sportDBHelper.getTournamentPlayerDAO().queryForEq(DBConstants.cTOURNAMENT_ID, id);
            if (!players.isEmpty())
                return false;

            getDao().delete(getDao().queryForId(id));
            sportDBHelper.getPointConfigurationDAO().deleteById(id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<Tournament> getByCompetitionId(long competitionId) {
        try {
            return getDao().queryForEq(DBConstants.cCOMPETITION_ID, competitionId);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Player> getTournamentPlayers(long tournamentId) {
        Map<Long, Player> allPlayers = corePlayerManager.getAllPlayers();
        List<Player> res = new ArrayList<>();
        try {
            List<TournamentPlayer> tournamentPlayers = sportDBHelper.getTournamentPlayerDAO().queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
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
            List<TournamentPlayer> tournamentPlayers = sportDBHelper.getTournamentPlayerDAO().queryForEq(DBConstants.cPLAYER_ID, playerId);
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
            List<CompetitionPlayer> competitionPlayers = sportDBHelper.getCompetitionPlayerDAO()
                    .queryForEq(DBConstants.cCOMPETITION_ID, tournament.getCompetitionId());
            Map<Long, Player> allPlayers = corePlayerManager.getAllPlayers();
            Map<Long, Player> allCompetitionPlayers = new HashMap<>();
            for (CompetitionPlayer competitionPlayer : competitionPlayers) {
                allCompetitionPlayers.put(competitionPlayer.getPlayerId(), allPlayers.get(competitionPlayer.getPlayerId()));
            }
            List<TournamentPlayer> tournamentPlayers = sportDBHelper.getTournamentPlayerDAO().queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
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
            Map<Long, Player> allPlayers = corePlayerManager.getAllPlayers();

            teamPlayers = sportDBHelper.getTeamPlayerDAO().queryForEq(DBConstants.cTEAM_ID, team.getId());
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
            Tournament tournament = sportDBHelper.getTournamentDAO().queryForId(tournamentId);
            Competition competition = sportDBHelper.getCompetitionDAO().queryForId(tournament.getCompetitionId());
            competition.setType(CompetitionTypes.competitionTypes()[competition.getTypeId()]);

            if (CompetitionTypes.teams().equals(competition.getType())) {
                // Check if some team contains player
                List<Team> teams = sportDBHelper.getTeamDAO().queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
                for (Team team : teams) {
                    List<Player> teamPlayers = getTeamPlayers(team);
                    for (Player player : teamPlayers)
                        if (player.getId() == playerId)
                            return false;
                }
            }

            // Check if player participates in match
            List<Match> matches = sportDBHelper.getMatchDAO().queryForEq(DBConstants.cTOURNAMENT_ID, tournamentId);
            for (Match match : matches) {
                List<Participant> matchParticipants = sportDBHelper.getParticipantDAO().queryForEq(DBConstants.cMATCH_ID, match.getId());
                for (Participant participant : matchParticipants) {
                    // Check if some participant is player
                    if (CompetitionTypes.individuals().equals(competition.getType()) && participant.getParticipantId() == playerId)
                        return false;

                    List<PlayerStat> playerStats = sportDBHelper.getPlayerStatDAO().queryForEq(DBConstants.cPARTICIPANT_ID, participant.getId());
                    for (PlayerStat playerStat : playerStats)
                        if (playerStat.getPlayerId() == playerId)
                            return false;
                }
            }

            // Remove player
            Dao<TournamentPlayer, Long> tournamentPlayerDao = sportDBHelper.getTournamentPlayerDAO();
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
        Dao<TournamentPlayer, Long> tournamentPlayerDao = sportDBHelper.getTournamentPlayerDAO();
        try {
            tournamentPlayerDao.create(new TournamentPlayer(tournamentId, playerId));
        } catch (SQLException e) {}
    }
}
