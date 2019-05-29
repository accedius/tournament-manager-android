package fit.cvut.org.cz.bowling.presentation.activities;

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

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.fragments.BowlingMatchOverviewFragment;
import fit.cvut.org.cz.bowling.presentation.fragments.BowlingMatchStatsFragment;
import fit.cvut.org.cz.bowling.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;

/**
 * Activity to handle showing bowling match inner fragments (Overview and Players)
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
        Fragment f1 = BowlingMatchOverviewFragment.newInstance(matchId);
        Fragment f2 = BowlingMatchStatsFragment.newInstance(matchId);
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

    /**
     * Method to set a menu UI
     * @param menu menu to inflate a UI to
     * @return true if menu is inflated, false otherwise
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_match, menu);
        return true;
    }

    private void sendToSaveMatch() {
        Match score = ((BowlingMatchOverviewFragment) (getSupportFragmentManager().findFragmentByTag(adapter.getTag(0)))).getScore();
        if (score.isShootouts() && (score.getHomeScore() == score.getAwayScore())) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.shootouts_error), Snackbar.LENGTH_LONG).show();
            return;
        }
        List<PlayerStat> homeStats = ((BowlingMatchStatsFragment) (getSupportFragmentManager().findFragmentByTag(adapter.getTag(1)))).getHomeList();
        List<PlayerStat> awayStats = ((BowlingMatchStatsFragment) (getSupportFragmentManager().findFragmentByTag(adapter.getTag(1)))).getAwayList();

        Intent intent = MatchService.newStartIntent(MatchService.ACTION_UPDATE_FOR_OVERVIEW, this);
        intent.putExtra(ExtraConstants.EXTRA_MATCH_SCORE, score);
        intent.putExtra(ExtraConstants.EXTRA_HOME_STATS, new ArrayList<>(homeStats));
        intent.putExtra(ExtraConstants.EXTRA_AWAY_STATS, new ArrayList<>(awayStats));

        startService(intent);

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_finish  || item.getItemId() == android.R.id.home) {
            Match score = ((BowlingMatchOverviewFragment) (getSupportFragmentManager().findFragmentByTag(adapter.getTag(0)))).getScore();
            if (score.isShootouts() && (score.getHomeScore() == score.getAwayScore())) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.shootouts_error), Snackbar.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
            }
            List<PlayerStat> homeStats = ((BowlingMatchStatsFragment) (getSupportFragmentManager().findFragmentByTag(adapter.getTag(1)))).getHomeList();
            List<PlayerStat> awayStats = ((BowlingMatchStatsFragment) (getSupportFragmentManager().findFragmentByTag(adapter.getTag(1)))).getAwayList();

            Intent intent = MatchService.newStartIntent(MatchService.ACTION_UPDATE_FOR_OVERVIEW, this);
            intent.putExtra(ExtraConstants.EXTRA_MATCH_SCORE, score);
            intent.putExtra(ExtraConstants.EXTRA_HOME_STATS, new ArrayList<>(homeStats));
            intent.putExtra(ExtraConstants.EXTRA_AWAY_STATS, new ArrayList<>(awayStats));

            startService(intent);

            finish();
        } else if (item.getItemId() == R.id.action_edit_stats) {
            ((BowlingMatchStatsFragment) (getSupportFragmentManager().findFragmentByTag(adapter.getTag(1)))).editAll();
        } else if (item.getItemId() == R.id.action_edit) {
            BowlingMatchOverviewFragment fr = (BowlingMatchOverviewFragment) fragments[0];
            Intent intent = CreateMatchActivity.newStartIntent(this, matchId, fr.getTournamentId());
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_cancel) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        sendToSaveMatch();
    }

}
