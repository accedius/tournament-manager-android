package fit.cvut.org.cz.bowling.presentation.activities;

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
import java.util.List;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.fragments.MatchEditAtOnceFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;

public class EditAtOnceActivity extends AbstractToolbarActivity {
    /**
     * Creates a new intent to start this activity
     * @param context
     * @param homeStats stats of the home team
     * @param awayStats stats of the away team
     * @return Intent to that can be used to start this activity
     */
    public static Intent newStartIntent(Context context, List<PlayerStat> homeStats, List<PlayerStat> awayStats) {
        Intent res = new Intent(context, EditAtOnceActivity.class);

        ArrayList<PlayerStat> newHomeStat = new ArrayList<>(homeStats);
        ArrayList<PlayerStat> newAwayStat = new ArrayList<>(awayStats);

        res.putParcelableArrayListExtra(ExtraConstants.EXTRA_HOME_STATS, newHomeStat);
        res.putParcelableArrayListExtra(ExtraConstants.EXTRA_AWAY_STATS, newAwayStat);

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

        ArrayList<PlayerStat> homeStats = getIntent().getParcelableArrayListExtra(ExtraConstants.EXTRA_HOME_STATS);
        ArrayList<PlayerStat> awayStats = getIntent().getParcelableArrayListExtra(ExtraConstants.EXTRA_AWAY_STATS);

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

            ArrayList<PlayerStat> homeStats = fragment.getHomeData();
            ArrayList<PlayerStat> awayStats = fragment.getAwayData();

            Intent data = new Intent();
            data.putParcelableArrayListExtra(ExtraConstants.EXTRA_HOME_STATS, homeStats);
            data.putParcelableArrayListExtra(ExtraConstants.EXTRA_AWAY_STATS, awayStats);

            setResult(Activity.RESULT_OK, data);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
