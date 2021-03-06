package fit.cvut.org.cz.hockey.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.data.entities.Match;
import fit.cvut.org.cz.hockey.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.hockey.presentation.fragments.NewHockeyMatchFragment;
import fit.cvut.org.cz.hockey.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;

/**
 * Activity for creating and modifying match
 * Created by atgot_000 on 19. 4. 2016.
 */
public class CreateMatchActivity extends AbstractToolbarActivity {
    /**
     * Creates a new intent to start this activity
     * @param context
     * @param tourId id of the tournament where the match should be created
     * @return Intent to that can be used to start this activity
     */
    public static Intent newStartIntent(Context context, long tourId) {
        Intent res = new Intent(context, CreateMatchActivity.class);
        res.putExtra(ExtraConstants.EXTRA_TOUR_ID, tourId);

        return res;
    }

    /**
     * Creates a new intent to start this activity
     * @param context
     * @param id id of the match
     * @param tourId id of the tournament where the match should be modified
     * @return Intent to that can be used to start this activity
     */
    public static Intent newStartIntent(Context context, long id, long tourId) {
        Intent res = new Intent(context, CreateMatchActivity.class);
        res.putExtra(ExtraConstants.EXTRA_TOUR_ID, tourId);
        res.putExtra(ExtraConstants.EXTRA_MATCH_ID, id);

        return res;
    }

    @Override
    protected View injectView(ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.activity_basic_layout, parent, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long matchId, tourId;
        tourId = getIntent().getLongExtra(ExtraConstants.EXTRA_TOUR_ID, -1);
        matchId = getIntent().getLongExtra(ExtraConstants.EXTRA_MATCH_ID, -1);

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            if (matchId == -1)
                getSupportFragmentManager().beginTransaction().add(R.id.container, NewHockeyMatchFragment.newInstance(tourId, NewHockeyMatchFragment.class)).commit();
            else
                getSupportFragmentManager().beginTransaction().add(R.id.container, NewHockeyMatchFragment.newInstance(matchId, tourId, NewHockeyMatchFragment.class)).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == fit.cvut.org.cz.tmlibrary.R.id.action_finish) {
            fit.cvut.org.cz.tmlibrary.data.entities.Match match = ((NewHockeyMatchFragment)(getSupportFragmentManager().findFragmentById(R.id.container))).getMatch();
            if (match == null) {
                Snackbar.make(findViewById(android.R.id.content), getString(fit.cvut.org.cz.tmlibrary.R.string.period_round_empty_error), Snackbar.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
            }
            Match hockeyMatch = new Match(match);
            if (hockeyMatch.getId() == -1) {
                List<Participant> participants = hockeyMatch.getParticipants();
                if (participants.get(0).getParticipantId() == participants.get(1).getParticipantId()) {
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.match_same_participants_error), Snackbar.LENGTH_LONG).show();
                    return super.onOptionsItemSelected(item);
                }
            }

            Intent intent;
            if (hockeyMatch.getId() == -1) {
                intent = MatchService.newStartIntent(MatchService.ACTION_CREATE, this);
                intent.setAction(MatchService.ACTION_CREATE);
            } else {
                intent = MatchService.newStartIntent(MatchService.ACTION_UPDATE, this);
                intent.setAction(MatchService.ACTION_UPDATE);
            }
            intent.putExtra(ExtraConstants.EXTRA_MATCH, hockeyMatch);

            startService(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
