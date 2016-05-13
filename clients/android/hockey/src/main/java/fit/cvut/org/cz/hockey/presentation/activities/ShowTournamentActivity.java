package fit.cvut.org.cz.hockey.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.fragments.AgregStatsTitleFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyMatchesListWrapperFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyTeamsListFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyTournamentOverviewFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.NewHockeyMatchFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.NewHockeyTournamentFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.StandingsStatsTitleFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.MatchesListWrapperFragment;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewMatchFragment;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.TournamentOverviewFragment;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class ShowTournamentActivity extends AbstractTabActivity {

    public static final String TOUR_ID = "tournament_id";

    private long tournamentID;

    private Fragment[] fragments;
    private String[] titles;

    private DefaultViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        tournamentID = getIntent().getExtras().getLong(TOUR_ID);

        titles = new String[]{ getString(R.string.header_overview), getString(R.string.header_players), getString(R.string.header_teams), getString(R.string.header_matches), getString(R.string.header_standings) };
        Fragment f1 = TournamentOverviewFragment.newInstance( tournamentID, HockeyTournamentOverviewFragment.class );
        Fragment f2 = AgregStatsTitleFragment.newInstance( tournamentID, false );
        Fragment f3 = HockeyTeamsListFragment.newInstance( tournamentID );
        Fragment f4 = MatchesListWrapperFragment.newInstance( tournamentID, HockeyMatchesListWrapperFragment.class );
        Fragment f5 = StandingsStatsTitleFragment.newInstance( tournamentID );
        fragments = new Fragment[]{ f1, f2, f3, f4, f5};

        super.onCreate(savedInstanceState);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fr = getSupportFragmentManager().findFragmentByTag(adapter.getTag(position));
                if (fr != null) {
                    if (fr instanceof AbstractDataFragment)
                        ((AbstractDataFragment) fr).customOnResume();
                    if (fr instanceof MatchesListWrapperFragment)
                        ((MatchesListWrapperFragment) fr).refresh();
                    if (fr instanceof StandingsStatsTitleFragment)
                        ((StandingsStatsTitleFragment) fr).refresh();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {

        PagerAdapter res = new DefaultViewPagerAdapter(manager, fragments, titles);

        adapter = (DefaultViewPagerAdapter)res;
        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_tournament_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case fit.cvut.org.cz.tmlibrary.R.id.action_edit:{
                if (tournamentID == -1) break;
                Intent intent = CreateTournamentActivity.newStartIntent( this, tournamentID, false );
                startActivity(intent);
                break;
            }
            case fit.cvut.org.cz.tmlibrary.R.id.action_point_config:{
                Intent intent = TournamentConfigurationActivity.newStartIntent(this, tournamentID);
                startActivity( intent );
                break;
            }
            default:
                break;
        }

        return true;
    }

    /**
     * Checks if there is enough teams to create a match
     * @return true of there is enough teams
     */
    public boolean isEnoughTeams(){
        int count = ((HockeyTeamsListFragment) (getSupportFragmentManager().findFragmentByTag(adapter.getTag(2)))).teamCount();
        return count > 1;
    }
}
