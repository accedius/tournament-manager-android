package fit.cvut.org.cz.tmlibrary.business.managers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionPlayer;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

/**
 * Competition Manager.
 */
public class CompetitionManager extends BaseManager<Competition> implements ICompetitionManager {
    @Override
    protected Class<Competition> getMyClass() {
        return Competition.class;
    }

    @Override
    public boolean delete(long id) {
        try {
            // delete tournaments
            List<Tournament> tournaments = managerFactory.getDaoFactory()
                    .getMyDao(Tournament.class).queryForEq(DBConstants.cCOMPETITION_ID, id);
            for (Tournament tournament : tournaments) {
                managerFactory.getEntityManager(Tournament.class).delete(tournament.getId());
            }

            // delete competition players
            Dao<CompetitionPlayer, Long> competitionPlayerDao = managerFactory.getDaoFactory().getMyDao(CompetitionPlayer.class);
            List<CompetitionPlayer> competitionPlayers = competitionPlayerDao.queryForEq(DBConstants.cCOMPETITION_ID, id);
            for (CompetitionPlayer competitionPlayer : competitionPlayers) {
                competitionPlayerDao.deleteById(competitionPlayer.getId());
            }

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
            ArrayList<Player> players = new ArrayList<>(allPlayers.values());
            PackagePlayerManager.orderPlayers(players);
            return players;
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

            // Check if some tournament in this competition contains this player
            String query =  "SELECT * " +
                            "FROM " + DBConstants.tTOURNAMENTS + " t " +
                                "JOIN " + DBConstants.tPLAYERS_IN_TOURNAMENT + " p " +
                                    "ON t." + DBConstants.cID + " = p." + DBConstants.cTOURNAMENT_ID +
                            " WHERE "+
                                    "t." + DBConstants.cCOMPETITION_ID + " = " + competitionId +
                                " AND "+
                                    "p." + DBConstants.cPLAYER_ID + " = " + playerId;
            GenericRawResults<String[]> results = managerFactory.getDaoFactory().getMyDao(Tournament.class).queryRaw(query);
            if (!results.getResults().isEmpty()) {
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
            e.printStackTrace();
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
