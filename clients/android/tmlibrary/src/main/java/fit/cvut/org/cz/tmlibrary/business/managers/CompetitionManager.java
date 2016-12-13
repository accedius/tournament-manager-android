package fit.cvut.org.cz.tmlibrary.business.managers;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.CompetitionPlayer;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.business.enums.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICorePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.SportDBHelper;

/**
 * Created by kevin on 30. 3. 2016.
 */
abstract public class CompetitionManager extends BaseManager<Competition> implements ICompetitionManager {
    public CompetitionManager(Context context, ICorePlayerManager corePlayerManager, SportDBHelper sportDBHelper) {
        super(context, corePlayerManager, sportDBHelper);
    }

    @Override
    public boolean delete(long id) {
        try {
            List<CompetitionPlayer> competitionPlayers = sportDBHelper.getCompetitionPlayerDAO().queryForEq(DBConstants.cCOMPETITION_ID, id);
            if (!competitionPlayers.isEmpty())
                return false;

            List<Tournament> tournaments = sportDBHelper.getTournamentDAO().queryForEq(DBConstants.cCOMPETITION_ID, id);
            if (!tournaments.isEmpty())
                return false;

            getDao().deleteById(id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Competition getById(long id) {
        try {
            Competition c = getDao().queryForId(id);
            c.setType(CompetitionTypes.competitionTypes()[c.getTypeId()]);
            return c;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Player> getCompetitionPlayers(long competitionId) {
        Map<Long, Player> allPlayers = corePlayerManager.getAllPlayers();
        List<Player> res = new ArrayList<>();
        try {
            List<CompetitionPlayer> competitionPlayers = sportDBHelper.getCompetitionPlayerDAO()
                    .queryForEq(DBConstants.cCOMPETITION_ID, competitionId);
            for (CompetitionPlayer competitionPlayer : competitionPlayers) {
                res.add(allPlayers.get(competitionPlayer.getPlayerId()));
            }
            return res;
        }
        catch (SQLException e) {
            return res;
        }
    }

    @Override
    public List<Player> getCompetitionPlayersComplement(long competitionId) {
        Map<Long, Player> allPlayers = corePlayerManager.getAllPlayers();
        try {
            List<CompetitionPlayer> competitionPlayers = sportDBHelper.getCompetitionPlayerDAO()
                    .queryForEq(DBConstants.cCOMPETITION_ID, competitionId);
            for (CompetitionPlayer competitionPlayer : competitionPlayers) {
                allPlayers.remove(competitionPlayer.getPlayerId());
            }
            return new ArrayList<>(allPlayers.values());
        }
        catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void addPlayer(Competition competition, Player player) {
        if (competition == null || player == null)
            return;
        Dao<CompetitionPlayer, Long> competitionPlayerDao = sportDBHelper.getCompetitionPlayerDAO();

        try {
            competitionPlayerDao.create(new CompetitionPlayer(competition.getId(), player.getId()));
        } catch (SQLException e) {}
    }

    @Override
    public boolean removePlayerFromCompetition(long playerId, long competitionId) {
        try {
            List<Tournament> tournaments = sportDBHelper.getTournamentDAO()
                    .queryForEq(DBConstants.cCOMPETITION_ID, competitionId);
            for (Tournament tournament : tournaments) {
                List<TournamentPlayer> tournamentPlayers;
                List<Player> players = new ArrayList<>();
                Map<Long, Player> allPlayers = corePlayerManager.getAllPlayers();

                tournamentPlayers = sportDBHelper.getTournamentPlayerDAO()
                        .queryForEq(DBConstants.cTOURNAMENT_ID, tournament.getId());
                for (TournamentPlayer tournamentPlayer : tournamentPlayers) {
                    players.add(allPlayers.get(tournamentPlayer.getPlayerId()));
                }
                for (Player player : players)
                    if (player.getId() == playerId)
                        return false;
            }

            // Remove player
            Dao<CompetitionPlayer, Long> competitionPlayerDao = sportDBHelper.getCompetitionPlayerDAO();
            DeleteBuilder<CompetitionPlayer, Long> deleteBuilder = competitionPlayerDao.deleteBuilder();
            deleteBuilder.where()
                        .eq(DBConstants.cCOMPETITION_ID, competitionId).and()
                        .eq(DBConstants.cPLAYER_ID, playerId);
            deleteBuilder.delete();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
