package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
    private ListView mDrawerList;

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

        mMenuOptions = new String[]{"Competitions", "Players"};
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.menu_option, mMenuOptions));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        selectItem(0);
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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        switch (position) {
            case 0:
                // get list of installed sport packages
                setTitle("Competitions");
                SportsFragment sf = new SportsFragment();
                Bundle b = new Bundle();
                b.putParcelableArrayList("sport_packages", sport_packages);
                sf.setArguments(b);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, sf)
                        .commit();
                break;
            case 1:
                setTitle("Players");
                PlayersListFragment plf = new PlayersListFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, plf)
                        .commit();
                break;
        }
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
}
