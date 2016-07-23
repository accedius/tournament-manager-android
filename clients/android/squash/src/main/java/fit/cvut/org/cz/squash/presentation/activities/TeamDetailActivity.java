package fit.cvut.org.cz.squash.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.fragments.SquashTeamDetailFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.TeamDetailFragment;

/**
 * This activity accomodates TeamDetailFragment
 * Created by Vaclav on 15. 4. 2016.
 */
public class TeamDetailActivity extends AbstractToolbarActivity {

    private static final String ARG_ID = "arg_id";

    public static Intent newStartIntent(long teamId, Context context){
        Intent intent = new Intent(context, TeamDetailActivity.class);
        intent.putExtra(ARG_ID, teamId);
        return intent;
    }


    @Override
    protected View injectView(ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.activity_single_container, parent, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long id = getIntent().getLongExtra(ARG_ID, -1);

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null){
            getSupportFragmentManager().beginTransaction().add(R.id.container, TeamDetailFragment.newInstance(id, SquashTeamDetailFragment.class)).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
