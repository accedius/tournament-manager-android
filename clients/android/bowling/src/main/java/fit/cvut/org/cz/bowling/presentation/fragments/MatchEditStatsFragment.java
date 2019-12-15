package fit.cvut.org.cz.bowling.presentation.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.List;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

public class MatchEditStatsFragment extends AbstractDataFragment {
    private Match match = null;
    private long matchId = -1;
    private Switch statsInputSwitch;
    private boolean switchChanged = false;
    private CheckBox partialDataPropagation;
    private BowlingAbstractMatchStatsListFragment inputFragment;
    private static final String inputFragmentTag = "inputFragmentTag";

    public Match getMatchResults() {
        List<Participant> matchParticipants = inputFragment.getMatchStats();
        match.setParticipants(matchParticipants);
        return match;
    }

    public boolean isSwitchChanged() {
        return switchChanged;
    }

    public static MatchEditStatsFragment newInstance (long matchId) {
        MatchEditStatsFragment fragment = new MatchEditStatsFragment();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_MATCH_ID, matchId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void askForData() {
        Intent intent = MatchService.newStartIntent(MatchService.ACTION_FIND_BY_ID, getContext());
        matchId = getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID);
        intent.putExtra(ExtraConstants.EXTRA_ID, matchId);
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return MatchService.isWorking(MatchService.ACTION_FIND_BY_ID);
    }

    protected void setContentFragment (boolean isChecked) {
        if(inputFragment != null)
            getChildFragmentManager().beginTransaction().remove(inputFragment).commit();
        if(isChecked) {
            inputFragment = FrameListFragment.newInstance(matchId);
        } else {
            inputFragment = ParticipantsOverviewFragment.newInstance(matchId);
        }
        getChildFragmentManager().beginTransaction().add(R.id.input_container, inputFragment, inputFragmentTag).commit();
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if(match == null) {
            match = intent.getParcelableExtra(ExtraConstants.EXTRA_MATCH);
            statsInputSwitch.setChecked(match.isTrackRolls());
            partialDataPropagation.setChecked(match.isValidForStats());
        }
        if (getChildFragmentManager().findFragmentById(R.id.input_container) == null) {
            setContentFragment(statsInputSwitch.isChecked());
        }
        statsInputSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            boolean abortToken;

            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                if(abortToken) {
                    abortToken = false;
                    return;
                }

                //Warning message
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle(R.string.edit_stats_fragment_switch_warning_title);
                alertDialog.setMessage(R.string.edit_stats_fragment_switch_warning_message);
                alertDialog.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switchChanged = !switchChanged;
                                match.setTrackRolls(isChecked);
                                setContentFragment(isChecked);
                            }
                        });

                alertDialog.setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        abortToken = true;
                        statsInputSwitch.setChecked(!isChecked);
                        dialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });
        partialDataPropagation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                match.setValidForStats(isChecked);
            }
        });
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(MatchService.ACTION_FIND_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_match_edit_stats, container, false);
        matchId = getArguments().getLong(ExtraConstants.EXTRA_ID, -1);
        statsInputSwitch = v.findViewById(R.id.individual_throw_switch);
        partialDataPropagation = v.findViewById(R.id.match_stats_update_with_partial_data);
        return v;
    }
}
