package fit.cvut.org.cz.bowling.data.helpers;

import android.content.res.Resources;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;

public class CompetitionTypes extends fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes {
    public final static int type_none = -1;
    protected final static int type_none_index = 0;
    protected final static int type_individuals_index = 1;
    protected final static int type_teams_index = 2;

    /**
     * Get all competition types.
     * @param resources resources for translation
     * @return all competition types
     */
    public static CompetitionType[] competitionTypes(Resources resources) {
        CompetitionType [] list = new CompetitionType[3];
        list[type_none_index] = new CompetitionType(resources, type_none, R.string.type_none);
        list[type_individuals_index] = new CompetitionType(resources, type_individuals, fit.cvut.org.cz.tmlibrary.R.string.type_individuals);
        list[type_teams_index] = new CompetitionType(resources, type_teams, fit.cvut.org.cz.tmlibrary.R.string.type_teams);
        return list;
    }

    /**
     * Get competition types without need for text translation.
     * @return all competition types
     */
    public static CompetitionType [] competitionTypes() {
        CompetitionType [] list = new CompetitionType[3];
        list[type_none_index] = none();
        list[type_individuals_index] = individuals();
        list[type_teams_index] = teams();
        return list;
    }

    /**
     * Get none competition type.
     * @return none competition type
     */
    public static CompetitionType none() {
        return new CompetitionType(null, type_none, R.string.type_none);
    }

    /**
     * Get individual competition type.
     * @return individual competition type
     */
    public static CompetitionType individuals() {
        return new CompetitionType(null, type_individuals, fit.cvut.org.cz.tmlibrary.R.string.type_individuals);
    }

    /**
     * Get team competition type.
     * @return team competition type
     */
    public static CompetitionType teams() {
        return new CompetitionType(null, type_teams, fit.cvut.org.cz.tmlibrary.R.string.type_teams);
    }

    /**
     * Get Competition's type from typeId
     * @param typeId typeId of Competition
     * @return corresponding Competition type
     */
    public static CompetitionType getTypeByTypeId(int typeId) {
        switch (typeId) {
            case type_teams:
                return teams();
            case type_individuals:
                return individuals();
            case type_none:
                return none();
            default:
                return none();
        }
    }

    /**
     * Get type's index in return list of method competitionTypes()
     * @param typeId typeId of Competition
     * @return corresponding index
     */
    public static int getIndexByTypeId (int typeId) {
        switch (typeId) {
            case type_teams:
                return type_teams_index;
            case type_individuals:
                return type_individuals_index;
            case type_none:
                return type_none_index;
            default:
                return type_none_index;
        }
    }
}
