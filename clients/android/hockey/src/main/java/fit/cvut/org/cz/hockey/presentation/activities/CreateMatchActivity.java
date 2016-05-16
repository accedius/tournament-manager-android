package fit.cvut.org.cz.hockey.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.fragments.NewHockeyMatchFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;

/**
 * Activity for creating and modifying match
 * Created by atgot_000 on 19. 4. 2016.
 */
public class CreateMatchActivity extends AbstractToolbarActivity {

    public static final String EXTRA_MATCH_ID = "match_id";
    public static final String EXTRA_TOUR_ID = "tour_id";

    /**
     * Creates a new intent to start this activity
     * @param context
     * @param tourId id of the tournament where the match should be created
     * @return Intent to that can be used to start this activity
     */
    public static Intent newStartIntent( Context context, long tourId  )
    {
        Intent res = new Intent(context, CreateMatchActivity.class);
        res.putExtra(EXTRA_TOUR_ID, tourId);

        return res;
    }

    /**
     * Creates a new intent to start this activity
     * @param context
     * @param id id of the match
     * @param tourId id of the tournament where the match should be modified
     * @return Intent to that can be used to start this activity
     */
    public static Intent newStartIntent( Context context, long id, long tourId  )
    {
        Intent res = new Intent(context, CreateMatchActivity.class);
        res.putExtra(EXTRA_TOUR_ID, tourId);
        res.putExtra(EXTRA_MATCH_ID, id);

        return res;
    }

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
        super.onCreate(savedInstanceState);

        long matchId, tourId;
        tourId = getIntent().getLongExtra( EXTRA_TOUR_ID, -1 );
        matchId = getIntent().getLongExtra( EXTRA_MATCH_ID, -1 );

        if( getSupportFragmentManager().findFragmentById(R.id.container) == null ) {
            if( matchId == -1)
                getSupportFragmentManager().beginTransaction().add(R.id.container, NewHockeyMatchFragment.newInstance(tourId, NewHockeyMatchFragment.class)).commit();
            else
                getSupportFragmentManager().beginTransaction().add(R.id.container, NewHockeyMatchFragment.newInstance(matchId, tourId, NewHockeyMatchFragment.class)).commit();
        }
    }
}
