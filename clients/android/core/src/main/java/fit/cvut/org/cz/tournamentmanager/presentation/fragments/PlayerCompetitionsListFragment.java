package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.CompetitionAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.EditDialog;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class PlayerCompetitionsListFragment extends AbstractListFragment<Competition> {
    private long playerId;

    private String package_name;
    private String sport_context;
    private String activity_create_competition;
    private String activity_detail_competition;
    private String package_service;

    private static String ARG_ID = "player_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        playerId = getArguments().getLong(ARG_ID);
        package_name = getArguments().getString("package_name");
        sport_context = getArguments().getString(CrossPackageConstants.EXTRA_SPORT_CONTEXT);
        activity_create_competition = getArguments().getString("activity_create_competition");
        activity_detail_competition = getArguments().getString("activity_detail_competition");
        package_service = getArguments().getString("package_service");

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
                        intent.putExtra(CrossPackageConstants.EXTRA_ID, competitionId);
                        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context);
                        startActivity(intent);
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        EditDialog dialog = EditDialog.newInstance(package_name, activity_create_competition, competitionId, sport_context);
                        Bundle b = new Bundle();
                        b.putString(EditDialog.ARG_TITLE, name);
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
        Intent intent = new Intent();
        intent.setClassName(package_name, package_service);
        intent.putExtra(CrossPackageConstants.EXTRA_ID, playerId);
        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context);
        intent.putExtra(CrossPackageConstants.EXTRA_ACTION, CrossPackageConstants.ACTION_GET_COMPETITIONS_BY_PLAYER);
        intent.putExtra(CrossPackageConstants.EXTRA_PACKAGE, package_name);
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return false;
    }

    @Override
    protected void registerReceivers() {
        receiver = new CompetitionsListReceiver();
        IntentFilter filter = new IntentFilter(package_name + CrossPackageConstants.ACTION_GET_COMPETITIONS_BY_PLAYER);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    protected String getDataKey() {
        return "extra_competition";
    }

    public class CompetitionsListReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!package_name.equals(intent.getStringExtra(CrossPackageConstants.EXTRA_PACKAGE))) {
                return;
            }

            if (!sport_context.equals(intent.getStringExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT))) {
                return;
            }

            contentView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            PlayerCompetitionsListFragment.super.bindDataOnView(intent);
        }
    }
}
