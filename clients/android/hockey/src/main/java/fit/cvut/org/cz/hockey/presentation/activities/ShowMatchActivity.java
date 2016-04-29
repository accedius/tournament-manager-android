package fit.cvut.org.cz.hockey.presentation.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.business.entities.MatchScore;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyMatchOverviewFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyMatchStatsFragment;
import fit.cvut.org.cz.hockey.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;

/**
 * Created by atgot_000 on 22. 4. 2016.
 */
public class ShowMatchActivity extends AbstractTabActivity {

    private static String HEADER_OVERVIEW_MATCH = "Overview";
    private static String HEADER_PLAYER_STATS_MATCH = "Player statistics";

    private static final String MATCH_ID = "match_id";

    private long matchId;
    private View v;

    private ViewPager pager;

    private Fragment[] fragments;
    private String[] titles;

    @Override
    protected View injectView(ViewGroup parent) {
        v = super.injectView(parent);
        return v;
    }

    public static Intent newStartIntent( Context context, long matchId )
    {
        Intent intent = new Intent( context, ShowMatchActivity.class);
        Bundle b = new Bundle();

        b.putLong(MATCH_ID, matchId);
        intent.putExtras(b);

        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        matchId = getIntent().getExtras().getLong(MATCH_ID);

        titles = new String[]{ getString(R.string.header_overview), getString(R.string.header_players) };
        Fragment f1 = HockeyMatchOverviewFragment.newInstance( matchId );
        Fragment f2 = HockeyMatchStatsFragment.newInstance( matchId );
        fragments = new Fragment[]{ f1, f2 };

        super.onCreate(savedInstanceState);

        pager = (ViewPager) findViewById(fit.cvut.org.cz.tmlibrary.R.id.viewPager);
        pager.setAdapter(getAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(0);

        TabLayout tabLayout = (TabLayout) findViewById(fit.cvut.org.cz.tmlibrary.R.id.tabs);

        tabLayout.setupWithViewPager(pager);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        PagerAdapter res = new DefaultViewPagerAdapter(manager, fragments, titles);


        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_finish, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == fit.cvut.org.cz.tmlibrary.R.id.action_finish){
//            for( int i = 0; i < pager.getAdapter().getCount(); i++ ) {
//                ((HockeyMatchOverviewFragment)((DefaultViewPagerAdapter)(pager.getAdapter())).getItem( i )).testMethod();
//            }
            MatchScore score = ((HockeyMatchOverviewFragment) ((DefaultViewPagerAdapter) (pager.getAdapter())).getItem(0)).getScore();
            if( score.isShootouts() && (score.getHomeScore() == score.getAwayScore()))
            {
                Snackbar.make( findViewById(android.R.id.content), "Shootouts are checked, match must not be draw!", Snackbar.LENGTH_LONG ).show();
                return super.onOptionsItemSelected(item);
            }
            ArrayList<MatchPlayerStatistic> homeStats = ((HockeyMatchStatsFragment) ((DefaultViewPagerAdapter) (pager.getAdapter())).getItem(1)).getHomeList();
            ArrayList<MatchPlayerStatistic> awayStats = ((HockeyMatchStatsFragment) ((DefaultViewPagerAdapter) (pager.getAdapter())).getItem(1)).getAwayList();

            Intent intent = MatchService.newStartIntent( MatchService.ACTION_UPDATE_FOR_OVERVIEW, this );
            intent.putExtra( MatchService.EXTRA_MATCH_SCORE, score );
            intent.putExtra( MatchService.EXTRA_HOME_STATS, homeStats);
            intent.putExtra( MatchService.EXTRA_AWAY_STATS, awayStats);

            startService(intent);
        }
        finish();

        return super.onOptionsItemSelected(item);
    }

}
