package fit.cvut.org.cz.hockey.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyCompetitionOverviewFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyTournamentsListFragment;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.CompetitionOverviewFragment;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class ShowCompetitionActivity extends AbstractTabActivity {
    //TODO Zjistit jak to je s manifestem u týhle aktivity
    //Ani nevim, proč tu tyhle mám, zatim jsou stejně jen na jednom místě... ale může se to hodit
    private static String HEADER_OVERVIEW_COMPETITION = "Overview";
    private static String HEADER_TOURNAMENTS_LIST = "Tournaments";
    private static String HEADER_COMPETITION_STANDINGS = "Players";

    private long competitionID;

    private Fragment[] fragments;
    private String[] titles;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        competitionID = 0;

        titles = new String[]{ HEADER_OVERVIEW_COMPETITION, HEADER_TOURNAMENTS_LIST/*, HEADER_COMPETITION_STANDINGS*/ };
        Fragment f1 = CompetitionOverviewFragment.newInstance( competitionID, HockeyCompetitionOverviewFragment.class );
        Fragment f2 = HockeyTournamentsListFragment.newInstance( competitionID );
//        Fragment f3 = new HockeyCompetitionOverviewFragment();
        fragments = new Fragment[]{ f1, f2 };

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
}
