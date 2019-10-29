package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.PointConfigurationService;

public class ShowPointConfigurationFragment extends PointConfigurationDetailFragment {
    @Override
    protected void updateConfigurations(PointConfiguration pc) {
        Intent intent = PointConfigurationService.newStartIntent(PointConfigurationService.ACTION_EDIT, getContext());
        intent.putExtra(ExtraConstants.EXTRA_CONFIGURATION, pc);
        getContext().startService(intent);
    }

    @Override
    public void askForData() {
        Intent intent = PointConfigurationService.newStartIntent(PointConfigurationService.ACTION_GET_BY_ID, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, configurationId);
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return PointConfigurationService.isWorking(PointConfigurationService.ACTION_GET_BY_ID);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(PointConfigurationService.ACTION_GET_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cancel, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        List<Float> oldPC = pointConfiguration.getConfigurationPlacePoints();
        if(item.getItemId() == R.id.action_cancel) {
            adapter.swapData(oldPC);
        } else {
            List<Float> resultPC = adapter.getData();
            if(!oldPC.equals(resultPC)) {
                pointConfiguration.setConfigurationPlacePoints(resultPC);
                updateConfigurations(pointConfiguration);
            }
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
