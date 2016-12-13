package fit.cvut.org.cz.hockey.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.hockey.presentation.fragments.ConfigurePointsFragment;
import fit.cvut.org.cz.hockey.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;

/**
 * Activity for setting tournament configuration
 * Created by atgot_000 on 11. 4. 2016.
 */
public class TournamentConfigurationActivity extends AbstractToolbarActivity {
    public static final String EXTRA_TOUR_ID = "tour_id";

    /**
     * Creates a new intent to start this activity
     * @param context
     * @param id id of tournament
     * @return Intent to that can be used to start this activity
     */
    public static Intent newStartIntent(Context context, long id) {
        Intent res = new Intent(context, TournamentConfigurationActivity.class);

        res.putExtra(EXTRA_TOUR_ID, id);

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

        long tourID;
        tourID = getIntent().getLongExtra(EXTRA_TOUR_ID, -1);

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, ConfigurePointsFragment.newInstance(tourID)).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == fit.cvut.org.cz.tmlibrary.R.id.action_finish) {
            PointConfiguration pointConfig = ((ConfigurePointsFragment)(getSupportFragmentManager().findFragmentById(R.id.container))).getPointConfig();
            if (pointConfig == null) {
                Snackbar.make(findViewById(android.R.id.content), getString(fit.cvut.org.cz.tmlibrary.R.string.not_all_fields_error), Snackbar.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
            }

            Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_SET_CONFIG, this);
            intent.putExtra(TournamentService.EXTRA_CONFIGURATION, pointConfig);
            startService(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
