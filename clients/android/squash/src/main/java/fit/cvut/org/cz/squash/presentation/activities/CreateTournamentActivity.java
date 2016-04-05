package fit.cvut.org.cz.squash.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.fragments.NewSquashCompetitionFragment;
import fit.cvut.org.cz.squash.presentation.fragments.NewSquashTournametFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public class CreateTournamentActivity extends AbstractToolbarActivity {

    public static final String EXTRA_COMPETITON_ID = "competition_id";
    public static final String EXTRA_TOURNAMENT_ID = "tournament_id";

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

        long tournamentid, competitionId;

        tournamentid = getIntent().getLongExtra(EXTRA_TOURNAMENT_ID, -1);
        competitionId = getIntent().getLongExtra(EXTRA_COMPETITON_ID, -1);

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {


            getSupportFragmentManager().beginTransaction().add(R.id.container, NewSquashTournametFragment.newInstance(1, false, NewSquashTournametFragment.class)).commit();
        }
    }
}
