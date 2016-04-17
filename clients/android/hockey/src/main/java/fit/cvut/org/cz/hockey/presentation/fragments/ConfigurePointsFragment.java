package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Date;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.PointConfiguration;
import fit.cvut.org.cz.hockey.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Created by atgot_000 on 11. 4. 2016.
 */
public class ConfigurePointsFragment extends AbstractDataFragment {

    private static final String ARG_TOUR_ID = "tournament_id";
    private Long tournamentId;

    private EditText ntW, ntD, ntL, otW, otD, otL, soW, soL;
    private FloatingActionButton fab;

    public static ConfigurePointsFragment newInstance( long tourId )
    {
        ConfigurePointsFragment fragment = new ConfigurePointsFragment();

        Bundle args = new Bundle();
        args.putLong(ARG_TOUR_ID, tourId);

        fragment.setArguments(args);
        return fragment;
    }

    private boolean validate( View v )
    {
        if(     ntW.getText().toString().isEmpty() ||
                ntD.getText().toString().isEmpty() ||
                ntL.getText().toString().isEmpty() ||
                otW.getText().toString().isEmpty() ||
                otD.getText().toString().isEmpty() ||
                otL.getText().toString().isEmpty() ||
                soW.getText().toString().isEmpty() ||
                soL.getText().toString().isEmpty() )
        {
            Snackbar.make(v, R.string.set_all, Snackbar.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void saveConfig( PointConfiguration pointConfiguration )
    {
        Intent intent = TournamentService.newStartIntent( TournamentService.ACTION_SET_CONFIG, getContext() );
        intent.putExtra( TournamentService.EXTRA_CONFIGURATION, pointConfiguration );
        intent.putExtra( TournamentService.EXTRA_ID, getArguments().getLong(ARG_TOUR_ID) );

        getContext().startService( intent );
    }



    @Override
    protected void askForData() {
        Intent intent = TournamentService.newStartIntent( TournamentService.ACTION_GET_CONFIG_BY_ID, getContext() );
        intent.putExtra( TournamentService.EXTRA_ID, getArguments().getLong(ARG_TOUR_ID) );

        getContext().startService( intent );
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TournamentService.isWorking( TournamentService.ACTION_GET_CONFIG_BY_ID );
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        PointConfiguration pointConfiguration = intent.getParcelableExtra( TournamentService.EXTRA_CONFIGURATION );
        if( pointConfiguration != null )
        {
            ntW.setText( Long.toString( pointConfiguration.ntW ) );
            ntD.setText( Long.toString( pointConfiguration.ntD ) );
            ntL.setText( Long.toString( pointConfiguration.ntL ) );

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
        View v = inflater.inflate(R.layout.fragment_config_points, container, false );

        ntW = (EditText) v.findViewById( R.id.et_nt_w );
        ntD = (EditText) v.findViewById( R.id.et_nt_d );
        ntL = (EditText) v.findViewById( R.id.et_nt_l );

        otW = (EditText) v.findViewById( R.id.et_ot_w );
        otD = (EditText) v.findViewById( R.id.et_ot_d );
        otL = (EditText) v.findViewById( R.id.et_ot_l );

        soW = (EditText) v.findViewById( R.id.et_so_w );
        soL = (EditText) v.findViewById( R.id.et_so_l );

        fab = (FloatingActionButton) v.findViewById( R.id.fab_save );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDataSourceWorking() && validate(v)) {
                    Long nW = Long.valueOf(ntW.getText().toString());
                    Long nD = Long.valueOf(ntD.getText().toString());
                    Long nL = Long.valueOf(ntL.getText().toString());

                    Long oW = Long.valueOf(otW.getText().toString());
                    Long oD = Long.valueOf(otD.getText().toString());
                    Long oL = Long.valueOf(otL.getText().toString());

                    Long sW = Long.valueOf(soW.getText().toString());
                    Long sL = Long.valueOf(soL.getText().toString());

                    PointConfiguration pointConfiguration = new PointConfiguration(nW, nD, nL, oW, oD, oL, sW, sL);

                    saveConfig(pointConfiguration);

                    getActivity().finish();
                }
            }
        });


        return v;
    }
}
