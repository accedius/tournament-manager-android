package fit.cvut.org.cz.hockey.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.data.entities.Match;
import fit.cvut.org.cz.hockey.data.entities.PlayerStat;
import fit.cvut.org.cz.hockey.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyMatchOverviewFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyMatchStatsFragment;
import fit.cvut.org.cz.hockey.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;

/**
 * Activity showing match detail
 * Created by atgot_000 on 22. 4. 2016.
 */
public class ShowMatchActivity extends AbstractTabActivity {
    private long matchId;
    private View v;

    private ViewPager pager;

    private Fragment[] fragments;
    private String[] titles;

    private DefaultViewPagerAdapter adapter;

    @Override
    protected View injectView(ViewGroup parent) {
        v = super.injectView(parent);
        return v;
    }

    /**
     * Creates a new intent to start this activity
     * @param context
     * @param matchId - id of the match to be shown
     * @return Intent to that can be used to start this activity
     */
    public static Intent newStartIntent(Context context, long matchId) {
        Intent intent = new Intent(context, ShowMatchActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_MATCH_ID, matchId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        matchId = getIntent().getExtras().getLong(ExtraConstants.EXTRA_MATCH_ID);

        titles = new String[]{
                getString(fit.cvut.org.cz.tmlibrary.R.string.overview),
                getString(fit.cvut.org.cz.tmlibrary.R.string.players) };
        Fragment f1 = HockeyMatchOverviewFragment.newInstance(matchId);
        Fragment f2 = HockeyMatchStatsFragment.newInstance(matchId);
        fragments = new Fragment[]{ f1, f2 };

        super.onCreate(savedInstanceState);

        adapter = (DefaultViewPagerAdapter)getAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(fit.cvut.org.cz.tmlibrary.R.id.viewPager);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(1);

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
        getMenuInflater().inflate(R.menu.menu_match, menu);
        return true;
    }

    /**
     * Handles clicking on menu. If the save button is clicked, this activity gets data from both its fragments and sends them to service
     * @param item menuItem
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_finish) {
            Match score = ((HockeyMatchOverviewFragment) (getSupportFragmentManager().findFragmentByTag(adapter.getTag(0)))).getScore();
            if (score.isShootouts() && (score.getHomeScore() == score.getAwayScore())) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.shootouts_error), Snackbar.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
            }
            List<PlayerStat> homeStats = ((HockeyMatchStatsFragment) (getSupportFragmentManager().findFragmentByTag(adapter.getTag(1)))).getHomeList();
            List<PlayerStat> awayStats = ((HockeyMatchStatsFragment) (getSupportFragmentManager().findFragmentByTag(adapter.getTag(1)))).getAwayList();

            Intent intent = MatchService.newStartIntent(MatchService.ACTION_UPDATE_FOR_OVERVIEW, this);
            intent.putExtra(ExtraConstants.EXTRA_MATCH_SCORE, score);
            intent.putExtra(ExtraConstants.EXTRA_HOME_STATS, new ArrayList<>(homeStats));
            intent.putExtra(ExtraConstants.EXTRA_AWAY_STATS, new ArrayList<>(awayStats));

            startService(intent);

            finish();
        } else if (item.getItemId() == R.id.action_edit_stats) {
            ((HockeyMatchStatsFragment) (getSupportFragmentManager().findFragmentByTag(adapter.getTag(1)))).editAll();
        } else if (item.getItemId() == R.id.action_edit) {
            HockeyMatchOverviewFragment fr = (HockeyMatchOverviewFragment) fragments[0];
            Intent intent = CreateMatchActivity.newStartIntent(this, matchId, fr.getTournamentId());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
