package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.SortingCompetitionsDialog;
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.SortingPlayersDialog;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.PlayersListFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.SportsFragment;

/**
 * Created by kevin on 4.4.2016.
 */
public class MainActivity extends AbstractToolbarActivity {

    private final String frg_competitions = "competitions";
    private final String frg_players = "players";

    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawerList;

    ArrayList<ApplicationInfo> sport_packages;
    private String active_fragment;
    private View v;
    private PlayersListFragment plf = null;
    private SportsFragment sf = null;

    private String orderColumn = Competition.col_end_date;
    private String orderType = "DESC";

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
        v = getLayoutInflater().inflate(R.layout.activity_main, parent, false);
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
                active_fragment = frg_competitions;
                setTitle(fit.cvut.org.cz.tmlibrary.R.string.competitions);
                sf = new SportsFragment();
                b.putString("order_column", orderColumn);
                b.putString("order_type", orderType);
                sf.setArguments(b);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, sf)
                        .commit();
                break;
            case R.id.players:
                active_fragment = frg_players;
                setTitle(fit.cvut.org.cz.tmlibrary.R.string.players);
                plf = new PlayersListFragment();
                plf.setArguments(b);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, plf)
                        .commit();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sport_detail, menu);
        return true;
    }

    private String getColumnForDialogId(int which) {
        switch (which) {
            case 0: return Competition.col_name;
            case 1: return Competition.col_start_date;
            case 2: return Competition.col_end_date;
            default: return Competition.col_name;
        }

    }

    private void switchOrder() {
        if (orderType.equals("DESC")) {
            orderType = "ASC";
        } else {
            orderType = "DESC";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_order) {
            if (active_fragment.equals(frg_competitions)) {
                SortingCompetitionsDialog dialog = SortingCompetitionsDialog.newInstance();
                dialog.setListener(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (orderColumn == getColumnForDialogId(which)) {
                                    switchOrder();
                                } else {
                                    orderColumn = getColumnForDialogId(which);
                                    orderType = "ASC";
                                }

                                Bundle b = new Bundle();
                                b.putParcelableArrayList("sport_packages", sport_packages);
                                b.putString("order_column", orderColumn);
                                b.putString("order_type", orderType);
                                b.putInt("current_item", sf.getCurrentItem());
                                SportsFragment ssf = new SportsFragment();
                                ssf.setArguments(b);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.content_frame, ssf)
                                        .commit();
                                sf = ssf;
                                dialog.dismiss();
                            }
                        });
                dialog.show(getSupportFragmentManager(), "SORT_COMPETITIONS");
            } else if (active_fragment.equals(frg_players)) {
                SortingPlayersDialog dialog = SortingPlayersDialog.newInstance();
                dialog.setListener(
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:{
                                        plf.orderData(Player.col_name);
                                        break;
                                    }
                                    case 1:{
                                        plf.orderData(Player.col_email);
                                        break;
                                    }
                                }
                                dialog.dismiss();
                            }
                        });
                dialog.show(getSupportFragmentManager(), "SORT_COMPETITIONS");
            }
        }
        return true;
    }
}
