package fit.cvut.org.cz.hockey.presentation.fragments;

import android.support.v4.app.Fragment;

import fit.cvut.org.cz.tmlibrary.presentation.fragments.MatchesListWrapperFragment;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class HockeyMatchesListWrapperFragment extends MatchesListWrapperFragment {

    @Override
    protected Fragment supplyFragment(long tournamentId) {
        return HockeyMatchesListFragment.newInstance( tournamentId );
    }
}
