package fit.cvut.org.cz.squash.presentation.fragments;

import android.support.v4.app.Fragment;

import fit.cvut.org.cz.tmlibrary.presentation.fragments.MatchesListWrapperFragment;

/**
 * Header for match list fragment
 * Created by Vaclav on 10. 4. 2016.
 */
public class SquashMatchesListWrapperFragment extends MatchesListWrapperFragment {

    @Override
    protected Fragment supplyFragment(long tournamentId) {
        return MatchListFragment.newInstance(tournamentId);
    }
}
