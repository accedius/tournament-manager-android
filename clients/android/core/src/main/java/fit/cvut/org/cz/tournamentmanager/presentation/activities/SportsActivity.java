package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.PackagesInfo;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.CompetitionsListFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.PlayerDetailFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.PlayersListFragment;

/**
 * Created by kevin on 15.10.2016.
 */
public class SportsActivity extends AbstractTabActivity {
    private Fragment[] fragments;
    private String[] titles;
    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawerList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Map<String, ApplicationInfo> contexts = new TreeMap<>();
        for (ApplicationInfo app : PackagesInfo.getPackages(this, getResources())) {
            for (String context : app.metaData.getString("context_names").split(",")) {
                contexts.put(context, app);
            }
        }

        titles = new String[contexts.size()];
        fragments = new Fragment[contexts.size()];
        int i = 0;
        for (Map.Entry<String, ApplicationInfo> entry : contexts.entrySet()) {
            String c = entry.getKey();
            CompetitionsListFragment clf = new CompetitionsListFragment();
            Bundle b = new Bundle();
            b.putParcelable("sport_package", entry.getValue());
            b.putString("context", c);
            b.putString("order_column", Competition.col_end_date);
            b.putString("order_type", "DESC");
            clf.setAction(c + clf.getAction() + "." + entry.getValue().metaData.getString("package_name"));
            clf.setArguments(b);

            titles[i] = c;
            fragments[i] = clf;
            i++;
        }

        super.onCreate(savedInstanceState);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_tab);
        mDrawerList = (NavigationView) findViewById(R.id.left_drawer_tab);

        // Set the list's click listener
        mDrawerList.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
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
    }

    @Override
    protected View injectView(ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.activity_main_tab, parent, false);
    }

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        PagerAdapter res = new DefaultViewPagerAdapter(manager, fragments, titles);
        return res;
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        switch (position) {
            case R.id.competitions:
                break;
            case R.id.players:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
