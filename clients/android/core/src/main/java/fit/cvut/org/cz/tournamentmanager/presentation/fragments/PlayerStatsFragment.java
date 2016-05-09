package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.business.AggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.PlayerAggregatedStats;
import fit.cvut.org.cz.tmlibrary.business.PlayerAggregatedStatsRecord;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;
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
    private String stats_service;

    private static String ARG_ID = "player_id";


    public static PlayerStatsFragment newInstance(long id, Class<? extends PlayerStatsFragment> clazz){
        PlayerStatsFragment fragment = null;
        try {
            Constructor<? extends PlayerStatsFragment> c = clazz.getConstructor();
            fragment = c.newInstance();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putString("package_name", "hockey");

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        playerID = getArguments().getLong(ARG_ID);
        package_name = getArguments().getString("package_name");
        stats_service = getArguments().getString("stats_service");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void askForData() {
        Intent intent = new Intent();
        intent.setClassName(package_name, stats_service);
        intent.putExtra(CrossPackageComunicationConstants.EXTRA_ACTION, CrossPackageComunicationConstants.ACTION_GET_STATS);
        intent.putExtra(CrossPackageComunicationConstants.EXTRA_PACKAGE, package_name);
        intent.putExtra(CrossPackageComunicationConstants.EXTRA_ID, playerID);
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return false;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        Log.d("ORIENTATION", "" + getResources().getConfiguration().orientation);
        Log.d("Landscape", ""+ getResources().getConfiguration().ORIENTATION_LANDSCAPE);
        Log.d("Portrait", ""+getResources().getConfiguration().ORIENTATION_PORTRAIT);

        int orientation = getResources().getConfiguration().orientation;
        int landscape = getResources().getConfiguration().ORIENTATION_LANDSCAPE;
        int portrait = getResources().getConfiguration().ORIENTATION_PORTRAIT;

        label_row.removeAllViews();
        stats_row.removeAllViews();

        AggregatedStats ags = intent.getParcelableExtra(CrossPackageComunicationConstants.EXTRA_STATS);
        for(PlayerAggregatedStats as : ags.getRecords()) {
            for (PlayerAggregatedStatsRecord asr : as.getRecords()) {
                if (orientation == landscape || (orientation == portrait && asr.getForPortrait())) {
                    TextView label = new TextView(getContext());
                    LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    if (asr.getKey().equals("Name")) {
                        params.weight = 2;
                    } else {
                        params.weight = 1;
                    }
                    params.gravity = Gravity.CENTER;
                    params.width = 0;
                    label.setLayoutParams(params);
                    label.setText(asr.getKey());
                    label_row.addView(label);

                    TextView stat = new TextView(getContext());
                    params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    if (asr.getKey().equals("Name")) {
                        params.gravity = Gravity.LEFT;
                        params.setMargins(10, 0, 0, 0);
                        params.weight = 2;
                    } else {
                        params.gravity = Gravity.CENTER;
                        params.weight = 1;
                    }
                    params.width = 0;
                    stat.setLayoutParams(params);
                    stat.setText(asr.getVal());
                    stats_row.addView(stat);
                }
            }
        }
    }

    @Override
    protected void registerReceivers() {
        getActivity().registerReceiver(receiver, new IntentFilter(package_name + CrossPackageComunicationConstants.ACTION_GET_STATS));
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
