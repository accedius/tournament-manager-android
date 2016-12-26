package fit.cvut.org.cz.tmlibrary.business.managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentPlayer;

/**
 * Created by kevin on 30. 3. 2016.
 */
public class CompetitionManager extends TManager<Competition> implements ICompetitionManager {
    @Override
    protected Class<Competition> getMyClass() {
        return Competition.class;
    }

    @Override
    public boolean delete(long id) {
        try {
            List<CompetitionPlayer> competitionPlayers = managerFactory.getDaoFactory()
                    .getMyDao(CompetitionPlayer.class).queryForEq(DBConstants.cCOMPETITION_ID, id);
            if (!competitionPlayers.isEmpty())
                return false;

            List<Tournament> tournaments = managerFactory.getDaoFactory()
                    .getMyDao(Tournament.class).queryForEq(DBConstants.cCOMPETITION_ID, id);
            if (!tournaments.isEmpty())
                return false;

            managerFactory.getDaoFactory()
                    .getMyDao(Competition.class).deleteById(id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Competition getById(long id) {
        try {
            Competition competition = managerFactory.getDaoFactory()
                    .getMyDao(Competition.class).queryForId(id);
            if (competition == null)
                return competition;

            competition.setType(CompetitionTypes.competitionTypes()[competition.getTypeId()]);
            return competition;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public List<Player> getCompetitionPlayers(long competitionId) {
        Map<Long, Player> allPlayers = ((IPackagePlayerManager)managerFactory.getEntityManager(Player.class)).getMapAll();
        List<Player> res = new ArrayList<>();
        try {
            List<CompetitionPlayer> competitionPlayers = managerFactory.getDaoFactory()
                    .getMyDao(CompetitionPlayer.class).queryForEq(DBConstants.cCOMPETITION_ID, competitionId);
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
        Map<Long, Player> allPlayers = ((IPackagePlayerManager)managerFactory.getEntityManager(Player.class)).getMapAll();
        try {
            List<CompetitionPlayer> competitionPlayers = managerFactory.getDaoFactory()
                    .getMyDao(CompetitionPlayer.class).queryForEq(DBConstants.cCOMPETITION_ID, competitionId);
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

        try {
            Dao<CompetitionPlayer, Long> competitionPlayerDao = managerFactory.getDaoFactory()
                    .getMyDao(CompetitionPlayer.class);
            competitionPlayerDao.create(new CompetitionPlayer(competition.getId(), player.getId()));
        } catch (SQLException e) {}
    }

    @Override
    public boolean removePlayerFromCompetition(long playerId, long competitionId) {
        try {
            List<Tournament> tournaments = managerFactory.getDaoFactory()
                    .getMyDao(Tournament.class).queryForEq(DBConstants.cCOMPETITION_ID, competitionId);
            for (Tournament tournament : tournaments) {
                List<TournamentPlayer> tournamentPlayers;
                List<Player> players = new ArrayList<>();
                Map<Long, Player> allPlayers = ((IPackagePlayerManager)managerFactory.getEntityManager(Player.class)).getMapAll();

                tournamentPlayers = managerFactory.getDaoFactory()
                        .getMyDao(TournamentPlayer.class).queryForEq(DBConstants.cTOURNAMENT_ID, tournament.getId());
                for (TournamentPlayer tournamentPlayer : tournamentPlayers) {
                    players.add(allPlayers.get(tournamentPlayer.getPlayerId()));
                }
                for (Player player : players)
                    if (player.getId() == playerId)
                        return false;
            }

            // Remove player
            Dao<CompetitionPlayer, Long> competitionPlayerDao = managerFactory.getDaoFactory()
                    .getMyDao(CompetitionPlayer.class);
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

    @Override
    public List<Tournament> getByCompetitionId(long competitionId) {
        try {
            return managerFactory.getDaoFactory()
                    .getMyDao(Tournament.class).queryForEq(DBConstants.cCOMPETITION_ID, competitionId);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}
