package fit.cvut.org.cz.bowling.presentation.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.List;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.TournamentManager;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

public class MatchEditStatsFragment extends AbstractDataFragment {
    private Match match = null;
    private int tournamentTypeId;
    private long matchId = -1;
    private Switch statsInputSwitch;
    private boolean switchChanged = false;
    private CheckBox partialDataPropagation;
    private BowlingAbstractMatchStatsListFragment inputFragment;
    private static final String inputFragmentTag = "inputFragmentTag";
    public static final int REQUEST_CODE_MANAGE_CHECKBOX_STATE = 3137;
    private boolean userInputOnCheckBox = false;

    public Bundle getResultsBundle() {
        if(inputFragment == null) {
            inputFragment = (BowlingAbstractMatchStatsListFragment) getChildFragmentManager().findFragmentByTag(inputFragmentTag);
        }
        Bundle bundle = inputFragment.getMatchStats();
        boolean isMatchPlayed = bundle.getBoolean(ExtraConstants.EXTRA_BOOLEAN_IS_MATCH_PLAYED, false);
        match.setPlayed(isMatchPlayed);
        bundle.remove(ExtraConstants.EXTRA_BOOLEAN_IS_MATCH_PLAYED);
        bundle.putBoolean(ExtraConstants.EXTRA_BOOLEAN_IS_INPUT_TYPE_CHANGED, switchChanged);
        return bundle;
    }

    public Match getMatchWithResults() {
        if(inputFragment == null) {
            inputFragment = (BowlingAbstractMatchStatsListFragment) getChildFragmentManager().findFragmentByTag(inputFragmentTag);
        }
        List<Participant> matchParticipants = inputFragment.getMatchParticipants();
        match.setParticipants(matchParticipants);
        match.setTrackRolls(statsInputSwitch.isChecked());
        match.setValidForStats(partialDataPropagation.isChecked());
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_match_stats, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
            switch(tournamentTypeId) {
                case TournamentTypes.type_individuals: {
                    //inputFragment = ParticipantsOverviewFragment.newInstance(matchId);
                    inputFragment = SimpleStatsFragment.newInstance(matchId);
                    break;
                }
                case TournamentTypes.type_teams: {
                    inputFragment = SimpleStatsFragment.newInstance(matchId);
                    break;
                }
                default: {
                    inputFragment = SimpleStatsFragment.newInstance(matchId);
                }
            }
        }
        getChildFragmentManager().beginTransaction().add(R.id.input_container, inputFragment, inputFragmentTag).commit();
        inputFragment.setTargetFragment(null, REQUEST_CODE_MANAGE_CHECKBOX_STATE);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if(match == null) {
            match = intent.getParcelableExtra(ExtraConstants.EXTRA_MATCH);
            statsInputSwitch.setChecked(match.isTrackRolls());
            partialDataPropagation.setChecked(match.isValidForStats());
            long tournamentId = match.getTournamentId();
            TournamentManager tournamentManager = ManagerFactory.getInstance(getContext()).getEntityManager(Tournament.class);
            Tournament tournament = tournamentManager.getById(tournamentId);
            tournamentTypeId = tournament.getTypeId();
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
                                //match.setTrackRolls(isChecked);
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
                userInputOnCheckBox = true;
                //match.setValidForStats(isChecked);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_MANAGE_CHECKBOX_STATE:
                //if true, that match is played by all participants
                if(userInputOnCheckBox)
                    break;
                boolean checkBoxShouldBeState = resultCode == 1;
                if(checkBoxShouldBeState) {
                    //checks only then wasn't checked before + if user didn't use manual input in last save, how works -> negation of (do nothing, then previously user set notValid for played match, because it seems user done this for purpose)
                    if(!partialDataPropagation.isChecked() && !(!match.isValidForStats() && match.isPlayed()) )
                        partialDataPropagation.setChecked(true);
                } else {
                    //same thing for opposite case
                    if(partialDataPropagation.isChecked() && !(match.isValidForStats() && !match.isPlayed()) )
                        partialDataPropagation.setChecked(false);
                }
                break;
        }
    }
}
