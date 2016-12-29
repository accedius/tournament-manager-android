package fit.cvut.org.cz.hockey.presentation.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import fit.cvut.org.cz.hockey.presentation.HockeyPackage;
import fit.cvut.org.cz.hockey.presentation.fragments.AggregStatsTitleFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyCompetitionOverviewFragment;
import fit.cvut.org.cz.hockey.presentation.fragments.HockeyTournamentsListFragment;
import fit.cvut.org.cz.tmlibrary.business.serialization.Constants;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.SortingTournamentsDialog;
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
    private String sportContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        sportContext = getIntent().getExtras().getString(CrossPackageConstants.EXTRA_SPORT_CONTEXT);
        ((HockeyPackage) this.getApplication()).setSportContext(sportContext);

        competitionID = getIntent().getExtras().getLong(CrossPackageConstants.EXTRA_ID);
        titles = new String[]{
                getString(fit.cvut.org.cz.tmlibrary.R.string.overview),
                getString(fit.cvut.org.cz.tmlibrary.R.string.tournaments),
                getString(fit.cvut.org.cz.tmlibrary.R.string.players) };
        Fragment f1 = CompetitionOverviewFragment.newInstance(competitionID, HockeyCompetitionOverviewFragment.class);
        Fragment f2 = HockeyTournamentsListFragment.newInstance(competitionID);
        Fragment f3 = AggregStatsTitleFragment.newInstance(competitionID, true);
        fragments = new Fragment[]{ f1, f2, f3 };
        super.onCreate(savedInstanceState);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

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
    protected void onResume() {
        ((HockeyPackage)getApplication()).setSportContext(sportContext);
        super.onResume();
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
                Intent intent = new Intent(this, CreateCompetitionActivity.class);
                intent.putExtra(CrossPackageConstants.EXTRA_ID, competitionID);
                intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, ((HockeyPackage) this.getApplication()).getSportContext());
                startActivity(intent);
                break;
            case fit.cvut.org.cz.tmlibrary.R.id.action_order:
                SortingTournamentsDialog dialog = SortingTournamentsDialog.newInstance();
                dialog.setListener(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:{
                                        ((HockeyTournamentsListFragment)fragments[1]).orderData(Constants.NAME);
                                        break;
                                    }
                                    case 1:{
                                        ((HockeyTournamentsListFragment)fragments[1]).orderData(Constants.START);
                                        break;
                                    }
                                    case 2:{
                                        ((HockeyTournamentsListFragment)fragments[1]).orderData(Constants.END);
                                        break;
                                    }
                                }
                                dialog.dismiss();
                            }
                        });
                dialog.show(getSupportFragmentManager(), "SORT_PLAYERS");
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
