package fit.cvut.org.cz.bowling.business.managers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.data.entities.WinCondition;
import fit.cvut.org.cz.tmlibrary.data.entities.EntityDAO;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.data.interfaces.IEntityDAO;

public class TournamentManager extends fit.cvut.org.cz.tmlibrary.business.managers.TournamentManager {
    @Override
    public void insert(Tournament tournament) {
        try {
            super.insert(tournament);
            PointConfiguration pointConfiguration = PointConfiguration.defaultConfig();//(PointConfiguration) tournament.getPointConfiguration();
            if (tournament.getPointConfiguration() == null) {
                pointConfiguration = PointConfiguration.defaultConfig();
            }
            pointConfiguration.setTournamentId(tournament.getId());
            EntityDAO<PointConfiguration, Long> pointConfigurationDAO = managerFactory.getDaoFactory().getMyDao(PointConfiguration.class);
            pointConfigurationDAO.create(pointConfiguration);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(long id) {
        if (!super.delete(id))
            return false;

        try {
            // delete matches
            EntityDAO<Match, Long> matchDAO = managerFactory.getDaoFactory().getMyDao(Match.class);
            List<Match> matches = matchDAO.getListItemById(DBConstants.cTOURNAMENT_ID, id);
            for (Match match : matches) {
                managerFactory.getEntityManager(Match.class).delete(match.getId());
            }

            // delete teams
            EntityDAO<Team, Long> teamDAO = managerFactory.getDaoFactory().getMyDao(Team.class);
            List<Team> teams = teamDAO.getListItemById(DBConstants.cTOURNAMENT_ID, id);
            for (Team team : teams) {
                managerFactory.getEntityManager(Team.class).delete(team.getId());
            }

            // delete tournament players
            EntityDAO<TournamentPlayer, Long> tournamentPlayerDAO = managerFactory.getDaoFactory().getMyDao(TournamentPlayer.class);
            List<TournamentPlayer> players = tournamentPlayerDAO.getListItemById(DBConstants.cTOURNAMENT_ID, id);
            for (TournamentPlayer player : players) {
                tournamentPlayerDAO.deleteById(player.getId());
            }

            //delete point configurations
            EntityDAO<PointConfiguration, Long> pointConfigurationDAO = managerFactory.getDaoFactory().getMyDao(PointConfiguration.class);
            List<PointConfiguration> pointConfigurations = pointConfigurationDAO.getListItemById(DBConstants.cTOURNAMENT_ID, id);
            for (PointConfiguration pointConfiguration : pointConfigurations) {
                pointConfigurationDAO.deleteById(pointConfiguration.getId());
            }

            // delete win condition
            final WinConditionManager winConditionManager = managerFactory.getEntityManager(WinCondition.class);
            if(winConditionManager.getByTournamentId(id) != null) {
                winConditionManager.deleteByTournamentId(id);
            }

            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
