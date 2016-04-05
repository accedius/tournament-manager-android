package fit.cvut.org.cz.hockey.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.fragments.NewHockeyTournamentFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;

/**
 * Created by atgot_000 on 5. 4. 2016.
 */
public class CreateTournamentActivity extends AbstractToolbarActivity {

    public static final String EXTRA_COMP_ID = "comp_id";
    public static final String EXTRA_TOUR_ID = "tour_id";

    public static Intent newStartIntent( Context context, long id, boolean compId  )
    {
        Intent res = new Intent(context, CreateTournamentActivity.class);
        if( compId )
            res.putExtra(EXTRA_COMP_ID, id);
        else res.putExtra(EXTRA_TOUR_ID, id);

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
            if( tourID != -1)
                getSupportFragmentManager().beginTransaction().add(R.id.container, NewHockeyTournamentFragment.newInstance(tourID, false, NewHockeyTournamentFragment.class)).commit();
            else
                getSupportFragmentManager().beginTransaction().add(R.id.container, NewHockeyTournamentFragment.newInstance(compID, true, NewHockeyTournamentFragment.class)).commit();
        }
    }
}
