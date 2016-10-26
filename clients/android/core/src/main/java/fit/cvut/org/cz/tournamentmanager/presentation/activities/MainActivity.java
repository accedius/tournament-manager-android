package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.SortingPlayersDialog;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.PlayersListFragment;

/**
 * Created by kevin on 4.4.2016.
 */
public class MainActivity extends AbstractToolbarActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawerList;

    private PlayersListFragment plf = null;
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
        mDrawerList.getMenu().getItem(1).setChecked(true);

        setTitle(fit.cvut.org.cz.tmlibrary.R.string.players);
        plf = new PlayersListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, plf).commit();

        selectItem(R.id.players);
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
                Intent intent = new Intent(this, SportsActivity.class);
                intent.putExtra(AbstractTabActivity.ARG_TABMODE, TabLayout.MODE_SCROLLABLE);
                startActivity(intent);
                break;
            case R.id.players:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sport_detail, menu);
        return true;
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
        if (item.getItemId() == R.id.action_order) {
            SortingPlayersDialog dialog = SortingPlayersDialog.newInstance();
            dialog.setListener(getSortingPlayersListener());
            dialog.show(getSupportFragmentManager(), "SORT_PLAYERS");
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
}
