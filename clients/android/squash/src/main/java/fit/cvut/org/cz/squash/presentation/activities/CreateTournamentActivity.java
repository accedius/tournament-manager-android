package fit.cvut.org.cz.squash.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.fragments.NewSquashTournametFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;

/**
 * This Activity accomodates NewSquashTournamentFragment
 * Created by Vaclav on 5. 4. 2016.
 */
public class CreateTournamentActivity extends AbstractToolbarActivity {

    public static final String EXTRA_COMPETITON_ID = "competition_id";
    public static final String EXTRA_TOURNAMENT_ID = "tournament_id";

    public static Intent newStartIntent(Context context, long id, boolean forCompetition){
        Intent intent = new Intent(context, CreateTournamentActivity.class);
        if (forCompetition) intent.putExtra(EXTRA_COMPETITON_ID, id);
        else intent.putExtra(EXTRA_TOURNAMENT_ID,id);

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

        long tournamentid, competitionId;

        tournamentid = getIntent().getLongExtra(EXTRA_TOURNAMENT_ID, -1);
        competitionId = getIntent().getLongExtra(EXTRA_COMPETITON_ID, -1);

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {

            if (tournamentid != -1){
                getSupportFragmentManager().beginTransaction().add(R.id.container, NewSquashTournametFragment.newInstance(tournamentid, false, NewSquashTournametFragment.class)).commit();
            } else if (competitionId != -1){
                getSupportFragmentManager().beginTransaction().add(R.id.container, NewSquashTournametFragment.newInstance(competitionId, true, NewSquashTournametFragment.class)).commit();
            }


        }
    }
}
