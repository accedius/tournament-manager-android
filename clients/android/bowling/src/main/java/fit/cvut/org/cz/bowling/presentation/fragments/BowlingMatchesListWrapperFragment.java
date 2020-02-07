package fit.cvut.org.cz.bowling.presentation.fragments;

import android.support.v4.app.Fragment;

/**
 * Fragment is used in
 */
public class BowlingMatchesListWrapperFragment extends FFAMatchesListWrapperFragment {
    @Override
    protected Fragment supplyFragment(long tournamentId) {
        return BowlingMatchesListFragment.newInstance(tournamentId);
    }
}
