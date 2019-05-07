package fit.cvut.org.cz.bowling.business.managers;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentPlayer;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;

public class TournamentManager extends fit.cvut.org.cz.tmlibrary.business.managers.TournamentManager {
    @Override
    public void insert(Tournament tournament) {
        try {
            super.insert(tournament);
            PointConfiguration pointConfiguration = (PointConfiguration) tournament.getPointConfiguration();
            if (tournament.getPointConfiguration() == null) {
                pointConfiguration = PointConfiguration.defaultConfig();
            }
            pointConfiguration.setTournamentId(tournament.getId());
            managerFactory.getDaoFactory().getMyDao(PointConfiguration.class).create(pointConfiguration);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(long id) {
        if (!super.delete(id))
            return false;

        try {
            // delete matches
            List<Match> matches = managerFactory.getDaoFactory().getMyDao(Match.class).getListItemById(DBConstants.cTOURNAMENT_ID, id);
            for (Match match : matches) {
                managerFactory.getEntityManager(Match.class).delete(match.getId());
            }

            // delete teams
            List<Team> teams = managerFactory.getDaoFactory().getMyDao(Team.class).getListItemById(DBConstants.cTOURNAMENT_ID, id);
            for (Team team : teams) {
                managerFactory.getEntityManager(Team.class).delete(team.getId());
            }

            // delete tournament players
            Dao<TournamentPlayer, Long> tournamentPlayerDao = managerFactory.getDaoFactory().getMyDao(TournamentPlayer.class);
            List<TournamentPlayer> players = managerFactory.getDaoFactory().getMyDao(TournamentPlayer.class).getListItemById(DBConstants.cTOURNAMENT_ID, id);
            for (TournamentPlayer player : players) {
                tournamentPlayerDao.deleteById(player.getId());
            }

            managerFactory.getDaoFactory().getMyDao(PointConfiguration.class).deleteById(id);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
