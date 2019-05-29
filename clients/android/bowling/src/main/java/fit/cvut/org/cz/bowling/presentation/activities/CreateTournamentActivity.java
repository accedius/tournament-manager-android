package fit.cvut.org.cz.bowling.presentation.activities;

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

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.fragments.NewBowlingTournamentFragment;
import fit.cvut.org.cz.bowling.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewTournamentFragment;

/**
 * Activity to create a bowling tournament
 */
public class CreateTournamentActivity extends AbstractToolbarActivity {
    /**
     * Creates a new intent to start this activity
     * @param context
     * @param tourId id of tournament
     * @param compId id oc competition
     * @return Intent to that can be used to start this activity
     */
    public static Intent newStartIntent(Context context, long tourId, long compId) {
        Intent res = new Intent(context, CreateTournamentActivity.class);
        res.putExtra(ExtraConstants.EXTRA_TOUR_ID, tourId);
        res.putExtra(ExtraConstants.EXTRA_COMP_ID, compId);
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

        long tourID, compID;
        tourID = getIntent().getLongExtra(ExtraConstants.EXTRA_TOUR_ID, -1);
        compID = getIntent().getLongExtra(ExtraConstants.EXTRA_COMP_ID, -1);

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            NewTournamentFragment bowlingTournamentFragment = NewBowlingTournamentFragment.newInstance(tourID, compID, NewBowlingTournamentFragment.class);
            getSupportFragmentManager().beginTransaction().add(R.id.container, bowlingTournamentFragment).commit();
        }
    }

    /**
     * Method to set a menu UI
     * @param menu menu to inflate a UI to
     * @return true if menu is inflated, false otherwise
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == fit.cvut.org.cz.tmlibrary.R.id.action_finish) {
            NewTournamentFragment tournamentFragment = (NewTournamentFragment) getSupportFragmentManager().findFragmentById(R.id.container);
            Tournament tournament = tournamentFragment.getTournament();
            if (tournament.getName().isEmpty()) {
                Snackbar.make(findViewById(android.R.id.content), getString(fit.cvut.org.cz.tmlibrary.R.string.name_empty_error), Snackbar.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
            }

            Intent intent;
            if (tournament.getId() == -1) {
                intent = TournamentService.newStartIntent(TournamentService.ACTION_CREATE, this);
            } else {
                intent = TournamentService.newStartIntent(TournamentService.ACTION_UPDATE, this);
            }
            intent.putExtra(ExtraConstants.EXTRA_TOURNAMENT, tournament);
            startService(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
