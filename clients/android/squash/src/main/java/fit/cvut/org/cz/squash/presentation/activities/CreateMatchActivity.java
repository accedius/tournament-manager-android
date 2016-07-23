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
import fit.cvut.org.cz.squash.presentation.fragments.CreateSquashMatchFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;

/** this Activity accomodates CreateSquashMatchFragment
 * Created by Vaclav on 5. 4. 2016.
 */
public class CreateMatchActivity extends AbstractToolbarActivity {

    public static final String EXTRA_MATCH_ID = "match_id";
    public static final String EXTRA_TOURNAMENT_ID = "tournament_id";

    public static Intent newStartIntent(Context context, long id, long tournamentId){
        Intent intent = new Intent(context, CreateMatchActivity.class);
        intent.putExtra(EXTRA_TOURNAMENT_ID, tournamentId);
        intent.putExtra(EXTRA_MATCH_ID,id);

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

        long tournamentid, matchId;

        tournamentid = getIntent().getLongExtra(EXTRA_TOURNAMENT_ID, -1);
        matchId = getIntent().getLongExtra(EXTRA_MATCH_ID, -1);

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {

            if (matchId == -1)
                getSupportFragmentManager().beginTransaction().add(R.id.container, CreateSquashMatchFragment.newInstance(tournamentid, CreateSquashMatchFragment.class)).commit();
            else
                getSupportFragmentManager().beginTransaction().add(R.id.container, CreateSquashMatchFragment.newInstance(matchId, tournamentid, CreateSquashMatchFragment.class)).commit();
        }
    }
}
