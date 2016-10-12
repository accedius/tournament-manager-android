package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
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

import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageCommunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.CompetitionAdapter;
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
                        Bundle b = new Bundle();
                        b.putLong(CrossPackageCommunicationConstants.EXTRA_ID, competitionId);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        CompetitionDialog dialog = CompetitionDialog.newInstance(competitionId, position, name, package_name, activity_create_competition);
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
        Intent intent = CompetitionService.getStartIntent(this.action, this.package_name, this.content, getContext());
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return CompetitionService.isWorking(this.action);
    }

    @Override
    protected void registerReceivers() {
        receiver = new CompetitionsListReceiver();
        IntentFilter filter = new IntentFilter(this.action);
        filter.addAction(CompetitionDialog.ACTION_DELETE_COMPETITION);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
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

    @Override
    protected void afterBindData() {
        ArrayList<Competition> data = adapter.getData();
        orderData(data, getArguments().getString("order_column"), getArguments().getString("order_type"));
        adapter.swapData(data);
        adapter.notifyDataSetChanged();
    }

    public void orderData(ArrayList<Competition> data, final String column, final String order) {
        if (adapter == null) return;

        ArrayList<Competition> players = data;
        if (order.equals("DESC")) {
            Collections.sort(players, new Comparator<Competition>() {
                @Override
                public int compare(Competition ls, Competition rs) {
                    return rs.getColumn(column).compareToIgnoreCase(ls.getColumn(column));
                }
            });
        } else {
            Collections.sort(players, new Comparator<Competition>() {
                @Override
                public int compare(Competition ls, Competition rs) {
                    return ls.getColumn(column).compareToIgnoreCase(rs.getColumn(column));
                }
            });
        }
    }

    public class CompetitionsListReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!package_name.equals(intent.getStringExtra(CompetitionService.EXTRA_PACKAGE))) {
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
                    if (v != null)
                        Snackbar.make(v, fit.cvut.org.cz.tmlibrary.R.string.competition_not_empty_error, Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }
}
