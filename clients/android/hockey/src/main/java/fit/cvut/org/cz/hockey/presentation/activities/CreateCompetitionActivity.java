package fit.cvut.org.cz.hockey.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.HockeyPackage;
import fit.cvut.org.cz.hockey.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.hockey.presentation.fragments.NewHockeyCompetitionFragment;
import fit.cvut.org.cz.hockey.presentation.services.CompetitionService;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewCompetitionFragment;

public class CreateCompetitionActivity extends AbstractToolbarActivity {
    @Override
    protected View injectView(ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.activity_basic_layout, parent, false);
    }

    @Override
    protected FloatingActionButton getFloatingActionButton(ViewGroup root) {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String sport_context = getIntent().getExtras().getString(CrossPackageConstants.EXTRA_SPORT_CONTEXT);
        ((HockeyPackage) this.getApplication()).setSportContext(sport_context);

        super.onCreate(savedInstanceState);

        long id = getIntent().getLongExtra(CrossPackageConstants.EXTRA_ID, -1);

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null)
            getSupportFragmentManager().beginTransaction().add(R.id.container, NewHockeyCompetitionFragment.newInstance(id, NewHockeyCompetitionFragment.class)).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == fit.cvut.org.cz.tmlibrary.R.id.action_finish) {
            Competition competition = ((NewCompetitionFragment)(getSupportFragmentManager().findFragmentById(R.id.container))).getCompetition();
            if (competition.getName().isEmpty()) {
                Snackbar.make(findViewById(android.R.id.content), getString(fit.cvut.org.cz.tmlibrary.R.string.name_empty_error), Snackbar.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
            }

            Intent intent;
            if (competition.getId() == -1) {
                intent = CompetitionService.newStartIntent(CompetitionService.ACTION_CREATE, this);
            } else {
                intent = CompetitionService.newStartIntent(CompetitionService.ACTION_UPDATE, this);
            }
            intent.putExtra(ExtraConstants.EXTRA_COMPETITION, competition);
            startService(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
