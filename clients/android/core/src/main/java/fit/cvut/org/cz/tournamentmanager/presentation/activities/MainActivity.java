package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.SortingCompetitionsDialog;
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.SortingPlayersDialog;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.PlayersListFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.SettingsFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.SportsFragment;

/**
 * Created by kevin on 4.4.2016.
 */
public class MainActivity extends AbstractToolbarActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawerList;

    private SportsFragment cf = null;
    private PlayersListFragment plf = null;
    private SettingsFragment sf = null;
    private int selectedItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (NavigationView) findViewById(R.id.left_drawer);

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
        mDrawerList.getMenu().getItem(0).setChecked(true);

        selectItem(R.id.competitions);
    }

    @Override
    protected View injectView(ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.activity_main, parent, false);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        selectItem(selectedItem);
        super.onConfigurationChanged(newConfig);
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        selectedItem = position;
        switch (position) {
            case R.id.competitions:
                setTitle(fit.cvut.org.cz.tmlibrary.R.string.competitions);
                cf = new SportsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, cf).commit();
                break;
            case R.id.players:
                setTitle(fit.cvut.org.cz.tmlibrary.R.string.players);
                plf = new PlayersListFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, plf).commit();
                break;
            case R.id.settings:
                setTitle(fit.cvut.org.cz.tmlibrary.R.string.settings);
                sf = new SettingsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, sf).commit();
                break;
        }
    }

    @Override
    protected boolean displayUpButton() {
        return false;
    }

    @Override
    protected void onUpButtonClicked() {
        //we want this method to be empty by default
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_players_order) {
            SortingPlayersDialog dialog = SortingPlayersDialog.newInstance();
            dialog.setListener(getSortingPlayersListener());
            dialog.show(getSupportFragmentManager(), "SORT_PLAYERS");
        } else if (item.getItemId() == R.id.action_order) {
            SortingCompetitionsDialog dialog = SortingCompetitionsDialog.newInstance();
            dialog.setListener(getSortingCompetitionsListener());
            dialog.show(getSupportFragmentManager(), "SORT_COMPETITIONS");
        }
        return super.onOptionsItemSelected(item);
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

    private DialogInterface.OnClickListener getSortingPlayersListener() {
        return new DialogInterface.OnClickListener() {
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
        };
    }

    private DialogInterface.OnClickListener getSortingCompetitionsListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:{
                        cf.orderData(Competition.col_name);
                        break;
                    }
                    case 1:{
                        cf.orderData(Competition.col_start_date);
                        break;
                    }
                    case 2:{
                        cf.orderData(Competition.col_end_date);
                        break;
                    }
                }
                dialog.dismiss();
            }
        };
    }
}
