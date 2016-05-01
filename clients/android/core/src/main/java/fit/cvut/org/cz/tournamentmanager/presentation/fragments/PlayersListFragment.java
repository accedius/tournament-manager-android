package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

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

import fit.cvut.org.cz.squash.presentation.dialogs.EditDeleteDialog;
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
    protected void askForData() {
        Intent intent = PlayerService.newStartIntent(PlayerService.ACTION_GET_ALL, getActivity());
        getActivity().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return PlayerService.isWorking(PlayerService.ACTION_GET_ALL);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(PlayerService.ACTION_GET_ALL));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new PlayerAdapter() {
            @Override
            protected void setOnClickListeners(View v, final long playerId) {
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
                    public boolean onLongClick(final View v) {
                        final View fw = v;
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
                                                //TODO Delete not implemented yet
                                                Snackbar.make(fw, "delete not yet implemented", Snackbar.LENGTH_SHORT).show();
                                            }
                                        }
                                        dialog.dismiss();
                                    }
                                };
                            }
                        };
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


}