package fit.cvut.org.cz.tmlibrary.data.helpers;

import android.content.res.Resources;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;

/**
 * Class for types of Competition.
 */
public class CompetitionTypes {
    public static CompetitionType[] competitionTypes(Resources r) {
        CompetitionType [] list = new CompetitionType[2];
        list[0] = new CompetitionType(r, 0, R.string.type_individuals);
        list[1] = new CompetitionType(r, 1, R.string.type_teams);
        return list;
    }

    public static CompetitionType [] competitionTypes() {
        CompetitionType [] list = new CompetitionType[2];
        list[0] = individuals();
        list[1] = teams();
        return list;
    }

    public static CompetitionType individuals() {
        return new CompetitionType(null, 0, R.string.type_individuals);
    }

    public static CompetitionType teams() {
        return new CompetitionType(null, 1, R.string.type_teams);
    }
}
