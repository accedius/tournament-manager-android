package fit.cvut.org.cz.squash.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import fit.cvut.org.cz.tmlibrary.presentation.fragments.MatchesListWrapperFragment;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public class SquashMatchesListWrapperFragment extends MatchesListWrapperFragment {

    @Override
    protected Fragment supplyFragment(long tournamentId) {
        return MatchListFragment.newInstance(tournamentId);
    }
}
