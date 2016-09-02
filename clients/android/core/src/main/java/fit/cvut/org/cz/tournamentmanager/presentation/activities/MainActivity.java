package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.PlayersListFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.SportsFragment;

/**
 * Created by kevin on 4.4.2016.
 */
public class MainActivity extends AbstractToolbarActivity {

    private String[] mMenuOptions;
    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawerList;

    ArrayList<ApplicationInfo> sport_packages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<ApplicationInfo> packages = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        sport_packages = new ArrayList<>();

        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.metaData != null) {
                if (packageInfo.metaData.containsKey("application_type") == true
                    && packageInfo.metaData.get("application_type").equals(getString(R.string.tournament_manager_package))) {
                    sport_packages.add(packageInfo);
                }
            }
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (NavigationView) findViewById(R.id.left_drawer);

        // Set the list's click listener
        mDrawerList.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(!item.isChecked());
                mDrawerLayout.closeDrawers();

                selectItem(item.getItemId());
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        selectItem(R.id.competitions);
    }

    @Override
    protected View injectView(ViewGroup parent) {
        View v = getLayoutInflater().inflate(R.layout.activity_main, parent, false);
        return v;
    }

    @Override
    protected FloatingActionButton getFloatingActionButton(ViewGroup root) {
        return null;
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        Bundle b = new Bundle();
        // get list of installed sport packages
        b.putParcelableArrayList("sport_packages", sport_packages);
        switch (position) {
            case R.id.competitions:
                setTitle(fit.cvut.org.cz.tmlibrary.R.string.competitions);
                SportsFragment sf = new SportsFragment();
                sf.setArguments(b);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, sf)
                        .commit();
                break;
            case R.id.players:
                setTitle(fit.cvut.org.cz.tmlibrary.R.string.players);
                PlayersListFragment plf = new PlayersListFragment();
                plf.setArguments(b);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, plf)
                        .commit();
                break;
        }
    }
}
