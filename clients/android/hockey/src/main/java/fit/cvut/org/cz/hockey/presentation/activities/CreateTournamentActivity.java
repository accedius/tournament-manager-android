package fit.cvut.org.cz.hockey.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.fragments.NewHockeyTournamentFragment;
import fit.cvut.org.cz.hockey.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewTournamentFragment;

/**
 * Activity for creating and modifying tournament
 * Created by atgot_000 on 5. 4. 2016.
 */
public class CreateTournamentActivity extends AbstractToolbarActivity {

    public static final String EXTRA_COMP_ID = "comp_id";
    public static final String EXTRA_TOUR_ID = "tour_id";

    /**
     * Creates a new intent to start this activity
     * @param context
     * @param tourId id of tournament
     * @param compId id oc competition
     * @return Intent to that can be used to start this activity
     */
    public static Intent newStartIntent( Context context, long tourId, long compId  ) {
        Intent res = new Intent(context, CreateTournamentActivity.class);
        res.putExtra(EXTRA_TOUR_ID, tourId);
        res.putExtra(EXTRA_COMP_ID, compId);
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
        tourID = getIntent().getLongExtra( EXTRA_TOUR_ID, -1 );
        compID = getIntent().getLongExtra( EXTRA_COMP_ID, -1 );

        if( getSupportFragmentManager().findFragmentById(R.id.container) == null ) {
                getSupportFragmentManager().beginTransaction().add(R.id.container, NewHockeyTournamentFragment.newInstance(tourID, compID, NewHockeyTournamentFragment.class)).commit();
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
            Tournament tournament = ((NewTournamentFragment)(getSupportFragmentManager().findFragmentById(R.id.container))).getTournament();
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
            Log.d("TOURNAMENT_ACTIVITY", "Tournament_id="+tournament.getId());
            Log.d("TOURNAMENT_ACTIVITY", "Competition_id="+tournament.getCompetitionId());
            intent.putExtra(TournamentService.EXTRA_TOURNAMENT, tournament);
            startService(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
