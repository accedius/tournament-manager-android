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
import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.communication.ExtraConstants;

/**
 * Fragment for display Player statistics.
 */
public class PlayerStatsFragment extends AbstractDataFragment {
    private LinearLayout labelRow;
    private LinearLayout statsRow;
    private View v;

    private long playerID;

    private String packageName;
    private String sportContext;
    private String packageService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        playerID = getArguments().getLong(ExtraConstants.EXTRA_ID);
        packageName = getArguments().getString(CrossPackageConstants.PACKAGE_NAME);
        sportContext = getArguments().getString(CrossPackageConstants.EXTRA_SPORT_CONTEXT);
        packageService = getArguments().getString(CrossPackageConstants.PACKAGE_SERVICE);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void askForData() {
        Intent intent = new Intent();
        intent.setClassName(packageName, packageService);
        intent.putExtra(CrossPackageConstants.EXTRA_ACTION, CrossPackageConstants.ACTION_GET_STATS);
        intent.putExtra(CrossPackageConstants.EXTRA_PACKAGE, packageName);
        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
        intent.putExtra(CrossPackageConstants.EXTRA_ID, playerID);
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

        labelRow.removeAllViews();
        statsRow.removeAllViews();

        LayoutParams labelParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutParams statsParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        labelParams.weight = 1;
        statsParams.weight = 1;

        ArrayList<PlayerAggregatedStats> ags = intent.getParcelableArrayListExtra(CrossPackageConstants.EXTRA_STATS);
        for (PlayerAggregatedStats as : ags) {
            for (PlayerAggregatedStatsRecord asr : as.getRecords()) {
                if (orientation == landscape || (orientation == portrait && asr.getForPortrait())) {
                    TextView label = new TextView(getContext());
                    label.setLayoutParams(labelParams);
                    label.setGravity(Gravity.CENTER);
                    label.setText(asr.getKey());
                    labelRow.addView(label);

                    TextView stat = new TextView(getContext());
                    stat.setLayoutParams(statsParams);
                    stat.setGravity(Gravity.CENTER);
                    stat.setText(asr.getVal());
                    statsRow.addView(stat);
                }
            }
        }
    }

    @Override
    protected void registerReceivers() {
        getActivity().registerReceiver(receiver, new IntentFilter(sportContext + packageName + CrossPackageConstants.ACTION_GET_STATS));
    }

    @Override
    protected void unregisterReceivers() {
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        v = inflater.inflate(R.layout.fragment_player_stats, container, false);
        labelRow = (LinearLayout) v.findViewById(R.id.player_stats_label_row);
        statsRow = (LinearLayout) v.findViewById(R.id.player_stats_row);
        return v;
    }
}
