package fit.cvut.org.cz.hockey.business;

import android.content.Context;

import fit.cvut.org.cz.hockey.data.DAO.CompetitionDAO;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.data.entities.DCompetition;

/**
 * Created by atgot_000 on 30. 3. 2016.
 */
public class CompetitionManager implements ICompetitionManager {

    private CompetitionDAO competitionDAO = new CompetitionDAO();

    @Override
    public void insert(Context context, Competition competition) {
        DCompetition dc = Competition.convertToDCompetition(competition);
        competitionDAO.insert(context, dc);
    }

    @Override
    public void update(Context context, Competition competition) {

    }

    @Override
    public void delete(Context context, long id) {

    }

    @Override
    public Competition getById(Context context, long id) {
        return null;
    }
}
