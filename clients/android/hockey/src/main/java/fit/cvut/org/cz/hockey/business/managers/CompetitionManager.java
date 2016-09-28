package fit.cvut.org.cz.hockey.business.managers;

import android.content.Context;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.business.ManagerFactory;
import fit.cvut.org.cz.hockey.data.DAOFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;

/**
 * Created by atgot_000 on 30. 3. 2016.
 */
public class CompetitionManager implements ICompetitionManager {
    @Override
    public void insert(Context context, Competition competition) {
        DCompetition dc = Competition.convertToDCompetition(competition);
        DAOFactory.getInstance().competitionDAO.insert(context, dc);
    }

    @Override
    public void update(Context context, Competition competition) {
        DCompetition dc = Competition.convertToDCompetition(competition);
        DAOFactory.getInstance().competitionDAO.update(context, dc);
    }

    @Override
    public boolean delete(Context context, long id) {
        ArrayList<Tournament> tournaments = ManagerFactory.getInstance().tournamentManager.getByCompetitionId(context, id);
        if (tournaments.size() > 0) return false;

        ArrayList<Long> playerIds = DAOFactory.getInstance().packagePlayerDAO.getPlayerIdsByCompetition(context, id);
        if (playerIds.size() > 0) return false;

        DAOFactory.getInstance().competitionDAO.delete(context, id);
        return true;
    }

    @Override
    public Competition getById(Context context, long id) {
        DCompetition dc = DAOFactory.getInstance().competitionDAO.getById(context, id);
        if (dc == null)
            return null;
        Competition c = new Competition(dc);
        return c;
    }
}
