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
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ITournamentManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 30. 3. 2016.
 */
abstract public class TournamentManager extends BaseManager<Tournament> implements ITournamentManager {
    public TournamentManager(ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(corePlayerManager, sportDBHelper);
    }

    @Override
    public boolean delete(Context context, long id) {
        try {
            List<Team> teams = sportDBHelper.getTeamDAO().queryBuilder()
                    .where().eq(DBConstants.cTOURNAMENT_ID, id).query();
            if (!teams.isEmpty())
                return false;

            List<TournamentPlayer> players = sportDBHelper.getTournamentPlayerDAO().queryBuilder()
                    .where().eq(DBConstants.cTOURNAMENT_ID, id).query();
            if (!players.isEmpty())
                return false;

            getDao(context).delete(getDao(context).queryForId(id));
            sportDBHelper.getPointConfigurationDAO().deleteById(id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<Tournament> getByCompetitionId(Context context, long competitionId) {
        try {
            return getDao(context).queryBuilder()
                    .where()
                    .eq(DBConstants.cCOMPETITION_ID, competitionId)
                    .query();
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Player> getTournamentPlayers(Context context, long tournamentId) {
        Map<Long, Player> allPlayers = corePlayerManager.getAllPlayers(context);
        List<Player> res = new ArrayList<>();
        try {
            List<TournamentPlayer> tournamentPlayers = sportDBHelper.getTournamentPlayerDAO().queryBuilder()
                    .where()
                    .eq(DBConstants.cTOURNAMENT_ID, tournamentId)
                    .query();
            for (TournamentPlayer tournamentPlayer : tournamentPlayers) {
                res.add(allPlayers.get(tournamentPlayer.getPlayerId()));
            }
            return res;
        }
        catch (SQLException e) {
            return res;
        }
    }

    @Override
    public List<Player> getTournamentPlayersComplement(Context context, long tournamentId) {
        Tournament tournament = getById(context, tournamentId);
        try {
            List<CompetitionPlayer> competitionPlayers = sportDBHelper.getCompetitionPlayerDAO().queryBuilder()
                    .where().eq(DBConstants.cCOMPETITION_ID, tournament.getCompetitionId()).query();
            Map<Long, Player> allPlayers = corePlayerManager.getAllPlayers(context);
            Map<Long, Player> allCompetitionPlayers = new HashMap<>();
            for (CompetitionPlayer competitionPlayer : competitionPlayers) {
                allCompetitionPlayers.put(competitionPlayer.getPlayerId(), allPlayers.get(competitionPlayer.getPlayerId()));
            }
            List<TournamentPlayer> tournamentPlayers = sportDBHelper.getTournamentPlayerDAO().queryBuilder()
                    .where()
                    .eq(DBConstants.cTOURNAMENT_ID, tournamentId)
                    .query();
            for (TournamentPlayer competitionPlayer : tournamentPlayers) {
                allCompetitionPlayers.remove(competitionPlayer.getPlayerId());
            }
            return new ArrayList<>(allCompetitionPlayers.values());
        }
        catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    List<Player> getTeamPlayers(Context context, Team team) {
        try {
            List<TeamPlayer> teamPlayers;
            List<Player> players = new ArrayList<>();
            Map<Long, Player> allPlayers = corePlayerManager.getAllPlayers(context);

            teamPlayers = sportDBHelper.getTeamPlayerDAO().queryBuilder()
                    .where().eq(DBConstants.cTEAM_ID, team.getId()).query();
            for (TeamPlayer teamPlayer : teamPlayers) {
                players.add(allPlayers.get(teamPlayer.getPlayerId()));
            }
            return players;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean removePlayerFromTournament(Context context, long playerId, long tournamentId) {
        try {
            Tournament tournament = sportDBHelper.getTournamentDAO().queryForId(tournamentId);
            Competition competition = sportDBHelper.getCompetitionDAO().queryForId(tournament.getCompetitionId());
            competition.setType(CompetitionTypes.competitionTypes()[competition.getTypeId()]);

            if (CompetitionTypes.teams().equals(competition.getType())) {
                // Check if some team contains player
                List<Team> teams = sportDBHelper.getTeamDAO().queryBuilder()
                        .where().eq(DBConstants.cTOURNAMENT_ID, tournamentId).query();
                for (Team team : teams) {
                    List<Player> teamPlayers = getTeamPlayers(context, team);
                    for (Player player : teamPlayers)
                        if (player.getId() == playerId)
                            return false;
                }
            }

            // Check if player participates in match
            List<Match> matches = sportDBHelper.getMatchDAO().queryBuilder()
                    .where().eq(DBConstants.cTOURNAMENT_ID, tournamentId).query();
            for (Match match : matches) {
                List<Participant> matchParticipants = sportDBHelper.getParticipantDAO().queryBuilder()
                        .where().eq(DBConstants.cMATCH_ID, match.getId()).query();
                for (Participant participant : matchParticipants) {
                    // Check if some participant is player
                    if (CompetitionTypes.individuals().equals(competition.getType()) && participant.getParticipantId() == playerId)
                        return false;

                    List<PlayerStat> playerStats = sportDBHelper.getPlayerStatDAO().queryBuilder()
                            .where().eq(DBConstants.cPARTICIPANT_ID, participant.getId()).query();
                    for (PlayerStat playerStat : playerStats)
                        if (playerStat.getPlayerId() == playerId)
                            return false;
                }
            }

            // Remove player
            Dao<TournamentPlayer, Long> tournamentPlayerDao = sportDBHelper.getTournamentPlayerDAO();
            DeleteBuilder<TournamentPlayer, Long> tournamentPlayerBuilder = tournamentPlayerDao.deleteBuilder();
            tournamentPlayerBuilder.where()
                    .eq(DBConstants.cTOURNAMENT_ID, tournamentId)
                    .and()
                    .eq(DBConstants.cPLAYER_ID, playerId);
            tournamentPlayerBuilder.delete();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public void addPlayer(Context context, long playerId, long tournamentId) {
        Dao<TournamentPlayer, Long> tournamentPlayerDao = sportDBHelper.getTournamentPlayerDAO();
        try {
            tournamentPlayerDao.create(new TournamentPlayer(tournamentId, playerId));
        } catch (SQLException e) {}
    }
}
