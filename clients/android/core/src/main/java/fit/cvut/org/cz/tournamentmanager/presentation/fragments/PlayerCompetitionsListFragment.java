package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.EditDeleteDialog;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.CompetitionAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.services.CompetitionService;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class PlayerCompetitionsListFragment extends AbstractListFragment<Competition> {

    private long playerID;
    private String action = "org.cz.cvut.tournamentmanager";
    private String content = "competitions_by_player/";

    private String package_name;
    private String activity_create_competition;
    private String activity_detail_competition;

    private static String ARG_ID = "player_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        playerID = getArguments().getLong(ARG_ID);
        package_name = getArguments().getString("package_name");
        activity_create_competition = getArguments().getString("activity_create_competition");
        activity_detail_competition = getArguments().getString("activity_detail_competition");
        action += "." + package_name;

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new CompetitionAdapter() {
            @Override
            protected void setOnClickListeners(View v, final long competitionId, int position, final String name) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClassName(package_name, activity_detail_competition);
                        Bundle b = new Bundle();
                        b.putLong(CrossPackageComunicationConstants.EXTRA_ID, competitionId);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        EditDeleteDialog dialog = new EditDeleteDialog() {
                            @Override
                            protected DialogInterface.OnClickListener supplyListener() {
                                return  new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:{
                                                Intent intent = new Intent();
                                                intent.setClassName(package_name, activity_create_competition);
                                                Bundle b = new Bundle();
                                                b.putLong(CrossPackageComunicationConstants.EXTRA_ID, competitionId);
                                                intent.putExtras(b);
                                                startActivity(intent);
                                                break;
                                            }
                                            case 1:{
                                                //TODO Zde bude maximálně odebrání hráče ze soutěže, pokud vůbec... Mazání soutěže je hloupost, to nikdy nepůjde
                                                Snackbar.make(v, "delete not yet implemented", Snackbar.LENGTH_SHORT).show();
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
    public void askForData() {
        Intent intent = CompetitionService.getStartIntent(this.action, this.package_name, this.content + this.playerID, getContext());
        Log.d("PCLF - ACTION", this.action);
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return CompetitionService.isWorking(this.action);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(this.action));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected String getDataKey() {
        return CompetitionService.EXTRA_COMPETITION;
    }
}
