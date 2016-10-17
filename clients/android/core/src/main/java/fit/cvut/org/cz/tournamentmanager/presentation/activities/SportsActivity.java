package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;
import java.util.TreeMap;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageCommunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.PackagesInfo;
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.SortingCompetitionsDialog;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.CompetitionsListFragment;

/**
 * Created by kevin on 15.10.2016.
 */
public class SportsActivity extends AbstractTabActivity {
    private Fragment[] fragments;
    private String[] titles;
    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawerList;

    private String orderColumn = Competition.col_end_date;
    private String orderType = "DESC";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Map<String, ApplicationInfo> contexts = PackagesInfo.getSportContexts(this, getResources());

        titles = new String[contexts.size()];
        fragments = new Fragment[contexts.size()];
        int i = 0;
        for (Map.Entry<String, ApplicationInfo> entry : contexts.entrySet()) {
            String sport_context = entry.getKey();
            CompetitionsListFragment clf = new CompetitionsListFragment();
            Bundle b = new Bundle();
            b.putParcelable("sport_package", entry.getValue());
            b.putString(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sport_context);
            b.putString("order_column", orderColumn);
            b.putString("order_type", orderType);
            // TODO možná vynechat setAction, zbytečné, competitions list receiver může filtrovat také...
            clf.setAction(sport_context + clf.getAction() + "." + entry.getValue().metaData.getString("package_name"));
            clf.setArguments(b);

            titles[i] = sport_context;
            fragments[i] = clf;
            i++;
        }

        // null fixes bug with bad competition list after orientation change
        super.onCreate(null);

        TabLayout tabLayout = (TabLayout) findViewById(fit.cvut.org.cz.tmlibrary.R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_tab);
        mDrawerList = (NavigationView) findViewById(R.id.left_drawer_tab);

        // Set the list's click listener
        mDrawerList.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.isChecked()) {
                    mDrawerLayout.closeDrawers();
                    return true;
                }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sport_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_order) {
            SortingCompetitionsDialog dialog = SortingCompetitionsDialog.newInstance();
            dialog.setListener(getSortingCompetitionsListener());
            dialog.show(getSupportFragmentManager(), "SORT_COMPETITIONS");
        }
        return true;
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.exit_title)
                    .setMessage(R.string.exit_text)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            finish();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private DialogInterface.OnClickListener getSortingCompetitionsListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (orderColumn == getColumnForDialogId(which)) {
                    switchOrder();
                } else {
                    orderColumn = getColumnForDialogId(which);
                    orderType = "ASC";
                }

                for (Fragment f : fragments) {
                    CompetitionsListFragment clf = (CompetitionsListFragment) f;
                    clf.orderData(orderColumn, orderType);
                }

                dialog.dismiss();
            }
        };
    }
}
