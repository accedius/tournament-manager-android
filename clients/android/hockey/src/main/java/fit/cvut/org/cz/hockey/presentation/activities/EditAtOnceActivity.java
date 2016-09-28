package fit.cvut.org.cz.hockey.presentation.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.MatchPlayerStatistic;
import fit.cvut.org.cz.hockey.presentation.fragments.MatchEditAtOnceFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;

/**
 * Activity for editing all stats in a match at once
 * Created by atgot_000 on 1. 5. 2016.
 */
public class EditAtOnceActivity extends AbstractToolbarActivity {
    public static final String EXTRA_HOME_STATS = "home_stats";
    public static final String EXTRA_AWAY_STATS = "away_stats";

    /**
     * Creates a new intent to start this activity
     * @param context
     * @param homeStats stats of the home team
     * @param awayStats stats of the away team
     * @return Intent to that can be used to start this activity
     */
    public static Intent newStartIntent(Context context, ArrayList<MatchPlayerStatistic> homeStats, ArrayList<MatchPlayerStatistic> awayStats) {
        Intent res = new Intent(context, EditAtOnceActivity.class);

        ArrayList<MatchPlayerStatistic> newHomeStat = new ArrayList<>(homeStats);
        ArrayList<MatchPlayerStatistic> newAwayStat = new ArrayList<>(awayStats);

        res.putParcelableArrayListExtra(EXTRA_HOME_STATS, newHomeStat);
        res.putParcelableArrayListExtra(EXTRA_AWAY_STATS, newAwayStat);

        return res;
    }

    @Override
    protected View injectView(ViewGroup parent) {
        return LayoutInflater.from(this).inflate(R.layout.activity_basic_layout, parent, false);
    }

    @Override
    protected FloatingActionButton getFloatingActionButton(ViewGroup root) {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<MatchPlayerStatistic> homeStats = getIntent().getParcelableArrayListExtra(EXTRA_HOME_STATS);
        ArrayList<MatchPlayerStatistic> awayStats = getIntent().getParcelableArrayListExtra(EXTRA_AWAY_STATS);

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, MatchEditAtOnceFragment.newInstance(homeStats, awayStats)).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_finish) {
            MatchEditAtOnceFragment fragment = (MatchEditAtOnceFragment) getSupportFragmentManager().findFragmentById(R.id.container);
            if (fragment == null) {
                return super.onOptionsItemSelected(item);
            }

            ArrayList<MatchPlayerStatistic> homeStats = fragment.getHomeData();
            ArrayList<MatchPlayerStatistic> awayStats = fragment.getAwayData();

            Intent data = new Intent();
            data.putParcelableArrayListExtra(EXTRA_HOME_STATS, homeStats);
            data.putParcelableArrayListExtra(EXTRA_AWAY_STATS, awayStats);

            setResult(Activity.RESULT_OK, data);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
