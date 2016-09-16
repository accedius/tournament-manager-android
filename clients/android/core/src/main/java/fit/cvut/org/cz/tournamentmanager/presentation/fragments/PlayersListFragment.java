package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.EditDeleteDialog;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.PlayerAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.services.PlayerService;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class PlayersListFragment extends AbstractListFragment<Player> {
    ArrayList<ApplicationInfo> sport_packages;

    private String package_name = "fit.cvut.org.cz.tournamentmanager";
    private String activity_create_player = "fit.cvut.org.cz.tournamentmanager.presentation.activities.CreatePlayerActivity";
    private String activity_detail_player = "fit.cvut.org.cz.tournamentmanager.presentation.activities.PlayerDetailActivity";

    private String orderColumn = Player.col_name;
    private String orderType = "ASC";

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floating_button_add, parent, false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClassName(package_name, activity_create_player);
                startActivity(intent);
            }
        });

        return fab;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sport_packages = getArguments().getParcelableArrayList("sport_packages");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void askForData() {
        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_GET_ALL, getContext());
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return PlayerService.isWorking(PlayerService.ACTION_GET_ALL);
    }

    @Override
    protected void registerReceivers() {
        receiver = new PlayersListReceiver();
        IntentFilter filter = new IntentFilter(PlayerService.ACTION_GET_ALL);
        filter.addAction(PlayerService.ACTION_DELETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    public void orderData(final String type) {
        if (adapter == null) return;

        ArrayList<Player> players = adapter.getData();
        if (orderColumn.equals(type) && orderType.equals("ASC")) {
            orderType = "DESC";
            Collections.sort(players, new Comparator<Player>() {
                @Override
                public int compare(Player ls, Player rs) {
                    return rs.getColumn(type).compareToIgnoreCase(ls.getColumn(type));
                }
            });
        } else {
            if (!orderColumn.equals(type)) {
                orderColumn = type;
            }
            orderType = "ASC";
            Collections.sort(players, new Comparator<Player>() {
                @Override
                public int compare(Player ls, Player rs) {
                    return ls.getColumn(type).compareToIgnoreCase(rs.getColumn(type));
                }
            });
        }

        adapter.swapData(players);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new PlayerAdapter() {
            @Override
            protected void setOnClickListeners(View v, final long playerId, final int position, final String name) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClassName(package_name, activity_detail_player);
                        Bundle b = new Bundle();
                        b.putLong(PlayerService.EXTRA_ID, playerId);
                        b.putParcelableArrayList(PlayerService.EXTRA_PACKAGES, sport_packages);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        EditDeleteDialog dialog = new EditDeleteDialog() {
                            @Override
                            protected DialogInterface.OnClickListener supplyListener() {
                                return  new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:{
                                                Intent intent = new Intent();
                                                intent.setClassName(package_name, activity_create_player);
                                                Bundle b = new Bundle();
                                                b.putLong(PlayerService.EXTRA_ID, playerId);
                                                intent.putExtras(b);
                                                startActivity(intent);
                                                break;
                                            }
                                            case 1:{
                                                Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_DELETE, getContext());
                                                intent.putExtra(PlayerService.EXTRA_ID, playerId);
                                                intent.putExtra(PlayerService.EXTRA_POSITION, position);
                                                intent.putParcelableArrayListExtra(PlayerService.EXTRA_PACKAGES, sport_packages);
                                                getContext().startService(intent);
                                            }
                                        }
                                        dialog.dismiss();
                                    }
                                };
                            }
                        };

                        Bundle b = new Bundle();
                        b.putString(EditDeleteDialog.ARG_TITLE, name);
                        dialog.setArguments(b);
                        dialog.show(getFragmentManager(), "EDIT_DELETE");
                        return false;
                    }
                });
            }
        };
    }

    @Override
    protected String getDataKey() {
        return PlayerService.EXTRA_PLAYERS;
    }



    public class PlayersListReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            contentView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            String action = intent.getAction();
            switch (action) {
                case PlayerService.ACTION_GET_ALL:
                    PlayersListFragment.super.bindDataOnView(intent);
                    break;
                case PlayerService.ACTION_DELETE:
                    boolean result = intent.getBooleanExtra(PlayerService.EXTRA_RESULT, false);
                    if (result) {
                        int position = intent.getIntExtra(PlayerService.EXTRA_POSITION, -1);
                        adapter.delete(position);
                    } else {
                        View v = getView().findFocus();
                        if (v != null)
                            Snackbar.make(v, fit.cvut.org.cz.tmlibrary.R.string.player_not_empty_error, Snackbar.LENGTH_LONG).show();
                    }
                    break;
            }
        }
    }
}