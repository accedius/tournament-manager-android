package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.adapters.SimpleStatAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.constraints.ConstraintsConstants;
import fit.cvut.org.cz.bowling.presentation.dialogs.EditPlayerStatDialog;
import fit.cvut.org.cz.bowling.presentation.services.ParticipantService;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class TeamSimpleStatsFragment extends BowlingAbstractMatchStatsListFragment<PlayerStat> {
    private BroadcastReceiver participantReceiver = new ParticipantReceiver();
    protected List<Participant> matchParticipants;
    protected long matchId;
    private Spinner participantSpinner;
    private Fragment thisFragment;

    public static final class RecyclerViewUpdateCodes {
        public static final int DIALOG = 0;
        public static final int SPINNER_ITEM_SELECTED = 1;
    }

    @Override
    public Bundle getMatchStats() {
        //whether or not the game is completed by all participants
        boolean isMatchPlayed = false;
        byte participantsWhoCompletedGameNumber = 0;

        for(Participant participant : matchParticipants) {
            //TODO make more sophisticated method (toAdd, toDelete, toEdit scheme)
            ParticipantStat participantStat = (ParticipantStat) participant.getParticipantStats().get(0);
            if(participant.getPlayerStats() != null && participantStat.getFramesPlayedNumber() == ConstraintsConstants.tenPinMatchParticipantMaxFrames * participant.getPlayerStats().size())
                ++participantsWhoCompletedGameNumber;
        }
        if(participantsWhoCompletedGameNumber == matchParticipants.size())
            isMatchPlayed = true;

        Bundle matchStatsBundle = new Bundle();
        matchStatsBundle.putBoolean(EXTRA_BOOLEAN_IS_MATCH_PLAYED, isMatchPlayed);
        matchStatsBundle.putParcelableArrayList(ExtraConstants.EXTRA_PARTICIPANTS, (ArrayList<? extends Parcelable>) matchParticipants);
        return matchStatsBundle;
    }

    @Override
    public List<Participant> getMatchParticipants() {
        return matchParticipants;
    }

    public static TeamSimpleStatsFragment newInstance(long matchId) {
        TeamSimpleStatsFragment fragment = new TeamSimpleStatsFragment();

        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_MATCH_ID, matchId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        matchId = getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID);
        matchParticipants = null;
        if(savedInstanceState != null) {
            matchParticipants = savedInstanceState.getParcelableArrayList(ExtraConstants.EXTRA_PARTICIPANTS);
        }
        thisFragment = this;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ExtraConstants.EXTRA_PARTICIPANTS, (ArrayList<? extends Parcelable>) matchParticipants);
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View fragmentView = inflater.inflate(R.layout.fragment_match_simple_stats, container, false);

        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.simple_stats_recycler_view);
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        int orientation = getActivity().getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        } else {
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        }
        recyclerView.setLayoutManager(linearLayoutManager);

        participantSpinner = fragmentView.findViewById(R.id.participant_spinner);

        return fragmentView;
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new SimpleStatAdapter() {
            @Override
            protected void setOnClickListeners(View v, final PlayerStat stat, final int position) {
                super.setOnClickListeners(v, stat, position);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PlayerStat statToEdit = new PlayerStat(stat);
                        Participant participant = (Participant) participantSpinner.getSelectedItem();
                        ParticipantStat participantStat = (ParticipantStat) participant.getParticipantStats().get(0);
                        EditPlayerStatDialog dialog = EditPlayerStatDialog.newInstance(statToEdit, (byte) (ConstraintsConstants.tenPinMatchParticipantMaxFrames - (participantStat.getFramesPlayedNumber() - stat.getFramesPlayedNumber()) ), position);
                        dialog.setTargetFragment(thisFragment, RecyclerViewUpdateCodes.DIALOG);
                        dialog.show(getFragmentManager(), "editPlayerStatDialog");
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //TODO if needed
                        return false;
                    }
                });
            }
        };
    }

    @Override
    public void askForData() {
        Intent intent = ParticipantService.newStartIntent(ParticipantService.ACTION_GET_BY_MATCH_ID, getContext());
        Long matchId = getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID, -1);
        intent.putExtra(ExtraConstants.EXTRA_MATCH_ID, matchId);
        getContext().startService(intent);
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_PLAYER_STATS;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if(matchParticipants == null) {
            matchParticipants = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PARTICIPANTS);
            for(Participant participant : matchParticipants) {
                if(participant.getParticipantStats() == null || participant.getParticipantStats().size() < 1) {
                    List<ParticipantStat> participantStats = new ArrayList<>();
                    ParticipantStat participantStat = new ParticipantStat(participant.getId(), 0, (byte) 0);
                    participantStats.add(participantStat);
                    participant.setParticipantStats(participantStats);
                }
            }
            //TODO map of actions
            bindParticipantsOnSpinner();
            return;
        }

        ArrayList<PlayerStat> playerStatsToShow = (ArrayList<PlayerStat>) ((Participant) participantSpinner.getSelectedItem()).getPlayerStats();
        intent.putParcelableArrayListExtra(getDataKey(), (ArrayList<? extends Parcelable>) playerStatsToShow);
        super.bindDataOnView(intent);
        switchRecyclerViewsProgressBar();
    }

    private void bindParticipantsOnSpinner() {
        ArrayAdapter<Participant> participantSpinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, matchParticipants);
        participantSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        participantSpinner.setAdapter(participantSpinnerAdapter);
        participantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*ArrayList<PlayerStat> playerStatsToShow = (ArrayList<PlayerStat>) ((Participant) parent.getSelectedItem()).getPlayerStats();
                intent.putParcelableArrayListExtra(getDataKey(), playerStatsToShow);*/
                switchRecyclerViewsProgressBar();
                bindDataOnView(new Intent());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //empty
            }
        });
    }

    private void setIsMatchPlayedInParentFragment(){
        //Check if match is played to set MatchEditStatsFragment's CheckBox validForStats checked to true or false
        int participantsWhoNotCompletedGameNumber = matchParticipants.size();
        for(Participant participant : matchParticipants){
            ParticipantStat overallStat = (ParticipantStat) participant.getParticipantStats().get(0);
            if(overallStat.getFramesPlayedNumber() == ConstraintsConstants.tenPinMatchParticipantMaxFrames)
                --participantsWhoNotCompletedGameNumber;
        }
        if(getParentFragment() != null) {
            if(participantsWhoNotCompletedGameNumber == 0 ) {
                getParentFragment().onActivityResult(getTargetRequestCode(), 1, null);
            } else {
                getParentFragment().onActivityResult(getTargetRequestCode(), 0, null);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RecyclerViewUpdateCodes.DIALOG: {
                boolean scoreChanged = data.getBooleanExtra(ExtraConstants.EXTRA_BOOLEAN_IS_SCORE_CHANGED, false);
                boolean framesChanged = data.getBooleanExtra(ExtraConstants.EXTRA_BOOLEAN_IS_FRAMES_CHANGED, false);
                PlayerStat editedPlayerStat = data.getParcelableExtra(ExtraConstants.EXTRA_PLAYER_STAT);
                int position = data.getIntExtra(ExtraConstants.EXTRA_POSITION, -1);

                Participant teamToEditStats = (Participant) participantSpinner.getSelectedItem();
                ParticipantStat participantStat = (ParticipantStat) teamToEditStats.getParticipantStats().get(0);
                int framesPlayed = participantStat.getFramesPlayedNumber();
                int score = participantStat.getScore();
                PlayerStat previousPlayerStat = ( (List<PlayerStat>) teamToEditStats.getPlayerStats() ).get(position);
                framesPlayed -= previousPlayerStat.getFramesPlayedNumber();
                score -= previousPlayerStat.getPoints();
                framesPlayed += editedPlayerStat.getFramesPlayedNumber();
                score += editedPlayerStat.getPoints();
                participantStat.setFramesPlayedNumber( (byte) framesPlayed);
                participantStat.setScore(score);

                ( (List<PlayerStat>) teamToEditStats.getPlayerStats() ).set(position, editedPlayerStat);

                //TODO Action map change for that player stat

                //switchRecyclerViewsProgressBar();

                ( (SimpleStatAdapter) recyclerView.getAdapter() ).setItemAtPosition(position, new PlayerStat(editedPlayerStat) );
                recyclerView.getAdapter().notifyItemChanged(position);

                if(framesChanged) {
                    setIsMatchPlayedInParentFragment();
                }

                break;
            }
        }
    }

    /**
     * Switches between states: progress bar is only visible / content view with data is only visible
     */
    private void switchRecyclerViewsProgressBar(){
        switch (progressBar.getVisibility()) {
            case View.VISIBLE: {
                progressBar.setVisibility(View.GONE);
                contentView.setVisibility(View.VISIBLE);
                break;
            }
            case View.GONE:
            case View.INVISIBLE:
            default: {
                progressBar.setVisibility(View.VISIBLE);
                contentView.setVisibility(View.GONE);
                break;
            }
        }
    }

    @Override
    protected boolean isDataSourceWorking() {
        return ParticipantService.isWorking(ParticipantService.ACTION_GET_BY_MATCH_ID);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(ParticipantService.ACTION_GET_BY_MATCH_ID);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(participantReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(participantReceiver);
    }

    public class ParticipantReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switchRecyclerViewsProgressBar();
            switch (action) {
                case ParticipantService.ACTION_GET_BY_MATCH_ID: {
                    bindDataOnView(intent);
                    break;
                }
            }
        }
    }
}
