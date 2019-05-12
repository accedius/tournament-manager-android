package fit.cvut.org.cz.bowling.presentation.fragments;

import android.support.v4.app.Fragment;

import fit.cvut.org.cz.tmlibrary.presentation.fragments.MatchesListWrapperFragment;

public class BowlingMatchesListWrapperFragment extends MatchesListWrapperFragment {
    @Override
    protected Fragment supplyFragment(long tournamentId) {
        return BowlingMatchesListFragment.newInstance(tournamentId);
    }
}
