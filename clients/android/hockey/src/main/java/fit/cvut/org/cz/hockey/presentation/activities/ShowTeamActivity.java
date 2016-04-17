package fit.cvut.org.cz.hockey.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.fragments.ShowTeamFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.TeamDetailFragment;

/**
 * Created by atgot_000 on 17. 4. 2016.
 */
public class ShowTeamActivity extends AbstractToolbarActivity {

    public static final String EXTRA_TEAM_ID = "team_id";

    public static Intent newStartIntent( Context context, long id  )
    {
        Intent res = new Intent(context, ShowTeamActivity.class);

        res.putExtra(EXTRA_TEAM_ID, id);

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

        long teamID;
        teamID = getIntent().getLongExtra( EXTRA_TEAM_ID, -1 );

        if( getSupportFragmentManager().findFragmentById(R.id.container) == null ) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, TeamDetailFragment.newInstance(teamID, ShowTeamFragment.class)).commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
