package fit.cvut.org.cz.hockey.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;

import fit.cvut.org.cz.hockey.presentation.fragments.HockeyTournamentOverviewFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.NewHockeyMatchFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.NewHockeyTournamentFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewMatchFragment;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.TournamentOverviewFragment;

/**
 * Created by atgot_000 on 8. 4. 2016.
 */
public class ShowTournamentActivity extends AbstractTabActivity {

    private static String HEADER_OVERVIEW_TOURNAMENT = "Overview";
    public static final String TOUR_ID = "tournament_id";

    private long tournamentID;

    private Fragment[] fragments;
    private String[] titles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        tournamentID = getIntent().getExtras().getLong(TOUR_ID);
        //tournamentID = 1;

        titles = new String[]{ HEADER_OVERVIEW_TOURNAMENT };
        Fragment f1 = TournamentOverviewFragment.newInstance( tournamentID, HockeyTournamentOverviewFragment.class );
        //Fragment f2 = NewMatchFragment.newInstance( tournamentID, true, NewHockeyMatchFragment.class );
        fragments = new Fragment[]{ f1 };

        super.onCreate(savedInstanceState);


    }

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {

        PagerAdapter res = new DefaultViewPagerAdapter(manager, fragments, titles);


        return res;
    }
}
