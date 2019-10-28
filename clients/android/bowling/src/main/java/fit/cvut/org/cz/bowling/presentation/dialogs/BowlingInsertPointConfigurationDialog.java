package fit.cvut.org.cz.bowling.presentation.dialogs;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.PointConfigurationService;

public class BowlingInsertPointConfigurationDialog extends InsertPointConfigurationDialog {
    @Override
    protected void askForData() {
        Intent i = PointConfigurationService.newStartIntent(PointConfigurationService.ACTION_GET_BY_ID, getContext());
        i.putExtra(ExtraConstants.EXTRA_ID, pointConfigurationId);
        getContext().startService(i);
    }

    @Override
    protected void registerReceiver() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(PointConfigurationService.ACTION_GET_BY_ID));
    }

    @Override
    protected void unregisterReceiver() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected void insertPointConfiguration(PointConfiguration pc) {
        Intent i = PointConfigurationService.newStartIntent(PointConfigurationService.ACTION_INSERT, getContext());
        i.putExtra(ExtraConstants.EXTRA_CONFIGURATION, pc);
        getContext().startService(i);
    }

    @Override
    protected void editPointConfiguration(PointConfiguration pc) {
        Intent i = PointConfigurationService.newStartIntent(PointConfigurationService.ACTION_EDIT, getContext());
        i.putExtra(ExtraConstants.EXTRA_CONFIGURATION, pc);
        getContext().startService(i);
    }

    @Override
    protected String getPointConfigurationKey() {
        return ExtraConstants.EXTRA_CONFIGURATION;
    }

    @Override
    protected boolean isDataSourceWorking() {
        return PointConfigurationService.isWorking(PointConfigurationService.ACTION_GET_BY_ID);
    }
}
