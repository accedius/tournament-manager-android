package fit.cvut.org.cz.hockey.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.fragments.AgregStatsTitleFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyCompetitionOverviewFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyTournamentsListFragment;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.CompetitionOverviewFragment;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class ShowCompetitionActivity extends AbstractTabActivity {
//    private static String HEADER_OVERVIEW_COMPETITION = getString(R.string.header_overview);
//    private static String HEADER_TOURNAMENTS_LIST = "Tournaments";
//    private static String HEADER_COMPETITION_STANDINGS = "Players";

    private long competitionID = -1;

    private Fragment[] fragments;
    private String[] titles;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        competitionID = getIntent().getExtras().getLong(CrossPackageComunicationConstants.EXTRA_ID);
        //competitionID = 3;

        titles = new String[]{ getString(R.string.header_overview), getString(R.string.header_tournaments_list), getString(R.string.header_players) };
        Fragment f1 = CompetitionOverviewFragment.newInstance( competitionID, HockeyCompetitionOverviewFragment.class );
        Fragment f2 = HockeyTournamentsListFragment.newInstance( competitionID );
        Fragment f3 = AgregStatsTitleFragment.newInstance( competitionID, true );
        fragments = new Fragment[]{ f1, f2, f3 };

        super.onCreate(savedInstanceState);


    }



    public long getCompetitionID()
    {
        return competitionID;
    }

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {

        PagerAdapter res = new DefaultViewPagerAdapter(manager, fragments, titles);


        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_competition_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case fit.cvut.org.cz.tmlibrary.R.id.action_edit:{
                if (competitionID == -1) break;
                Intent intent = new Intent(this, CreateCompetitionActivity.class);
                intent.putExtra(CrossPackageComunicationConstants.EXTRA_ID, competitionID);
                startActivity(intent);
                break;
            }
            default:
                break;
        }

        return true;
    }
}
