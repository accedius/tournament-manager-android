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
import fit.cvut.org.cz.tmlibrary.presentation.activities.ImportActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.CompetitionAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.AddCompetitionDialog;
import fit.cvut.org.cz.tournamentmanager.presentation.dialogs.CompetitionDialog;
import fit.cvut.org.cz.tournamentmanager.presentation.helpers.FilesHelper;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class CompetitionsListFragment extends AbstractListFragment<Competition> {
    private String action = "org.cz.cvut.tournamentmanager";

    private String package_name;
    private String activity_create_competition;
    private String activity_detail_competition;
    private String package_service;
    private String sport_context;

    private String orderColumn = DBConstants.cEND;
    private String orderType = Constants.ORDER_DESC;

    private BroadcastReceiver receiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ApplicationInfo sport_package = getArguments().getParcelable(CrossPackageConstants.SPORT_PACKAGE);
        package_name = sport_package.metaData.getString(CrossPackageConstants.PACKAGE_NAME);
        activity_create_competition = sport_package.metaData.getString(CrossPackageConstants.ACTIVITY_CREATE_COMPETITION);
        activity_detail_competition = sport_package.metaData.getString(CrossPackageConstants.ACTIVITY_DETAIL_COMPETITION);
        package_service = sport_package.metaData.getString(CrossPackageConstants.PACKAGE_SERVICE);
        sport_context = getArguments().getString(CrossPackageConstants.EXTRA_SPORT_CONTEXT);
        action = sport_context+action+"."+package_name;
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
                        intent.putExtra(CrossPackageConstants.EXTRA_ID, competitionId);
                        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context);
                        startActivity(intent);
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        CompetitionDialog dialog = CompetitionDialog.newInstance(competitionId, position, name, package_name, sport_context, activity_create_competition, package_service);
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
        intent.setClassName(package_name, package_service);
        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context);
        intent.putExtra(CrossPackageConstants.EXTRA_ACTION, CrossPackageConstants.ACTION_GET_ALL_COMPETITIONS);
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
        IntentFilter filter = new IntentFilter(action);
        filter.addAction(package_name + CrossPackageConstants.ACTION_GET_COMPETITION_SERIALIZED);
        filter.addAction(package_name + CrossPackageConstants.ACTION_GET_COMPETITION_IMPORT_INFO);
        filter.addAction(package_name + CrossPackageConstants.ACTION_GET_ALL_COMPETITIONS);
        filter.addAction(package_name + CrossPackageConstants.ACTION_DELETE_COMPETITION);
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
                DialogFragment dialog = AddCompetitionDialog.newInstance(view, package_name, sport_context, activity_create_competition, package_service);
                dialog.show(getFragmentManager(), "ADD_COMPETITION");
            }
        });
        return fab;
    }

    @Override
    protected void afterBindData() {
        orderData(orderType);
    }

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

    public class CompetitionsListReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!package_name.equals(intent.getStringExtra(CrossPackageConstants.EXTRA_PACKAGE))) {
                return;
            }

            if (!sport_context.equals(intent.getStringExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT))) {
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
                View v = getView().findFocus();
                if (FilesHelper.saveFile(filename, json)) {
                    Snackbar.make(v, fit.cvut.org.cz.tmlibrary.R.string.export_file_created, Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(v, fit.cvut.org.cz.tmlibrary.R.string.export_file_failed, Snackbar.LENGTH_LONG).show();
                }
            } else if (type.equals(CrossPackageConstants.EXTRA_IMPORT_INFO)) {
                Intent res = new Intent(getActivity(), ImportActivity.class);
                res.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                res.putExtra(CrossPackageConstants.EXTRA_PACKAGE, package_name);
                res.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_context);
                res.putExtra(CrossPackageConstants.EXTRA_SPORT_SERVICE, package_service);
                res.putExtra(CrossPackageConstants.EXTRA_JSON, intent.getStringExtra(CrossPackageConstants.EXTRA_JSON));
                res.putExtra(ImportActivity.COMPETITION, intent.getParcelableExtra(ImportActivity.COMPETITION));
                res.putParcelableArrayListExtra(ImportActivity.TOURNAMENTS, intent.getParcelableArrayListExtra(ImportActivity.TOURNAMENTS));
                res.putParcelableArrayListExtra(ImportActivity.PLAYERS, intent.getParcelableArrayListExtra(ImportActivity.PLAYERS));
                res.putParcelableArrayListExtra(ImportActivity.CONFLICTS, intent.getParcelableArrayListExtra(ImportActivity.CONFLICTS));
                startActivity(res);
            }
        }
    }
}
