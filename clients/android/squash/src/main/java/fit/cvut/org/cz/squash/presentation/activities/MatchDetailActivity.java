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
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.fragments.MatchPlayersFragment;
import fit.cvut.org.cz.squash.presentation.fragments.SquashMatchOverviewFragment;
import fit.cvut.org.cz.squash.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.PlayerStat;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;

/**
 * This activity accommodates all fragments to display detail of match.
 * So either SetsListFragment alone or together with MatchPlayersFragment
 * Created by Vaclav on 24. 4. 2016.
 */
public class MatchDetailActivity extends AbstractTabActivity {
    public static Intent newStartIntent(Context context, long id, boolean played, CompetitionType type){
        Intent i = new Intent(context, MatchDetailActivity.class);
        i.putExtra(ExtraConstants.EXTRA_ID, id);
        i.putExtra(ExtraConstants.EXTRA_PLAYED, played);
        i.putExtra(ExtraConstants.EXTRA_TYPE, type.id);

        return i;
    }

    private DefaultViewPagerAdapter adapter = null;

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        long id = getIntent().getLongExtra(ExtraConstants.EXTRA_ID, -1);
        boolean played = getIntent().getBooleanExtra(ExtraConstants.EXTRA_PLAYED, true);
        CompetitionType type = CompetitionTypes.competitionTypes()[getIntent().getIntExtra(ExtraConstants.EXTRA_TYPE, 0)];
        if (type.equals(CompetitionTypes.individuals()))
            adapter = new DefaultViewPagerAdapter(manager,
                    new Fragment[]{SquashMatchOverviewFragment.newInstance(id, played)},
                    new String[]{getString(R.string.sets)}
            );
        else {
            adapter = new DefaultViewPagerAdapter(manager,
                    new Fragment[] {
                            SquashMatchOverviewFragment.newInstance(id, played),
                            MatchPlayersFragment.newInstance(id)},
                    new String[] {getString(R.string.sets), getString(R.string.rosters)}
            );
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
            if (fr != null) {
                if (fr.getSetsFragment().hasErrors()) {
                    Snackbar.make(findViewById(fit.cvut.org.cz.tmlibrary.R.id.tabs), R.string.sets_invalid_values, Snackbar.LENGTH_SHORT).show();
                    return true;
                }
                for (SetRowItem set : fr.getSetsFragment().getSets()) {
                    if (set.getHomeScore() == set.getAwayScore()) {
                        Snackbar.make(findViewById(fit.cvut.org.cz.tmlibrary.R.id.tabs), R.string.tie_set, Snackbar.LENGTH_SHORT).show();
                        return true;
                    }
                }
            }

            if (fr != null && !fr.isWorking()) {
                Intent intent = MatchService.newStartIntent(MatchService.ACTION_UPDATE_MATCH_DETAIL, this);
                intent.putExtra(ExtraConstants.EXTRA_ID, getIntent().getLongExtra(ExtraConstants.EXTRA_ID, -1));
                intent.putExtra(ExtraConstants.EXTRA_MATCHES, new ArrayList<>(fr.getSetsFragment().getSets()));

                ArrayList<PlayerStat> homePlayers = new ArrayList<>();
                ArrayList<PlayerStat> awayPlayers = new ArrayList<>();
                // Send players only for Teams competition (which's fragment has 2 tabs)
                if (adapter.getCount() > 1) {
                    MatchPlayersFragment mfr = (MatchPlayersFragment) getSupportFragmentManager().findFragmentByTag(adapter.getTag(1));
                    if (mfr != null) {
                        homePlayers.addAll(mfr.getHomePlayers());
                        awayPlayers.addAll(mfr.getAwayPlayers());
                    }
                }
                intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_HOME_STATS, homePlayers);
                intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_AWAY_STATS, awayPlayers);
                startService(intent);
            }
            finish();
            return true;
        } else if (item.getItemId() == fit.cvut.org.cz.tmlibrary.R.id.action_edit) {
            SquashMatchOverviewFragment fr = (SquashMatchOverviewFragment) adapter.getItem(0);
            Intent intent = CreateMatchActivity.newStartIntent(this, getIntent().getLongExtra(ExtraConstants.EXTRA_ID, -1), fr.getTournamentId());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

