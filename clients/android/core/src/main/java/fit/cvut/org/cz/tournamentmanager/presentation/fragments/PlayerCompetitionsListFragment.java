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
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.CompetitionAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.EditDialog;

/**
 * Fragment to display list of Competitions for player.
 */
public class PlayerCompetitionsListFragment extends AbstractListFragment<Competition> {
    private long playerId;

    private String packageName;
    private String sportContext;
    private String activityCreateCompetition;
    private String activityDetailCompetition;
    private String packageService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        playerId = getArguments().getLong(ExtraConstants.EXTRA_ID);
        packageName = getArguments().getString(CrossPackageConstants.PACKAGE_NAME);
        sportContext = getArguments().getString(CrossPackageConstants.EXTRA_SPORT_CONTEXT);
        activityCreateCompetition = getArguments().getString(CrossPackageConstants.ACTIVITY_CREATE_COMPETITION);
        activityDetailCompetition = getArguments().getString(CrossPackageConstants.ACTIVITY_DETAIL_COMPETITION);
        packageService = getArguments().getString(CrossPackageConstants.PACKAGE_SERVICE);

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
                        intent.setClassName(packageName, activityDetailCompetition);
                        intent.putExtra(CrossPackageConstants.EXTRA_ID, competitionId);
                        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                        startActivity(intent);
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        EditDialog dialog = EditDialog.newInstance(packageName, activityCreateCompetition, competitionId, sportContext);
                        Bundle b = new Bundle();
                        b.putString(ExtraConstants.EXTRA_TITLE, name);
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
        intent.setClassName(packageName, packageService);
        intent.putExtra(CrossPackageConstants.EXTRA_ID, playerId);
        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
        intent.putExtra(CrossPackageConstants.EXTRA_ACTION, CrossPackageConstants.ACTION_GET_COMPETITIONS_BY_PLAYER);
        intent.putExtra(CrossPackageConstants.EXTRA_PACKAGE, packageName);
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return false;
    }

    @Override
    protected void registerReceivers() {
        receiver = new CompetitionsListReceiver();
        IntentFilter filter = new IntentFilter(packageName + CrossPackageConstants.ACTION_GET_COMPETITIONS_BY_PLAYER);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    protected String getDataKey() {
        return CrossPackageConstants.EXTRA_COMPETITION;
    }

    /**
     * Receiver for Competitions.
     */
    public class CompetitionsListReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!packageName.equals(intent.getStringExtra(CrossPackageConstants.EXTRA_PACKAGE)))
                return;

            if (!sportContext.equals(intent.getStringExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT)))
                return;

            contentView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            PlayerCompetitionsListFragment.super.bindDataOnView(intent);
        }
    }
}
