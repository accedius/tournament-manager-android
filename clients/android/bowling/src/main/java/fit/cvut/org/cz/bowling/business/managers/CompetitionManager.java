package fit.cvut.org.cz.bowling.business.managers;


import java.sql.SQLException;

import fit.cvut.org.cz.bowling.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ICompetitionManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;

public class CompetitionManager extends fit.cvut.org.cz.tmlibrary.business.managers.CompetitionManager implements ICompetitionManager {
    @Override
    public Competition getById(long id) {
        try {
            Competition competition = managerFactory.getDaoFactory()
                    .getMyDao(Competition.class).queryForId(id);
            if (competition == null)
                return competition;

            competition.setType(CompetitionTypes.getTypeByTypeId(competition.getTypeId()) );
            return competition;
        } catch (SQLException e) {
            return null;
        }
    }
}
