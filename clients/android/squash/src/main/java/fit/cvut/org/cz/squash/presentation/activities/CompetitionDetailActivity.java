package fit.cvut.org.cz.squash.presentation.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import fit.cvut.org.cz.squash.presentation.fragments.AgregatedStatsListFragment;
import fit.cvut.org.cz.squash.presentation.fragments.SquashCompetitionOverviewFragment;
import fit.cvut.org.cz.squash.presentation.fragments.TournamentsListFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.CompetitionOverviewFragment;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public class CompetitionDetailActivity extends AbstractTabActivity {
    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        return new DefaultViewPagerAdapter(getSupportFragmentManager(),
                new Fragment[]{CompetitionOverviewFragment.newInstance(1, SquashCompetitionOverviewFragment.class), TournamentsListFragment.newInstance(1), AgregatedStatsListFragment.newInstance(1)},
                new String[] {"Overview", "Tournaments", "Stats && Players"});
    }

    //TODO pass id
}
