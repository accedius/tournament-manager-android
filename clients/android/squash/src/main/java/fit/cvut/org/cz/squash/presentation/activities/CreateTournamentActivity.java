package fit.cvut.org.cz.squash.presentation.activities;

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

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.fragments.NewSquashTournametFragment;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewTournamentFragment;

/**
 * This Activity accomodates NewSquashTournamentFragment
 * Created by Vaclav on 5. 4. 2016.
 */
public class CreateTournamentActivity extends AbstractToolbarActivity {
    public static Intent newStartIntent(Context context, long compId, long tourId) {
        Intent intent = new Intent(context, CreateTournamentActivity.class);
        intent.putExtra(ExtraConstants.EXTRA_COMP_ID, compId);
        intent.putExtra(ExtraConstants.EXTRA_TOUR_ID, tourId);

        return intent;
    }

    @Override
    protected View injectView(ViewGroup parent) {
        return LayoutInflater.from(this).inflate(R.layout.activity_single_container, parent, false);
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
            getSupportFragmentManager().beginTransaction().add(R.id.container, NewSquashTournametFragment.newInstance(tourID, compID, NewSquashTournametFragment.class)).commit();
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
            intent.putExtra(ExtraConstants.EXTRA_TOURNAMENT, tournament);
            startService(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
