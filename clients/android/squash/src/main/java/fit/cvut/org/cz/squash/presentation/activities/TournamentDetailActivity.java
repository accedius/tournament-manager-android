package fit.cvut.org.cz.squash.presentation.activities;

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

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.SquashPackage;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.dialogs.GenerateRostersDialog;
import fit.cvut.org.cz.squash.presentation.dialogs.StatsHelpDialog;
import fit.cvut.org.cz.squash.presentation.fragments.SquashMatchesListWrapperFragment;
import fit.cvut.org.cz.squash.presentation.fragments.SquashTournamentOverviewFragment;
import fit.cvut.org.cz.squash.presentation.fragments.StandingsWrapperFragment;
import fit.cvut.org.cz.squash.presentation.fragments.StatsListWrapperFragment;
import fit.cvut.org.cz.squash.presentation.fragments.TeamsListFragment;
import fit.cvut.org.cz.squash.presentation.services.StatsService;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.MatchesListWrapperFragment;

/**
 * This acitivity includes all necessary fragments to display Detail of tournament
 * Created by Vaclav on 10. 4. 2016.
 */
public class TournamentDetailActivity extends AbstractTabActivity {
    private final int GEN_ROSTER_ID = 1001;
    private final int TEAMS_LIST_POSITION = 4;

    private long competitionID;
    private long tournamentID;
    private DefaultViewPagerAdapter adapter = null;
    private String sportContext;

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        competitionID = getIntent().getExtras().getLong(ExtraConstants.EXTRA_COMP_ID);
        tournamentID = getIntent().getExtras().getLong(ExtraConstants.EXTRA_TOUR_ID);
        CompetitionType type = CompetitionTypes.competitionTypes(getResources())[getIntent().getIntExtra(ExtraConstants.EXTRA_TYPE, 0)];

        if (type.equals(CompetitionTypes.teams())) {
            adapter =  new DefaultViewPagerAdapter(manager,
                    new Fragment[]{
                            SquashTournamentOverviewFragment.newInstance(tournamentID, type),
                            StandingsWrapperFragment.newInstance(tournamentID, CompetitionTypes.teams()),
                            StatsListWrapperFragment.newInstance(tournamentID, StatsService.ACTION_GET_STATS_BY_TOURNAMENT),
                            SquashMatchesListWrapperFragment.newInstance(tournamentID, SquashMatchesListWrapperFragment.class),
                            TeamsListFragment.newInstance(tournamentID)},
                    new String[]{getResources().getString(R.string.overview),
                            getResources().getString(R.string.standings),
                            getResources().getString(R.string.players),
                            getResources().getString(R.string.matches),
                            getResources().getString(R.string.teams)});
        } else {
            adapter =  new DefaultViewPagerAdapter(manager,
                    new Fragment[]{
                            SquashTournamentOverviewFragment.newInstance(tournamentID, type),
                            StandingsWrapperFragment.newInstance(tournamentID, CompetitionTypes.individuals()),
                            StatsListWrapperFragment.newInstance(tournamentID, StatsService.ACTION_GET_STATS_BY_TOURNAMENT),
                            SquashMatchesListWrapperFragment.newInstance(tournamentID, SquashMatchesListWrapperFragment.class)},
                    new String[]{getResources().getString(R.string.overview),
                            getResources().getString(R.string.standings),
                            getResources().getString(R.string.players),
                            getResources().getString(R.string.matches)});
        }
        return adapter;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sportContext = ((SquashPackage) this.getApplication()).getSportContext();
        super.onCreate(savedInstanceState);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                Fragment fr = getSupportFragmentManager().findFragmentByTag(adapter.getTag(position));
                if (fr != null) {
                    if (fr instanceof AbstractDataFragment) ((AbstractDataFragment) fr).customOnResume();
                    if (fr instanceof MatchesListWrapperFragment) ((MatchesListWrapperFragment) fr).refresh();
                    if (fr instanceof StandingsWrapperFragment) ((StandingsWrapperFragment) fr).refresh();
                    if (fr instanceof StatsListWrapperFragment) ((StatsListWrapperFragment) fr).refresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        IntentFilter filter = new IntentFilter(TournamentService.ACTION_GENERATE_ROSTERS);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getBooleanExtra(ExtraConstants.EXTRA_RESULT, false) == false) {
                    Snackbar.make(findViewById(android.R.id.content), getString(fit.cvut.org.cz.tmlibrary.R.string.failGenerateRosters), Snackbar.LENGTH_LONG).show();
                } else {
                    if (pager.getCurrentItem() == TEAMS_LIST_POSITION) {
                        TeamsListFragment fr = (TeamsListFragment) adapter.getItem(pager.getCurrentItem());
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
        ((SquashPackage)getApplication()).setSportContext(sportContext);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_tournament_detail, menu);
        CompetitionType type = CompetitionTypes.competitionTypes(getResources())[getIntent().getIntExtra(ExtraConstants.EXTRA_TYPE, 0)];
        if (type.equals(CompetitionTypes.teams())) {
            String genRosters = getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.generate_rosters);
            menu.add(0, GEN_ROSTER_ID, menu.size(), genRosters)
                    .setIcon(R.drawable.ic_people_white_24dp)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case fit.cvut.org.cz.tmlibrary.R.id.action_edit:{
                if (tournamentID == -1) break;
                Intent intent = CreateTournamentActivity.newStartIntent(this, competitionID, tournamentID);
                startActivity(intent);
                break;
            }
            case fit.cvut.org.cz.tmlibrary.R.id.action_point_config:{
                Intent intent = new Intent(this, PointConfigActivity.class);
                intent.putExtra(ExtraConstants.EXTRA_ID, tournamentID);
                startActivity(intent);
                return true;
            }
            case GEN_ROSTER_ID:{
                CompetitionType type = CompetitionTypes.competitionTypes(getResources())[getIntent().getIntExtra(ExtraConstants.EXTRA_TYPE, 0)];
                if (type.equals(CompetitionTypes.individuals()))
                    break;

                GenerateRostersDialog dialog = GenerateRostersDialog.newInstance(competitionID, tournamentID);
                dialog.show(getSupportFragmentManager(), "GENERATE_ROSTERS_DIALOG");
                return true;
            }
            case fit.cvut.org.cz.tmlibrary.R.id.action_help:
                StatsHelpDialog statsHelpDialog = StatsHelpDialog.newInstance();
                statsHelpDialog.show(getSupportFragmentManager(), "HELP");
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
