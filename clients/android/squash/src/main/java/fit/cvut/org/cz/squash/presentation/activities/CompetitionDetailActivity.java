package fit.cvut.org.cz.squash.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

import fit.cvut.org.cz.squash.presentation.fragments.AgregatedStatsListFragment;
import fit.cvut.org.cz.squash.presentation.fragments.SquashCompetitionOverviewFragment;
import fit.cvut.org.cz.squash.presentation.fragments.StatsListWrapperFragment;
import fit.cvut.org.cz.squash.presentation.fragments.TournamentsListFragment;
import fit.cvut.org.cz.squash.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.CompetitionOverviewFragment;

/**
 * Created by Vaclav on 5. 4. 2016.
 */
public class CompetitionDetailActivity extends AbstractTabActivity {

    private long competitionId = -1;

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        competitionId = getIntent().getExtras().getLong(CrossPackageComunicationConstants.EXTRA_ID);
        return new DefaultViewPagerAdapter(getSupportFragmentManager(),
                new Fragment[]{
                        CompetitionOverviewFragment.newInstance(competitionId, SquashCompetitionOverviewFragment.class),
                        TournamentsListFragment.newInstance(competitionId),
                        StatsListWrapperFragment.newInstance(competitionId, StatsService.ACTION_GET_STATS_BY_COMPETITION)},
                new String[] {"Overview", "Tournaments", "Stats && Players"});
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
                if (competitionId == -1) break;
                Intent intent = new Intent(this, CreateCompetitionActivity.class);
                intent.putExtra(CrossPackageComunicationConstants.EXTRA_ID, competitionId);
                startActivity(intent);
                break;
            }
            default:
                break;
        }

        return true;
    }
}
