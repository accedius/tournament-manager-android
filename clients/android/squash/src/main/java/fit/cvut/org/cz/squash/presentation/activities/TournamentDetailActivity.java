package fit.cvut.org.cz.squash.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.fragments.SquashMatchesListWrapperFragment;
import fit.cvut.org.cz.squash.presentation.fragments.SquashTournamentOverviewFragment;
import fit.cvut.org.cz.squash.presentation.fragments.StandingsWrapperFragment;
import fit.cvut.org.cz.squash.presentation.fragments.StatsListWrapperFragment;
import fit.cvut.org.cz.squash.presentation.fragments.TeamsListFragment;
import fit.cvut.org.cz.squash.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.MatchesListWrapperFragment;

/**
 * This acitivity includes all necessary fragments to display Detail of tournament
 * Created by Vaclav on 10. 4. 2016.
 */
public class TournamentDetailActivity extends AbstractTabActivity {

    public static final String EXTRA_ID = "extra_id";
    public static final String EXTRA_TYPE = "extra_type";
    private DefaultViewPagerAdapter adapter = null;


    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {

        long id = getIntent().getLongExtra(EXTRA_ID, -1);
        CompetitionType type = (CompetitionType) getIntent().getSerializableExtra(EXTRA_TYPE);

        if (type == CompetitionType.Teams) {
            adapter =  new DefaultViewPagerAdapter(manager,
                    new Fragment[]{
                            SquashTournamentOverviewFragment.newInstance(id, type),
                            StandingsWrapperFragment.newInstance(id),
                            SquashMatchesListWrapperFragment.newInstance(id, SquashMatchesListWrapperFragment.class),
                            TeamsListFragment.newInstance(id),
                            StatsListWrapperFragment.newInstance(id, StatsService.ACTION_GET_STATS_BY_TOURNAMENT)
                    },
                    new String[]{getResources().getString(R.string.overview), getResources().getString(R.string.standings), getResources().getString(R.string.matches), getResources().getString(R.string.teams), getResources().getString(R.string.sap)});
        } else {
            adapter =  new DefaultViewPagerAdapter(manager,
                    new Fragment[]{
                            SquashTournamentOverviewFragment.newInstance(id, type),
                            StandingsWrapperFragment.newInstance(id),
                            SquashMatchesListWrapperFragment.newInstance(id, SquashMatchesListWrapperFragment.class),
                            StatsListWrapperFragment.newInstance(id, StatsService.ACTION_GET_STATS_BY_TOURNAMENT)
                    },
                    new String[]{getResources().getString(R.string.overview), getResources().getString(R.string.standings), getResources().getString(R.string.matches), getResources().getString(R.string.sap)});
        }
        return adapter;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fr = getSupportFragmentManager().findFragmentByTag(adapter.getTag(position));
                if (fr != null){
                    if (fr instanceof AbstractDataFragment) ((AbstractDataFragment) fr).customOnResume();
                    if (fr instanceof MatchesListWrapperFragment) ((MatchesListWrapperFragment) fr).refresh();
                    if (fr instanceof StandingsWrapperFragment) ((StandingsWrapperFragment) fr).refresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_tournament_detail, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case fit.cvut.org.cz.tmlibrary.R.id.action_point_config:{
                Intent intent = new Intent(this, PointConfigActivity.class);
                intent.putExtra(PointConfigActivity.ARG_ID, getIntent().getLongExtra(EXTRA_ID, -1));
                startActivity(intent);
                return true;
            }
        }

        return false;
    }
}
