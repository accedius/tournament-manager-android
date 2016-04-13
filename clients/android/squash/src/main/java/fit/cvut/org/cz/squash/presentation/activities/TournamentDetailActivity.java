package fit.cvut.org.cz.squash.presentation.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import fit.cvut.org.cz.squash.presentation.fragments.AgregatedStatsListFragment;
import fit.cvut.org.cz.squash.presentation.fragments.MatchListFragment;
import fit.cvut.org.cz.squash.presentation.fragments.SquashMatchesListWrapperFragment;
import fit.cvut.org.cz.squash.presentation.services.StatsService;
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
                new Fragment[]{
                        SquashMatchesListWrapperFragment.newInstance(id, SquashMatchesListWrapperFragment.class),
                        AgregatedStatsListFragment.newInstance(id, StatsService.ACTION_GET_STATS_BY_TOURNAMENT)
                },
                new String[]{"Matches", "Players && stats"});
        //return null;
    }
}
