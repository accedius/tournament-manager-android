package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

public class ConfigurePointsFragment extends AbstractDataFragment {
    private EditText ntW, ntD, ntL, otW, otD, otL, soW, soL;
    private FloatingActionButton fab;

    public static ConfigurePointsFragment newInstance(long tourId) {
        ConfigurePointsFragment fragment = new ConfigurePointsFragment();

        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, tourId);

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Validates whether all input texts have numbers
     * @return true or false
     */
    private boolean validate() {
        if (    ntW.getText().toString().isEmpty() ||
                ntD.getText().toString().isEmpty() ||
                ntL.getText().toString().isEmpty() ||
                otW.getText().toString().isEmpty() ||
                otD.getText().toString().isEmpty() ||
                otL.getText().toString().isEmpty() ||
                soW.getText().toString().isEmpty() ||
                soL.getText().toString().isEmpty()) {
            return false;
        }

        return true;
    }

    @Override
    public void askForData() {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_GET_CONFIG_BY_ID, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID));

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TournamentService.isWorking(TournamentService.ACTION_GET_CONFIG_BY_ID);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        PointConfiguration pointConfiguration = intent.getParcelableExtra(ExtraConstants.EXTRA_CONFIGURATION);
        if (pointConfiguration != null) {
            ntW.setText(Long.toString(pointConfiguration.ntW));
            ntD.setText(Long.toString(pointConfiguration.ntD));
            ntL.setText(Long.toString(pointConfiguration.ntL));

            otW.setText(Long.toString(pointConfiguration.otW));
            otD.setText(Long.toString(pointConfiguration.otD));
            otL.setText(Long.toString(pointConfiguration.otL));

            soW.setText(Long.toString(pointConfiguration.soW));
            soL.setText(Long.toString(pointConfiguration.soL));
        }
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(TournamentService.ACTION_GET_CONFIG_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_config_points, container, false);

        ntW = (EditText) v.findViewById(R.id.et_nt_w);
        ntD = (EditText) v.findViewById(R.id.et_nt_d);
        ntL = (EditText) v.findViewById(R.id.et_nt_l);

        otW = (EditText) v.findViewById(R.id.et_ot_w);
        otD = (EditText) v.findViewById(R.id.et_ot_d);
        otL = (EditText) v.findViewById(R.id.et_ot_l);

        soW = (EditText) v.findViewById(R.id.et_so_w);
        soL = (EditText) v.findViewById(R.id.et_so_l);

        return v;
    }

    public PointConfiguration getPointConfig() {
        if (!validate())
            return null;
        try {
            return new PointConfiguration(
                    getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID),
                    Integer.parseInt(ntW.getText().toString()),
                    Integer.parseInt(ntD.getText().toString()),
                    Integer.parseInt(ntL.getText().toString()),
                    Integer.parseInt(otW.getText().toString()),
                    Integer.parseInt(otD.getText().toString()),
                    Integer.parseInt(otL.getText().toString()),
                    Integer.parseInt(soW.getText().toString()),
                    Integer.parseInt(soL.getText().toString()));
        } catch(NumberFormatException ex) {
            return null;
        }
    }
}

