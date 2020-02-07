package fit.cvut.org.cz.bowling.presentation.fragments;

import android.os.Bundle;
import android.os.Parcelable;

import java.util.List;

import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

public abstract class BowlingAbstractMatchStatsListFragment<T extends Parcelable> extends AbstractListFragment<T> {
    public static final String PARTICIPANT_STATS_TO_CREATE = ExtraConstants.PARTICIPANT_STATS_TO_CREATE;
    public static final String PARTICIPANT_STATS_TO_UPDATE = ExtraConstants.PARTICIPANT_STATS_TO_UPDATE;

    public static final String PLAYER_STATS_TO_CREATE = ExtraConstants.PLAYER_STATS_TO_CREATE;
    public static final String PLAYER_STATS_TO_UPDATE = ExtraConstants.PLAYER_STATS_TO_UPDATE;

    public static final String FRAMES_TO_CREATE = ExtraConstants.FRAMES_TO_CREATE;
    public static final String FRAMES_TO_UPDATE = ExtraConstants.FRAMES_TO_UPDATE;
    public static final String FRAMES_TO_DELETE = ExtraConstants.FRAMES_TO_DELETE;
    public static final String NOT_CHANGED_FRAMES_BUT_TO_ADD_ROLLS_TO = ExtraConstants.NOT_CHANGED_FRAMES_BUT_TO_ADD_ROLLS_TO;

    public static final String ROLLS_TO_CREATE = ExtraConstants.ROLLS_TO_CREATE;
    public static final String ROLLS_TO_UPDATE = ExtraConstants.ROLLS_TO_UPDATE;
    public static final String ROLLS_TO_DELETE = ExtraConstants.ROLLS_TO_DELETE;

    public static final String EXTRA_BOOLEAN_IS_MATCH_PLAYED = ExtraConstants.EXTRA_BOOLEAN_IS_MATCH_PLAYED;

    /**
     * Get match stats (overall or with frames and rolls) wrapped in bundle with different flags (..._TO_CREATE ..._TO_DELETE ...TO_EDIT)
     * @return bundle with all the match data about stats grouped by flags
     */
    public abstract Bundle getMatchStats();

    /**
     * Get match stats (overall or with frames and rolls) wrapped in Participant entities
     * @return List of current participants (inside match activity we have an option to add/delete players thus participants too) with their stats
     */
    public abstract List<Participant> getMatchParticipants();
}
