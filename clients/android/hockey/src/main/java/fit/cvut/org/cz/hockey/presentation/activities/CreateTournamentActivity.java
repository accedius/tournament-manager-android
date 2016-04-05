package fit.cvut.org.cz.hockey.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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

    @Override
    protected View injectView(ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.activity_basic_layout, parent, false );
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

        if( getSupportFragmentManager().findFragmentById(R.id.container) == null ) //TODO upravit, aby to bylo podle intentu
            getSupportFragmentManager().beginTransaction().add(R.id.container, NewHockeyTournamentFragment.newInstance( 1, true, NewHockeyTournamentFragment.class )).commit();
    }
}
