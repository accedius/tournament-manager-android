package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageCommunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.activities.ImportActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.business.serialization.FilesHelper;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.CompetitionAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.AddCompetitionDialog;
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.CompetitionDialog;
import fit.cvut.org.cz.tournamentmanager.presentation.services.CompetitionService;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionsListFragment extends AbstractListFragment<Competition> {
    private String action = "org.cz.cvut.tournamentmanager";
    private String content = "competitions";

    private String package_name;
    private String activity_create_competition;
    private String activity_detail_competition;
    private String stats_service;
    private String sport_context;

    private BroadcastReceiver receiver;

    public void setAction(String action) {
        this.action = action;
    }
    public String getAction() {
        return this.action;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ApplicationInfo sport_package = getArguments().getParcelable("sport_package");
        package_name = sport_package.metaData.getString("package_name");
        activity_create_competition = sport_package.metaData.getString("activity_create_competition");
        activity_detail_competition = sport_package.metaData.getString("activity_detail_competition");
        stats_service = sport_package.metaData.getString("service_stats");
        sport_context = getArguments().getString(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new CompetitionAdapter() {
            @Override
            protected void setOnClickListeners(View v, final long competitionId, final int position, final String name) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClassName(package_name, activity_detail_competition);
                        intent.putExtra(CrossPackageCommunicationConstants.EXTRA_ID, competitionId);
                        intent.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sport_context);
                        startActivity(intent);
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        CompetitionDialog dialog = CompetitionDialog.newInstance(competitionId, position, name, package_name, sport_context, activity_create_competition, stats_service);
                        dialog.show(getFragmentManager(), "EDIT_DELETE");
                        return true;
                    }
                } );
                super.setOnClickListeners(v, competitionId, position, name);
            }
        };
    }

    @Override
    public void askForData() {
        Log.d("CLF", "Asked for data "+sport_context);
        Intent intent = CompetitionService.getStartIntent(action, package_name, sport_context, content, getContext());
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return CompetitionService.isWorking(action);
    }

    @Override
    protected void registerReceivers() {
        Log.d("CLF", "Registered for "+sport_context);
        receiver = new CompetitionsListReceiver();
        IntentFilter filter = new IntentFilter(action);
        filter.addAction(package_name + CrossPackageCommunicationConstants.ACTION_GET_COMPETITION_SERIALIZED);
        filter.addAction(package_name + CrossPackageCommunicationConstants.ACTION_GET_COMPETITION_IMPORT_INFO);
        filter.addAction(CompetitionService.ACTION_DELETE_COMPETITION);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        Log.d("CLF", "Unregistered for "+sport_context);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        getActivity().unregisterReceiver(receiver);
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
                DialogFragment dialog = AddCompetitionDialog.newInstance(view, package_name, sport_context, activity_create_competition, stats_service);
                dialog.show(getFragmentManager(), "ADD_COMPETITION");
            }
        });
        return fab;
    }

    @Override
    protected void afterBindData() {
        orderData(getArguments().getString("order_column"), getArguments().getString("order_type"));
    }

    public void orderData(final String column, final String order) {
        if (adapter == null) return;

        ArrayList<Competition> competitions = adapter.getData();
        if (order.equals("DESC")) {
            Collections.sort(competitions, new Comparator<Competition>() {
                @Override
                public int compare(Competition ls, Competition rs) {
                    return rs.getColumn(column).compareToIgnoreCase(ls.getColumn(column));
                }
            });
        } else {
            Collections.sort(competitions, new Comparator<Competition>() {
                @Override
                public int compare(Competition ls, Competition rs) {
                    return ls.getColumn(column).compareToIgnoreCase(rs.getColumn(column));
                }
            });
        }
        adapter.swapData(competitions);
        adapter.notifyDataSetChanged();
    }

    public class CompetitionsListReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!package_name.equals(intent.getStringExtra(CompetitionService.EXTRA_PACKAGE))) {
                return;
            }

            if (!sport_context.equals(intent.getStringExtra(CompetitionService.EXTRA_SPORT_CONTEXT))) {
                return;
            }

            String type = intent.getStringExtra(CompetitionService.EXTRA_TYPE);

            contentView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            if (type.equals(CompetitionService.EXTRA_COMPETITION)) {
                CompetitionsListFragment.super.bindDataOnView(intent);
            } else if (type.equals(CompetitionService.EXTRA_DELETE)) {
                boolean result = intent.getBooleanExtra(CompetitionService.EXTRA_RESULT, true);
                if (result) {
                    int position = intent.getIntExtra(CompetitionService.EXTRA_POSITION, -1);
                    adapter.delete(position);
                } else {
                    View v = getView().findFocus();
                    Snackbar.make(v, fit.cvut.org.cz.tmlibrary.R.string.competition_not_empty_error, Snackbar.LENGTH_LONG).show();
                }
            } else if (type.equals(CrossPackageCommunicationConstants.EXTRA_EXPORT)) {
                String json = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_JSON);
                String filename = intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_NAME);
                View v = getView().findFocus();
                if (FilesHelper.saveFile(filename, json)) {
                    Snackbar.make(v, fit.cvut.org.cz.tmlibrary.R.string.export_file_created, Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(v, fit.cvut.org.cz.tmlibrary.R.string.export_file_failed, Snackbar.LENGTH_LONG).show();
                }
            } else if (type.equals(CrossPackageCommunicationConstants.EXTRA_IMPORT_INFO)) {
                Intent res = new Intent(getActivity(), ImportActivity.class);
                res.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sport_context);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_SERVICE, stats_service);
                res.putExtra(CrossPackageCommunicationConstants.EXTRA_JSON, intent.getStringExtra(CrossPackageCommunicationConstants.EXTRA_JSON));
                res.putExtra(ImportActivity.COMPETITION, intent.getParcelableExtra(ImportActivity.COMPETITION));
                res.putParcelableArrayListExtra(ImportActivity.TOURNAMENTS, intent.getParcelableArrayListExtra(ImportActivity.TOURNAMENTS));
                res.putParcelableArrayListExtra(ImportActivity.PLAYERS, intent.getParcelableArrayListExtra(ImportActivity.PLAYERS));
                res.putParcelableArrayListExtra(ImportActivity.CONFLICTS, intent.getParcelableArrayListExtra(ImportActivity.CONFLICTS));
                startActivity(res);
            }
        }
    }
}
