package fit.cvut.org.cz.squash.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.fragments.SquashCompetitionOverviewFragment;
import fit.cvut.org.cz.squash.presentation.fragments.StatsListWrapperFragment;
import fit.cvut.org.cz.squash.presentation.fragments.TournamentsListFragment;
import fit.cvut.org.cz.squash.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.CompetitionOverviewFragment;

/**
 * This activity contains tabs that are needed to display all info about competition
 * thus, Overview, Tournamets and Stats and players
 * Created by Vaclav on 5. 4. 2016.
 */
public class CompetitionDetailActivity extends AbstractTabActivity {

    private long competitionId = -1;
    private DefaultViewPagerAdapter adapter = null;

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        competitionId = getIntent().getExtras().getLong(CrossPackageComunicationConstants.EXTRA_ID);
        adapter = new DefaultViewPagerAdapter(getSupportFragmentManager(),
                new Fragment[]{
                        CompetitionOverviewFragment.newInstance(competitionId, SquashCompetitionOverviewFragment.class),
                        TournamentsListFragment.newInstance(competitionId),
                        StatsListWrapperFragment.newInstance(competitionId, StatsService.ACTION_GET_STATS_BY_COMPETITION)},
                new String[] {getResources().getString(R.string.overview), getResources().getString(R.string.tournaments), getResources().getString(R.string.players)});
        return adapter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fr = getSupportFragmentManager().findFragmentByTag(adapter.getTag(position));
                if (fr != null){
                    if (fr instanceof AbstractDataFragment) ((AbstractDataFragment) fr).customOnResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
                if (competitionId == -1)
                    break;
                Intent intent = new Intent(this, CreateCompetitionActivity.class);
                intent.putExtra(CrossPackageComunicationConstants.EXTRA_ID, competitionId);
                startActivity(intent);
                break;
            default:
                break;
        }

        return true;
    }
}
