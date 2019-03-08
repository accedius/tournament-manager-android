package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.managers.PackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tournamentmanager.business.ManagerFactory;
import fit.cvut.org.cz.tournamentmanager.data.entities.Setting;
import fit.cvut.org.cz.tournamentmanager.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.PlayerDetailFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.PlayerSportFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.helpers.PackagesInfo;

/**
 * Activity for displaying detail of Player.
 */
public class PlayerDetailActivity extends AbstractTabActivity {
    private long playerID;

    private Fragment[] fragments;
    private String[] titles;
    private Map<String, ApplicationInfo> sportContexts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        playerID = getIntent().getExtras().getLong(CrossPackageConstants.EXTRA_ID);
        sportContexts = PackagesInfo.getSportContexts(this);

        List<Setting> ignoredSports = ManagerFactory.getInstance(this).getEntityManager(Setting.class).getAll();
        for (Setting s : ignoredSports) {
            if (sportContexts.containsKey(s.getSportName())) {
                sportContexts.remove(s.getSportName());
            }
        }

        titles = new String[1+ sportContexts.size()];
        titles[0] = getResources().getString(R.string.player_info);

        Fragment f1 = PlayerDetailFragment.newInstance(playerID, PlayerDetailFragment.class);
        fragments = new Fragment[1+ sportContexts.size()];
        fragments[0] = f1;

        int i=1;
        for (Map.Entry<String, ApplicationInfo> sport_context : sportContexts.entrySet()) {
            ApplicationInfo info = sport_context.getValue();
            PlayerSportFragment psf = new PlayerSportFragment();
            Bundle b = new Bundle();
            b.putLong(ExtraConstants.EXTRA_ID, playerID);
            b.putString(CrossPackageConstants.PACKAGE_NAME, info.metaData.getString(CrossPackageConstants.PACKAGE_NAME));
            b.putString(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context.getKey());
            b.putString(CrossPackageConstants.ACTIVITY_CREATE_COMPETITION, info.metaData.getString(CrossPackageConstants.ACTIVITY_CREATE_COMPETITION));
            b.putString(CrossPackageConstants.ACTIVITY_DETAIL_COMPETITION, info.metaData.getString(CrossPackageConstants.ACTIVITY_DETAIL_COMPETITION));
            b.putString(CrossPackageConstants.PACKAGE_SERVICE, info.metaData.getString(CrossPackageConstants.PACKAGE_SERVICE));
            psf.setArguments(b);
            fragments[i] = psf;
            titles[i] = getResources().getString(getResources().getIdentifier(sport_context.getKey(), "string", getPackageName()));
            i++;
        }

        super.onCreate(savedInstanceState);

        TabLayout tabLayout = (TabLayout) findViewById(fit.cvut.org.cz.tmlibrary.R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        return new DefaultViewPagerAdapter(manager, fragments, titles);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:{
                if (playerID == -1) break;
                Intent intent = new Intent(this, CreatePlayerActivity.class);
                ArrayList<Player> players = new ArrayList<>(new PackagePlayerManager(this).getAll());
                intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, players);
                intent.putExtra(ExtraConstants.EXTRA_ID, playerID);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
