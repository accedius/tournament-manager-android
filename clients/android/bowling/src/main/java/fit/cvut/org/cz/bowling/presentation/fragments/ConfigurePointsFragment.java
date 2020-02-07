package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.presentation.activities.ShowPointConfigurationActivity;
import fit.cvut.org.cz.bowling.presentation.adapters.PointConfigurationAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.dialogs.BowlingInsertPointConfigurationDialog;
import fit.cvut.org.cz.bowling.presentation.dialogs.InsertPointConfigurationDialog;
import fit.cvut.org.cz.bowling.presentation.dialogs.PointConfigurationsDialog;
import fit.cvut.org.cz.bowling.presentation.services.PointConfigurationService;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

/**
 * Fragment is used in TournamentConfigurationActivity to configure points settings for all the standings
 */
public class ConfigurePointsFragment extends AbstractListFragment<PointConfiguration> {
    private BroadcastReceiver pointConfigurationReceiver = new PointConfigurationReceiver();

    public static ConfigurePointsFragment newInstance(long tourId) {
        ConfigurePointsFragment fragment = new ConfigurePointsFragment();

        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, tourId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new PointConfigurationAdapter() {
            @Override
            protected void setOnClickListeners(View v, final long pointConfigurationId, final int position, final long sidesNumber) {
                super.setOnClickListeners(v, pointConfigurationId, position, sidesNumber);

                v.setOnClickListener( new View.OnClickListener(){
                                          @Override
                                          public void onClick(View v) {
                                              Intent i = ShowPointConfigurationActivity.newStartIntent(getContext(), pointConfigurationId);
                                              startActivity(i);
                                          }
                                      }
                );

                v.setOnLongClickListener(new View.OnLongClickListener() {
                                             @Override
                                             public boolean onLongClick(View v) {
                                                 String title = getResources().getString(R.string.prebaked_label_pc) + " " + sidesNumber;
                                                 PointConfigurationsDialog dialog = PointConfigurationsDialog.newInstance(pointConfigurationId, position, title);
                                                 dialog.show(getFragmentManager(), "tag3");
                                                 return true;
                                             }
                                         }
                );
            }
        };
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_CONFIGURATIONS;
    }

    @Override
    public void askForData() {
        Intent intent = PointConfigurationService.newStartIntent(PointConfigurationService.ACTION_GET_CONFIGS_BY_TOURNAMENT, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID, -1));

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return PointConfigurationService.isWorking(PointConfigurationService.ACTION_GET_CONFIGS_BY_TOURNAMENT);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(PointConfigurationService.ACTION_GET_CONFIGS_BY_TOURNAMENT);
        filter.addAction(PointConfigurationService.ACTION_DELETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(pointConfigurationReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(pointConfigurationReceiver);
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floatingbutton_add, parent, false);
        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       InsertPointConfigurationDialog dialog = InsertPointConfigurationDialog.newInstance(getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID), true, BowlingInsertPointConfigurationDialog.class);
                                       dialog.show(getFragmentManager(), "dialog");
                                   }
                               }
        );

        return fab;
    }

    private class PointConfigurationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            progressBar.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
            switch (action) {
                case PointConfigurationService.ACTION_GET_CONFIGS_BY_TOURNAMENT: {
                    ConfigurePointsFragment.super.bindDataOnView(intent);
                    break;
                }

                case PointConfigurationService.ACTION_DELETE: {
                    if(intent.getBooleanExtra(ExtraConstants.EXTRA_RESULT, false)) {
                        int position = intent.getIntExtra(ExtraConstants.EXTRA_POSITION, -1);
                        adapter.delete(position);
                        break;
                    } else {
                        View v = getView();
                        if (v != null) Snackbar.make(v, R.string.cant_delete_pc, Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        }
    }
}

