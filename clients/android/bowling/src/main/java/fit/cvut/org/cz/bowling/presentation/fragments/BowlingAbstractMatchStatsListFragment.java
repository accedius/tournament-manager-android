package fit.cvut.org.cz.bowling.presentation.fragments;

import android.os.Parcelable;

import java.util.List;

import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

public abstract class BowlingAbstractMatchStatsListFragment<T extends Parcelable> extends AbstractListFragment<T> {

    /**
     * Get match stats (overall or with frames and rolls) wrapped in Participant entities
     * @return List of current participants (inside match activity we have an option to add/delete players thus participants too) with their stats
     */
    public abstract List<Participant> getMatchStats();
}
