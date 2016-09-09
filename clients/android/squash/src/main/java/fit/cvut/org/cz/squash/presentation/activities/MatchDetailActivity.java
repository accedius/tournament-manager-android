package fit.cvut.org.cz.squash.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.presentation.fragments.MatchPlayersFragment;
import fit.cvut.org.cz.squash.presentation.fragments.SquashMatchOverviewFragment;
import fit.cvut.org.cz.squash.presentation.services.MatchService;
import fit.cvut.org.cz.squash.presentation.services.PlayerService;
import fit.cvut.org.cz.tmlibrary.business.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;

/**
 * This activity accomodates all fragments to display detail of match.
 * So either SetsListFragment alone or together with MatchPlayersFragment
 * Created by Vaclav on 24. 4. 2016.
 */
public class MatchDetailActivity extends AbstractTabActivity {

    public static final String ARG_PLAYED = "arg_played";
    public static final String ARG_ID = "arg_id";
    public static final String ARG_TYPE = "arg_type";

    public static Intent newStartIntent(Context context, long id, boolean played, CompetitionType type){
        Intent i = new Intent(context, MatchDetailActivity.class);
        i.putExtra(ARG_ID, id);
        i.putExtra(ARG_PLAYED, played);
        i.putExtra(ARG_TYPE, type.id);

        return i;
    }

    private DefaultViewPagerAdapter adapter = null;

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {

        long id = getIntent().getLongExtra(ARG_ID, -1);
        boolean played = getIntent().getBooleanExtra(ARG_PLAYED, true);
        CompetitionType type = CompetitionTypes.competitionTypes()[getIntent().getIntExtra(ARG_TYPE, 0)];
        if (type.equals(CompetitionTypes.individuals()))
            adapter = new DefaultViewPagerAdapter(manager, new Fragment[]{SquashMatchOverviewFragment.newInstance(id, played)}, new String[]{getString(R.string.sets)});
        else{
            adapter = new DefaultViewPagerAdapter(manager,
                    new Fragment[] {SquashMatchOverviewFragment.newInstance(id, played),
                                    MatchPlayersFragment.newInstance(id)
                    },
                    new String[] {getString(R.string.sets),
                            getString(R.string.rosters)
                    } );
            pager.setOffscreenPageLimit(1);
        }
        return adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_match, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == fit.cvut.org.cz.tmlibrary.R.id.action_finish) {
            SquashMatchOverviewFragment fr  = (SquashMatchOverviewFragment) getSupportFragmentManager().findFragmentByTag(adapter.getTag(0));
            if (fr !=null){
                ArrayList<SetRowItem> list = fr.getSetsFragment().getSets();
                if (fr.getSetsFragment().hasErrors()) {
                    Snackbar.make(findViewById(fit.cvut.org.cz.tmlibrary.R.id.tabs), R.string.sets_error, Snackbar.LENGTH_SHORT).show();
                    return true;
                }
                for (SetRowItem set : list) {
                    if (set.getWinner() == 0){
                        Snackbar.make(findViewById(fit.cvut.org.cz.tmlibrary.R.id.tabs), R.string.sets_error, Snackbar.LENGTH_SHORT).show();
                        return true;
                    }
                }
            }

            if (fr != null && !fr.isWorking()){
                Intent intent = MatchService.newStartIntent(MatchService.ACTION_UPDATE_MATCH_DETAIL, this);
                ArrayList<SetRowItem> list = fr.getSetsFragment().getSets();
                intent.putExtra(MatchService.EXTRA_MATCHES, list);
                intent.putExtra(MatchService.EXTRA_ID, getIntent().getLongExtra(ARG_ID, -1));
                startService(intent);
            }

            if (adapter.getCount() > 1){
                MatchPlayersFragment mfr = (MatchPlayersFragment) getSupportFragmentManager().findFragmentByTag(adapter.getTag(1));
                if (mfr != null){
                    ArrayList<Player> players = mfr.getAwayPlayers();
                    Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_UDATE_PLAYERS_FOR_MATCH, this);
                    intent.putExtra(PlayerService.EXTRA_ID, getIntent().getLongExtra(ARG_ID, -1));
                    intent.putExtra(PlayerService.EXTRA_ROLE, "away");
                    intent.putParcelableArrayListExtra(PlayerService.EXTRA_PLAYERS, players);
                    startService(intent);

                    players = mfr.getHomePlayers();
                    intent = PlayerService.newStartIntent(PlayerService.ACTION_UDATE_PLAYERS_FOR_MATCH, this);
                    intent.putExtra(PlayerService.EXTRA_ID, getIntent().getLongExtra(ARG_ID, -1));
                    intent.putExtra(PlayerService.EXTRA_ROLE, "home");
                    intent.putParcelableArrayListExtra(PlayerService.EXTRA_PLAYERS, players);
                    startService(intent);
                }
            } else {
                Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_UDATE_PLAYERS_FOR_MATCH, this);
                intent.putExtra(PlayerService.EXTRA_ID, getIntent().getLongExtra(ARG_ID, -1));
                startService(intent);
            }
            finish();
            return true;
        } else if (item.getItemId() == fit.cvut.org.cz.tmlibrary.R.id.action_edit) {
            SquashMatchOverviewFragment fr = (SquashMatchOverviewFragment) adapter.getItem(0);
            Intent intent = CreateMatchActivity.newStartIntent(this, getIntent().getLongExtra(ARG_ID, -1), fr.getTournamentId());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

