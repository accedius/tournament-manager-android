package fit.cvut.org.cz.bowling.presentation.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.presentation.BowlingPackage;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.dialogs.GenerateRostersDialog;
import fit.cvut.org.cz.bowling.presentation.dialogs.StandingsHelpDialog;
import fit.cvut.org.cz.bowling.presentation.dialogs.StatsHelpDialog;
import fit.cvut.org.cz.bowling.presentation.fragments.AggregStatsTitleFragment;
import fit.cvut.org.cz.bowling.presentation.fragments.BowlingMatchesListWrapperFragment;
import fit.cvut.org.cz.bowling.presentation.fragments.BowlingTeamsListFragment;
import fit.cvut.org.cz.bowling.presentation.fragments.BowlingTournamentOverviewFragment;
import fit.cvut.org.cz.bowling.presentation.fragments.CustomTournamentOverviewFragment;
import fit.cvut.org.cz.bowling.presentation.fragments.FFAMatchesListWrapperFragment;
import fit.cvut.org.cz.bowling.presentation.fragments.StandingsStatsTitleFragment;
import fit.cvut.org.cz.bowling.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Activity to show bowling tournament inner fragments (Overview, Standings, Matches etc.)
 */
public class ShowTournamentActivity extends AbstractTabActivity {
    private final int GEN_ROSTER_ID = 1001;
    private final int TEAMS_LIST_POSITION = 3;

    private long competitionId;
    private long tournamentId;

    private DefaultViewPagerAdapter adapter;
    private String sportContext;
    private int selectedPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sportContext = ((BowlingPackage) this.getApplication()).getSportContext();
        competitionId = getIntent().getExtras().getLong(ExtraConstants.EXTRA_COMP_ID);
        tournamentId = getIntent().getExtras().getLong(ExtraConstants.EXTRA_TOUR_ID);

        super.onCreate(savedInstanceState);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                selectedPage = position;
                Fragment fr = getSupportFragmentManager().findFragmentByTag(adapter.getTag(position));
                if (fr != null) {
                    if (fr instanceof AbstractDataFragment)
                        ((AbstractDataFragment) fr).customOnResume();
                    if (fr instanceof MatchesListWrapperFragment)
                        ((MatchesListWrapperFragment) fr).refresh();
                    if (fr instanceof StandingsStatsTitleFragment)
                        ((StandingsStatsTitleFragment) fr).refresh();
                    if (fr instanceof AggregStatsTitleFragment)
                        ((AggregStatsTitleFragment) fr).refresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        IntentFilter filter = new IntentFilter(TournamentService.ACTION_GENERATE_ROSTERS);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!intent.getBooleanExtra(ExtraConstants.EXTRA_RESULT, false)) {
                    Snackbar.make(findViewById(android.R.id.content), getString(fit.cvut.org.cz.tmlibrary.R.string.failGenerateRosters), Snackbar.LENGTH_LONG).show();
                } else {
                    if (pager.getCurrentItem() == TEAMS_LIST_POSITION) {
                        int position = pager.getCurrentItem();
                        BowlingTeamsListFragment fr = (BowlingTeamsListFragment) getSupportFragmentManager().findFragmentByTag(adapter.getTag(position));
                        fr.customOnResume();
                    } else {
                        pager.setCurrentItem(TEAMS_LIST_POSITION);
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    @Override
    protected void onResume() {
        ((BowlingPackage)getApplication()).setSportContext(sportContext);
        super.onResume();
    }

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        competitionId = getIntent().getExtras().getLong(ExtraConstants.EXTRA_COMP_ID);
        tournamentId = getIntent().getExtras().getLong(ExtraConstants.EXTRA_TOUR_ID);
        Tournament t = ManagerFactory.getInstance().getEntityManager(Tournament.class).getById(tournamentId);
        TournamentType type = TournamentTypes.getMyTournamentType(t.getTypeId());

        if(type.equals(TournamentTypes.individuals())) {
            adapter = new DefaultViewPagerAdapter(manager,
                    new Fragment[] {
                            CustomTournamentOverviewFragment.newInstance(tournamentId, BowlingTournamentOverviewFragment.class),
                            FFAMatchesListWrapperFragment.newInstance(tournamentId, BowlingMatchesListWrapperFragment.class),
                            AggregStatsTitleFragment.newInstance(tournamentId, false)
                    },
                    new String[] {
                        getString(fit.cvut.org.cz.tmlibrary.R.string.overview),
                        getString(fit.cvut.org.cz.tmlibrary.R.string.matches),
                        getString(fit.cvut.org.cz.tmlibrary.R.string.players)
                    }
                    );
        } else {
            adapter = new DefaultViewPagerAdapter(manager,
                    new Fragment[] {
                            CustomTournamentOverviewFragment.newInstance(tournamentId, BowlingTournamentOverviewFragment.class),
                            StandingsStatsTitleFragment.newInstance(tournamentId),
                            FFAMatchesListWrapperFragment.newInstance(tournamentId, BowlingMatchesListWrapperFragment.class),
                            BowlingTeamsListFragment.newInstance(tournamentId, competitionId),
                            AggregStatsTitleFragment.newInstance(tournamentId, false)
                    },
                    new String[] {
                            getString(fit.cvut.org.cz.tmlibrary.R.string.overview),
                            getString(fit.cvut.org.cz.tmlibrary.R.string.standings),
                            getString(fit.cvut.org.cz.tmlibrary.R.string.matches),
                            getString(fit.cvut.org.cz.tmlibrary.R.string.teams),
                            getString(fit.cvut.org.cz.tmlibrary.R.string.players)
                    }
            );
        }
        return adapter;
    }

    /**
     * Method to set a menu UI
     * @param menu menu to inflate a UI to
     * @return true if menu is inflated, false otherwise
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_tournament_detail, menu);
        String genRosters = getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.generate_rosters);
        menu.add(0, GEN_ROSTER_ID, menu.size(), genRosters)
                .setIcon(R.drawable.ic_people_white_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case fit.cvut.org.cz.tmlibrary.R.id.action_edit:{
                if (tournamentId == -1) break;
                Intent intent = CreateTournamentActivity.newStartIntent(this, tournamentId, competitionId);
                startActivity(intent);
                break;
            }
            case fit.cvut.org.cz.tmlibrary.R.id.action_point_config:{
                Intent intent = TournamentConfigurationActivity.newStartIntent(this, tournamentId);
                startActivity(intent);
                break;
            }
            case GEN_ROSTER_ID:{
                GenerateRostersDialog dialog = GenerateRostersDialog.newInstance(competitionId, tournamentId);
                dialog.show(getSupportFragmentManager(), "GENERATE_ROSTERS_DIALOG");
                break;
            }

            case fit.cvut.org.cz.tmlibrary.R.id.action_help:
                if (selectedPage == 1) {
                    StandingsHelpDialog standingsHelpDialog = StandingsHelpDialog.newInstance();
                    standingsHelpDialog.show(getSupportFragmentManager(), "HELP");
                } else {
                    StatsHelpDialog statsHelpDialog = StatsHelpDialog.newInstance();
                    statsHelpDialog.show(getSupportFragmentManager(), "HELP");
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * @return instance of bowlingTeamsListFragment if any is found
     */
    private BowlingTeamsListFragment getBowlingTeamsListFragment() {
        for(Fragment fragment : getSupportFragmentManager().getFragments()) {
            if(fragment instanceof BowlingTeamsListFragment) {
                return (BowlingTeamsListFragment) fragment;
            }
        }
        return null;
    }

    /**
     * Checks if there is enough teams to create a match
     * @return true of there is enough teams
     */
    public boolean isEnoughTeams() {
        BowlingTeamsListFragment teamsListFragment = getBowlingTeamsListFragment();
        return (teamsListFragment != null) && (teamsListFragment.teamCount() > 1);
    }
}

