package fit.cvut.org.cz.squash.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.fragments.CreateSquashMatchFragment;
import fit.cvut.org.cz.squash.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
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
        intent.putExtra(EXTRA_MATCH_ID, id);
        return intent;
    }

    @Override
    protected View injectView(ViewGroup parent) {
        return LayoutInflater.from(this).inflate(R.layout.activity_single_container, parent, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long tournamentId, matchId;

        tournamentId = getIntent().getLongExtra(EXTRA_TOURNAMENT_ID, -1);
        matchId = getIntent().getLongExtra(EXTRA_MATCH_ID, -1);

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            if (matchId == -1)
                getSupportFragmentManager().beginTransaction().add(R.id.container, CreateSquashMatchFragment.newInstance(tournamentId, CreateSquashMatchFragment.class)).commit();
            else
                getSupportFragmentManager().beginTransaction().add(R.id.container, CreateSquashMatchFragment.newInstance(matchId, tournamentId, CreateSquashMatchFragment.class)).commit();
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
            ScoredMatch scoredMatch = ((CreateSquashMatchFragment)(getSupportFragmentManager().findFragmentById(R.id.container))).getMatch();
            if (scoredMatch == null) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.not_all_fields_error), Snackbar.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
            }
            if (scoredMatch.getId() == -1 && scoredMatch.getHomeParticipantId() == scoredMatch.getAwayParticipantId()) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.match_same_participants_error), Snackbar.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
            }

            Intent intent;
            if (scoredMatch.getId() == -1) {
                intent = MatchService.newStartIntent(MatchService.ACTION_CREATE_MATCH, this);
                intent.setAction(MatchService.ACTION_CREATE_MATCH);
                scoredMatch.setTournamentId(getIntent().getLongExtra(EXTRA_TOURNAMENT_ID, -1));
                scoredMatch.setPlayed(false);
            } else {
                intent = MatchService.newStartIntent(MatchService.ACTION_UPDATE_MATCH, this);
                intent.setAction(MatchService.ACTION_UPDATE_MATCH);
            }
            intent.putExtra(MatchService.EXTRA_MATCH, scoredMatch);

            startService(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
