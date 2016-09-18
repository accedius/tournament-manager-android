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

import fit.cvut.org.cz.hockey.presentation.fragments.AggregStatsTitleFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyCompetitionOverviewFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyTournamentsListFragment;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.CompetitionOverviewFragment;

/**
 * Activity showing competition detail
 * Created by atgot_000 on 29. 3. 2016.
 */
public class ShowCompetitionActivity extends AbstractTabActivity {

    private long competitionID = -1;
    private DefaultViewPagerAdapter adapter = null;

    private Fragment[] fragments;
    private String[] titles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        competitionID = getIntent().getExtras().getLong(CrossPackageComunicationConstants.EXTRA_ID);
        titles = new String[]{
                getString(fit.cvut.org.cz.tmlibrary.R.string.overview),
                getString(fit.cvut.org.cz.tmlibrary.R.string.tournaments),
                getString(fit.cvut.org.cz.tmlibrary.R.string.players) };
        Fragment f1 = CompetitionOverviewFragment.newInstance( competitionID, HockeyCompetitionOverviewFragment.class );
        Fragment f2 = HockeyTournamentsListFragment.newInstance( competitionID );
        Fragment f3 = AggregStatsTitleFragment.newInstance(competitionID, true);
        fragments = new Fragment[]{ f1, f2, f3 };
        super.onCreate(savedInstanceState);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fr = getSupportFragmentManager().findFragmentByTag(adapter.getTag(position));
                if (fr != null && fr instanceof AbstractDataFragment)
                    ((AbstractDataFragment) fr).customOnResume();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        adapter = new DefaultViewPagerAdapter(manager, fragments, titles);
        return adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_competition_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case fit.cvut.org.cz.tmlibrary.R.id.action_edit:
                if (competitionID == -1)
                    break;
                Intent intent = new Intent(this, CreateCompetitionActivity.class);
                intent.putExtra(CrossPackageComunicationConstants.EXTRA_ID, competitionID);
                startActivity(intent);
                break;
        }

        return true;
    }
}
