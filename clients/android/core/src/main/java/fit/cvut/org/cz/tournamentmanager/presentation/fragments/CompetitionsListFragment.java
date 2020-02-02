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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.entities.communication.Constants;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.helpers.DBConstants;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tmlibrary.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.activities.ImportActivity;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.CompetitionAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.AddCompetitionDialog;
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.CompetitionDialog;
import fit.cvut.org.cz.tournamentmanager.presentation.helpers.FilesHelper;

/**
 * Fragment to display list of Competitions.
 */
public class CompetitionsListFragment extends AbstractListFragment<Competition> {
    private String action = "org.cz.cvut.tournamentmanager";

    private String packageName;
    private String activityCreateCompetition;
    private String activityDetailCompetition;
    private String packageService;
    private String sportContext;

    private String orderColumn = DBConstants.cEND;
    private String orderType = Constants.ORDER_DESC;

    private BroadcastReceiver receiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ApplicationInfo sport_package = getArguments().getParcelable(CrossPackageConstants.SPORT_PACKAGE);
        packageName = sport_package.metaData.getString(CrossPackageConstants.PACKAGE_NAME);
        activityCreateCompetition = sport_package.metaData.getString(CrossPackageConstants.ACTIVITY_CREATE_COMPETITION);
        activityDetailCompetition = sport_package.metaData.getString(CrossPackageConstants.ACTIVITY_DETAIL_COMPETITION);
        packageService = sport_package.metaData.getString(CrossPackageConstants.PACKAGE_SERVICE);
        sportContext = getArguments().getString(CrossPackageConstants.EXTRA_SPORT_CONTEXT);
        action = sportContext + action + "." + packageName;
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
                        intent.setClassName(packageName, activityDetailCompetition);
                        intent.putExtra(CrossPackageConstants.EXTRA_ID, competitionId);
                        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                        startActivity(intent);
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        CompetitionDialog dialog = CompetitionDialog.newInstance(competitionId, position, name, packageName, sportContext, activityCreateCompetition, packageService);
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
        Intent intent = new Intent();
        intent.setClassName(packageName, packageService);
        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
        intent.putExtra(CrossPackageConstants.EXTRA_ACTION, CrossPackageConstants.ACTION_GET_ALL_COMPETITIONS);
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
        IntentFilter filter = new IntentFilter(action);
        filter.addAction(packageName + CrossPackageConstants.ACTION_GET_COMPETITION_SERIALIZED);
        filter.addAction(packageName + CrossPackageConstants.ACTION_GET_COMPETITION_IMPORT_INFO);
        filter.addAction(packageName + CrossPackageConstants.ACTION_GET_ALL_COMPETITIONS);
        filter.addAction(packageName + CrossPackageConstants.ACTION_DELETE_COMPETITION);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    protected String getDataKey() {
        return CrossPackageConstants.EXTRA_COMPETITION;
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floating_button_add, parent, false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = AddCompetitionDialog.newInstance(view, packageName, sportContext, activityCreateCompetition, packageService);
                dialog.show(getFragmentManager(), "ADD_COMPETITION");
            }
        });
        return fab;
    }

    @Override
    protected void afterBindData() {
        orderData(orderType);
    }

    /**
     * Method to order Competitions.
     * @param type column for order
     */
    public void orderData(final String type) {
        if (adapter == null) return;

        List<Competition> competitions = adapter.getData();
        if (orderColumn.equals(type) && orderType.equals(Constants.ORDER_ASC)) {
            orderType = Constants.ORDER_DESC;
            Collections.sort(competitions, new Comparator<Competition>() {
                @Override
                public int compare(Competition ls, Competition rs) {
                    return rs.getColumn(type).compareToIgnoreCase(ls.getColumn(type));
                }
            });
        } else {
            if (!orderColumn.equals(type)) {
                orderColumn = type;
            }
            orderType = Constants.ORDER_ASC;
            Collections.sort(competitions, new Comparator<Competition>() {
                @Override
                public int compare(Competition ls, Competition rs) {
                    return ls.getColumn(type).compareToIgnoreCase(rs.getColumn(type));
                }
            });
        }

        adapter.swapData(competitions);
        adapter.notifyDataSetChanged();
    }

    /**
     * Receiver for Competitions.
     */
    public class CompetitionsListReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!packageName.equals(intent.getStringExtra(CrossPackageConstants.EXTRA_PACKAGE))) {
                return;
            }

            if (!sportContext.equals(intent.getStringExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT))) {
                return;
            }

            String type = intent.getStringExtra(CrossPackageConstants.EXTRA_TYPE);

            contentView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            if (type.equals(CrossPackageConstants.TYPE_COMPETITION)) {
                CompetitionsListFragment.super.bindDataOnView(intent);
            } else if (type.equals(CrossPackageConstants.TYPE_DELETE)) {
                boolean result = intent.getBooleanExtra(CrossPackageConstants.EXTRA_RESULT, false);
                if (result) {
                    int position = intent.getIntExtra(CrossPackageConstants.EXTRA_POSITION, -1);
                    adapter.delete(position);
                } else {
                    View v = getView().findFocus();
                    Snackbar.make(v, fit.cvut.org.cz.tmlibrary.R.string.competition_not_empty_error, Snackbar.LENGTH_LONG).show();
                }
            } else if (type.equals(CrossPackageConstants.EXTRA_EXPORT)) {
                String json = intent.getStringExtra(CrossPackageConstants.EXTRA_JSON);
                String filename = intent.getStringExtra(CrossPackageConstants.EXTRA_NAME);
                View v = getView();
                if (FilesHelper.saveFile(filename, json)) {
                    Snackbar.make(v, fit.cvut.org.cz.tmlibrary.R.string.export_file_created, Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(v, fit.cvut.org.cz.tmlibrary.R.string.export_file_failed, Snackbar.LENGTH_LONG).show();
                }
            } else if (type.equals(CrossPackageConstants.EXTRA_IMPORT_INFO)) {
                Intent res = new Intent(getActivity(), ImportActivity.class);
                res.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                res.putExtra(CrossPackageConstants.EXTRA_PACKAGE, packageName);
                res.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
                res.putExtra(CrossPackageConstants.EXTRA_PACKAGE_SERVICE, packageService);
                res.putExtra(CrossPackageConstants.EXTRA_JSON, intent.getStringExtra(CrossPackageConstants.EXTRA_JSON));
                res.putExtra(ExtraConstants.EXTRA_COMPETITION, intent.getParcelableExtra(ExtraConstants.EXTRA_COMPETITION));
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_TOURNAMENTS, intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_TOURNAMENTS));
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS, intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS));
                res.putParcelableArrayListExtra(ExtraConstants.EXTRA_CONFLICTS, intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_CONFLICTS));
                startActivity(res);
            }
        }
    }
}
