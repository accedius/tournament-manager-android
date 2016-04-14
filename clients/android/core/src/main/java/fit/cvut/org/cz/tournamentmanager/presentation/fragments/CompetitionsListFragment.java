package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.squash.presentation.dialogs.EditDeleteDialog;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.CompetitionAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.services.CompetitionService;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionsListFragment extends AbstractListFragment<Competition> {

    // TODO refactor - action name may be empty or shorter - is alwayas setting by SportFragment
    private String action = "org.cz.cvut.tournamentmanager.action";

    private String package_name;
    private String activity_create_competition;
    private String activity_detail_competition;

    public void setAction(String action) {
        this.action = action;
    }
    public String getAction() {
        return this.action;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        package_name = getArguments().getString("package_name");
        activity_create_competition = getArguments().getString("activity_create_competition");
        activity_detail_competition = getArguments().getString("activity_detail_competition");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new CompetitionAdapter() {
            @Override
            protected void setOnClickListeners(View v, final long competitionId) {
                final Context c = getContext();

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
                        final View fw =v;

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
    protected void askForData() {
        Intent intent = CompetitionService.getStartIntent(this.action, this.package_name, getActivity());
        Log.d("CLF - ACTION", this.action);
        getActivity().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return CompetitionService.isWorking(this.action);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, new IntentFilter(this.action));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
    }

    @Override
    protected String getDataKey() {
        return CompetitionService.EXTRA_COMPETITION;
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floating_button_add, parent, false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClassName(package_name, activity_create_competition);
                startActivity(intent);
            }
        });

        return fab;
    }
}
