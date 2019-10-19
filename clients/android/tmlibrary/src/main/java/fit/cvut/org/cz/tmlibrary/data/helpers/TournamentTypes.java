package fit.cvut.org.cz.tmlibrary.data.helpers;

import android.content.res.Resources;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;

/**
 * Class for processing tournament types
 */
public class TournamentTypes {
    public final static int type_teams = 0;
    public final static int type_individuals = 1;

    /**
     * Get all Tournament types.
     * @param resources resources for translation
     * @return all Tournament types
     */
    public static TournamentType[] tournamentTypes(Resources resources) {
        TournamentType [] list = new TournamentType[2];
        list[1] = new TournamentType(resources, 1, R.string.type_individuals);
        list[0] = new TournamentType(resources, 0, R.string.type_teams);
        return list;
    }

    /**
     * Get Tournament types without need for text translation.
     * @return all Tournament types
     */
    public static TournamentType [] tournamentTypes() {
        TournamentType [] list = new TournamentType[2];
        list[1] = individuals();
        list[0] = teams();
        return list;
    }

    /**
     * Get individual Tournament type.
     * @return individual Tournament type
     */
    public static TournamentType individuals() {
        return new TournamentType(null, 1, R.string.type_individuals);
    }

    /**
     * Get team Tournament type.
     * @return team Tournament type
     */
    public static TournamentType teams() {
        return new TournamentType(null, 0, R.string.type_teams);
    }

    /**
     * Get Tournament's type from typeId
     * @param typeId typeId of Tournament
     * @return corresponding Tournament type
     */
    public static TournamentType getMyTournamentType(int typeId) {
        switch (typeId) {
            case type_teams:
                return teams();
            case type_individuals:
                return individuals();
            default:
                return teams();
        }
    }

    /**
     * Get Tournament's type from type of Competition to which this Tournament belongs.
     * @param type type Competition
     * @return corresponding Tournament type
     */
    public static TournamentType getCorrespondingType(CompetitionType type) {
        switch (type.id) {
            case CompetitionTypes.type_teams:
                return teams();
            case CompetitionTypes.type_individuals:
                return individuals();
            default:
                return teams();
        }
    }
}

