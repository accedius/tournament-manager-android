package fit.cvut.org.cz.squash.presentation.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import fit.cvut.org.cz.squash.presentation.fragments.AgregatedStatsListFragment;
import fit.cvut.org.cz.squash.presentation.fragments.SquashCompetitionOverviewFragment;
import fit.cvut.org.cz.squash.presentation.fragments.TournamentsListFragment;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.CompetitionOverviewFragment;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public class CompetitionDetailActivity extends AbstractTabActivity {
    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        Long id = getIntent().getExtras().getLong(CrossPackageComunicationConstants.EXTRA_ID);
        return new DefaultViewPagerAdapter(getSupportFragmentManager(),
                new Fragment[]{CompetitionOverviewFragment.newInstance(id, SquashCompetitionOverviewFragment.class), TournamentsListFragment.newInstance(id), AgregatedStatsListFragment.newInstance(id)},
                new String[] {"Overview", "Tournaments", "Stats && Players"});
    }
}
