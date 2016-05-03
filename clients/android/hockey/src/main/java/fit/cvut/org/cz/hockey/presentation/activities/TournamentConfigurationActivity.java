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
import fit.cvut.org.cz.hockey.presentation.fragments.ConfigurePointsFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;

/**
 * Created by atgot_000 on 11. 4. 2016.
 */
public class TournamentConfigurationActivity extends AbstractToolbarActivity {

    public static final String EXTRA_TOUR_ID = "tour_id";

    /**
     * Creates a new intent to start this activity
     * @param context
     * @param id id of tournament
     * @return Intent to that can be used to start this activity
     */
    public static Intent newStartIntent( Context context, long id  )
    {
        Intent res = new Intent(context, TournamentConfigurationActivity.class);

        res.putExtra(EXTRA_TOUR_ID, id);

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

        long tourID;
        tourID = getIntent().getLongExtra( EXTRA_TOUR_ID, -1 );

        if( getSupportFragmentManager().findFragmentById(R.id.container) == null ) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, ConfigurePointsFragment.newInstance( tourID )).commit();

        }
    }
}
