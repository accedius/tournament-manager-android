package fit.cvut.org.cz.hockey.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class ShowCompetitionActivity extends AbstractTabActivity {
    //TODO Zjistit jak to je s manifestem u týhle aktivity
    //Ani nevim, proč tu tyhle mám, zatim jsou stejně jen na jednom místě... ale může se to hodit
    private static String HEADER_OVERVIEW_COMPETITION = "Overview";
    private static String HEADER_TOURNAMENTS_LIST = "Tournaments";
    private static String HEADER_COMPETITION_STANDINGS = "Players";


    private Fragment[] fragments;
    private String[] titles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //V intentu mi musi prijit id competitionu. Tady podle toho vytvorim fragmenty - ty by mely mit id competitionu v konstruktoru
        //V tech fragmentech nactu ty data a tak. Tady je jen prihodim do fragments[]
         titles = new String[]{ HEADER_OVERVIEW_COMPETITION, HEADER_TOURNAMENTS_LIST, HEADER_COMPETITION_STANDINGS };
    }

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {

        PagerAdapter res = new DefaultViewPagerAdapter(manager, fragments, titles);


        return res;
    }
}
