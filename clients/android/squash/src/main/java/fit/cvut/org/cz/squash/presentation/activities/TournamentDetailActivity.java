package fit.cvut.org.cz.squash.presentation.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import fit.cvut.org.cz.squash.presentation.fragments.AgregatedStatsListFragment;
import fit.cvut.org.cz.squash.presentation.fragments.MatchListFragment;
import fit.cvut.org.cz.squash.presentation.fragments.SquashMatchesListWrapperFragment;
import fit.cvut.org.cz.squash.presentation.fragments.SquashTournamentOverviewFragment;
import fit.cvut.org.cz.squash.presentation.fragments.StandingsWrapperFragment;
import fit.cvut.org.cz.squash.presentation.fragments.StatsListWrapperFragment;
import fit.cvut.org.cz.squash.presentation.fragments.TeamsListFragment;
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
                        SquashTournamentOverviewFragment.newInstance(id, SquashTournamentOverviewFragment.class),
                        StandingsWrapperFragment.newInstance(id),
                        SquashMatchesListWrapperFragment.newInstance(id, SquashMatchesListWrapperFragment.class),
                        TeamsListFragment.newInstance(id),
                        StatsListWrapperFragment.newInstance(id, StatsService.ACTION_GET_STATS_BY_TOURNAMENT)
                },
                new String[]{"Overview", "standings", "Matches", "Teams", "Players && stats"});
    }
}
