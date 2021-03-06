package fit.cvut.org.cz.tmlibrary.data.helpers;

import android.content.res.Resources;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;

/**
 * Class for types of Competition.
 */
public class CompetitionTypes {
    /**
     * Get all competition types.
     * @param resources resources for translation
     * @return all competition types
     */
    public static CompetitionType[] competitionTypes(Resources resources) {
        CompetitionType [] list = new CompetitionType[2];
        list[0] = new CompetitionType(resources, 0, R.string.type_individuals);
        list[1] = new CompetitionType(resources, 1, R.string.type_teams);
        return list;
    }

    /**
     * Get competition types without need for text translation.
     * @return all competition types
     */
    public static CompetitionType [] competitionTypes() {
        CompetitionType [] list = new CompetitionType[2];
        list[0] = individuals();
        list[1] = teams();
        return list;
    }

    /**
     * Get individual competition type.
     * @return individual competition type
     */
    public static CompetitionType individuals() {
        return new CompetitionType(null, 0, R.string.type_individuals);
    }

    /**
     * Get team competition type.
     * @return team competition type
     */
    public static CompetitionType teams() {
        return new CompetitionType(null, 1, R.string.type_teams);
    }
}
