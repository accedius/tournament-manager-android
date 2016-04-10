package fit.cvut.org.cz.squash.presentation.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import fit.cvut.org.cz.squash.presentation.fragments.MatchListFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public class TournamentDetailActivity extends AbstractTabActivity {

    public static final String EXTRA_ID = "extra_id";


    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {

        long id = getIntent().getLongExtra(EXTRA_ID, -1);

        return new DefaultViewPagerAdapter(manager,
                new Fragment[]{MatchListFragment.newInstance(id)},
                new String[]{"Matches"});
        //return null;
    }
}
