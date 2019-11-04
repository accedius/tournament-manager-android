package fit.cvut.org.cz.bowling.presentation.fragments;

import android.support.v4.app.Fragment;

import fit.cvut.org.cz.tmlibrary.presentation.fragments.MatchesListWrapperFragment;

/**
 * Fragment is used in
 */
public class BowlingMatchesListWrapperFragment extends FFAMatchesListWrapperFragment {
    @Override
    protected Fragment supplyFragment(long tournamentId) {
        return BowlingMatchesListFragment.newInstance(tournamentId);
    }
}
