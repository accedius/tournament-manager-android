package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.business.entities.PlayerAggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.entities.PlayerAggregatedStatsRecord;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageCommunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by kevin on 2. 5. 2016.
 */
public class PlayerStatsFragment extends AbstractDataFragment {
    private LinearLayout label_row;
    private LinearLayout stats_row;
    private View v;

    private long playerID;

    private String package_name;
    private String sport_context;
    private String package_service;

    private static String ARG_ID = "player_id";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        playerID = getArguments().getLong(ARG_ID);
        package_name = getArguments().getString("package_name");
        sport_context = getArguments().getString(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT);
        package_service = getArguments().getString("package_service");

        super.onCreate(savedInstanceState);
    }

    @Override
    public void askForData() {
        Intent intent = new Intent();
        intent.setClassName(package_name, package_service);
        intent.putExtra(CrossPackageCommunicationConstants.EXTRA_ACTION, CrossPackageCommunicationConstants.ACTION_GET_STATS);
        intent.putExtra(CrossPackageCommunicationConstants.EXTRA_PACKAGE, package_name);
        intent.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sport_context);
        intent.putExtra(CrossPackageCommunicationConstants.EXTRA_ID, playerID);
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return false;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        int orientation = getResources().getConfiguration().orientation;
        int landscape = getResources().getConfiguration().ORIENTATION_LANDSCAPE;
        int portrait = getResources().getConfiguration().ORIENTATION_PORTRAIT;

        label_row.removeAllViews();
        stats_row.removeAllViews();

        LayoutParams labelParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutParams statsParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        labelParams.weight = 1;
        statsParams.weight = 1;

        ArrayList<PlayerAggregatedStats> ags = intent.getParcelableArrayListExtra(CrossPackageCommunicationConstants.EXTRA_STATS);
        for (PlayerAggregatedStats as : ags) {
            for (PlayerAggregatedStatsRecord asr : as.getRecords()) {
                if (orientation == landscape || (orientation == portrait && asr.getForPortrait())) {
                    TextView label = new TextView(getContext());
                    label.setLayoutParams(labelParams);
                    label.setGravity(Gravity.CENTER);
                    label.setText(asr.getKey());
                    label_row.addView(label);

                    TextView stat = new TextView(getContext());
                    stat.setLayoutParams(statsParams);
                    stat.setGravity(Gravity.CENTER);
                    stat.setText(asr.getVal());
                    stats_row.addView(stat);
                }
            }
        }
    }

    @Override
    protected void registerReceivers() {
        getActivity().registerReceiver(receiver, new IntentFilter(sport_context + package_name + CrossPackageCommunicationConstants.ACTION_GET_STATS));
    }

    @Override
    protected void unregisterReceivers() {
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        v = inflater.inflate(R.layout.fragment_player_stats, container, false);
        label_row = (LinearLayout) v.findViewById(R.id.player_stats_label_row);
        stats_row = (LinearLayout) v.findViewById(R.id.player_stats_row);
        return v;
    }
}
