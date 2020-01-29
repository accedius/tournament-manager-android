package fit.cvut.org.cz.bowling.presentation.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.entities.FrameOverview;
import fit.cvut.org.cz.bowling.data.entities.Frame;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.data.entities.Roll;
import fit.cvut.org.cz.bowling.presentation.adapters.FrameOverviewAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.constraints.ConstraintsConstants;
import fit.cvut.org.cz.bowling.presentation.dialogs.EditDeleteFrameDialog;
import fit.cvut.org.cz.bowling.presentation.dialogs.EditFrameDialog;
import fit.cvut.org.cz.bowling.presentation.services.ParticipantService;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class TeamComplexStatsFragment extends BowlingAbstractMatchStatsListFragment<FrameOverview> {
    private BroadcastReceiver participantReceiver = new ParticipantReceiver();
    private Spinner participantSpinner;
    protected static List<Participant> matchParticipants;
    protected static List<ParticipantPlayer> matchParticipantPlayers;
    protected long matchId;
    protected TournamentType tournamentType;
    private Fragment thisFragment;
    int maxFrameScore = ConstraintsConstants.tenPinFrameMaxScore;

    private class ParticipantPlayer {
        String name;
        List<FrameOverview> frameOverviews;
        PlayerStat playerStat;
        int matchParticipantReferencePosition;
        long participantId;

        public ParticipantPlayer() {
        }

        public ParticipantPlayer(String name, List<FrameOverview> frameOverviews, PlayerStat playerStat, int matchParticipantReferencePosition, long participantId) {
            this.name = name;
            this.frameOverviews = frameOverviews;
            this.playerStat = playerStat;
            this.matchParticipantReferencePosition = matchParticipantReferencePosition;
            this.participantId = participantId;
        }
    }

    public static final class RequestCodes {
        public static final int ADD_FRAME = 0;
        public static final int REMOVE_FRAME = 1;
        public static final int EDIT_FRAME = 2;
    }

    @Override
    public Bundle getMatchStats() {
        //TODO make more sophisticated method (toAdd, toDelete, toEdit scheme)
        for(Participant participant : matchParticipants) {
            ParticipantStat participantStat = (ParticipantStat) participant.getParticipantStats().get(0);
            participantStat.setFrames(new ArrayList<Frame>());
        }

        for(ParticipantPlayer participantPlayer : matchParticipantPlayers) {
            List<FrameOverview> frameOverviews = participantPlayer.frameOverviews;
            Participant participant = matchParticipants.get(participantPlayer.matchParticipantReferencePosition);
            List<Frame> newFrames = new ArrayList<>();
            byte i = 1;
            for(FrameOverview overview : frameOverviews) {
                Frame newFrame = new Frame(matchId, participant.getId(), i, overview.getPlayerId());
                List<Byte> rolls = overview.getRolls();
                byte j = 1;
                List<Roll> newRolls = new ArrayList<>();
                for(Byte roll : rolls) {
                    Roll newRoll = new Roll(newFrame.getId(), j, overview.getPlayerId(), roll);
                    newRolls.add(newRoll);
                    ++j;
                }
                newFrame.setRolls(newRolls);
                newFrames.add(newFrame);
                ++i;
            }
            ParticipantStat participantStat = (ParticipantStat) participant.getParticipantStats().get(0);
            participantStat.getFrames().addAll(newFrames);
        }

        Bundle matchStatsBundle = new Bundle();
        matchStatsBundle.putParcelableArrayList(ExtraConstants.EXTRA_PARTICIPANTS, (ArrayList<? extends Parcelable>) matchParticipants);
        return matchStatsBundle;
    }

    @Override
    public List<Participant> getMatchParticipants() {
        return matchParticipants;
    }

    public static TeamComplexStatsFragment newInstance(long matchId) {
        TeamComplexStatsFragment fragment = new TeamComplexStatsFragment();

        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_MATCH_ID, matchId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        thisFragment = this;
        matchParticipantPlayers = null;
        matchParticipants = null;

        matchId = getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID, -1);
        IManagerFactory iManagerFactory = ManagerFactory.getInstance();
        Match match = iManagerFactory.getEntityManager(Match.class).getById(matchId);
        long tournamentId = match.getTournamentId();
        Tournament tournament = iManagerFactory.getEntityManager(Tournament.class).getById(tournamentId);
        tournamentType = TournamentTypes.getMyTournamentType(tournament.getTypeId());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View fragmentView = inflater.inflate(R.layout.fragment_match_complex_stats, container, false);

        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.complex_stats_recycler_view);
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

        final FloatingActionButton fab = getFAB((ViewGroup) fragmentView);
        if (fab != null) {
            this.fab = fab;
            ((ViewGroup) fragmentView).addView(fab);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE)
                        fab.show();

                    else fab.hide();
                }
            });
        }

        return fragmentView;
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        Context context = getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        FloatingActionButton fab = (FloatingActionButton) layoutInflater.inflate(R.layout.floatingbutton_add, parent, false);

        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       ParticipantPlayer participantPlayer = (ParticipantPlayer) participantSpinner.getSelectedItem();
                                       int participantIndex = participantPlayer.matchParticipantReferencePosition;
                                       long participantId = matchParticipants.get(participantIndex).getId();
                                       int position = participantPlayer.frameOverviews.size();
                                       EditFrameDialog dialog = EditFrameDialog.newInstance(true, participantId, participantPlayer.playerStat.getPlayerId(), participantPlayer.playerStat.getName(), position);
                                       dialog.setTargetFragment(thisFragment, RequestCodes.ADD_FRAME);
                                       dialog.show(getFragmentManager(), "dialogCreate");
                                   }
                               }
        );

        return fab;
    }

    @Override
    public void askForData() {
        Intent intent = ParticipantService.newStartIntent(ParticipantService.ACTION_GET_BY_MATCH_ID_WITH_ALL_CONTENTS, getContext());
        Long matchId = getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID, -1);
        intent.putExtra(ExtraConstants.EXTRA_MATCH_ID, matchId);
        getContext().startService(intent);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new FrameOverviewAdapter(tournamentType) {
            @Override
            protected void setOnClickListeners(View v, final int position, final long playerId, final byte frameNumber, List<Byte> rolls, String playerName, int currentScore) {
                super.setOnClickListeners(v, position, playerId, frameNumber, rolls, playerName, currentScore);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FrameOverview frameOverviewToEdit = new FrameOverview( data.get(position) );
                        EditFrameDialog dialog = EditFrameDialog.newInstance(frameOverviewToEdit, position);
                        dialog.setTargetFragment(thisFragment, RequestCodes.EDIT_FRAME);
                        dialog.show(getFragmentManager(), "dialogEdit");
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ParticipantPlayer participantPlayer = (ParticipantPlayer) participantSpinner.getSelectedItem();
                        int participantIndex = participantPlayer.matchParticipantReferencePosition;
                        long participantId = matchParticipants.get(participantIndex).getId();
                        FrameOverview frameOverviewToEdit = new FrameOverview( data.get(position) );
                        boolean isLast = position == (data.size()-1);
                        EditDeleteFrameDialog dialog = EditDeleteFrameDialog.newInstance(frameOverviewToEdit, position, isLast, participantId);
                        dialog.setTargetFragment(thisFragment, RequestCodes.EDIT_FRAME);
                        dialog.show(getFragmentManager(), "dialogEditDelete");
                        return true;
                    }
                });
            }
        };
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if(matchParticipants == null) {
            matchParticipants = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PARTICIPANTS);
            orderParticipantsAndTheirPlayers(matchParticipants);
            bindParticipantsOnSpinner();
            return;
        }

        ParticipantPlayer participantPlayer = (ParticipantPlayer) participantSpinner.getSelectedItem();
        List<FrameOverview> frameOverviews = participantPlayer.frameOverviews;
        intent.putParcelableArrayListExtra(getDataKey(), (ArrayList<? extends Parcelable>) frameOverviews);
        super.bindDataOnView(intent);
    }

    private List<Participant> orderParticipantsAndTheirPlayers(List<Participant> listToOrder) {
        ParticipantComparatorByName participantComparatorByName = new ParticipantComparatorByName();
        Collections.sort(listToOrder, participantComparatorByName);
        for(Participant participant : listToOrder) {
            PlayerStatComparatorByName playerStatComparatorByName = new PlayerStatComparatorByName();
            List<PlayerStat> playerStats = (List<PlayerStat>) participant.getPlayerStats();
            Collections.sort( playerStats, playerStatComparatorByName);
            participant.setPlayerStats(playerStats);
        }
        return listToOrder;
    }

    private class ParticipantComparatorByName implements Comparator<Participant> {
        @Override
        public int compare(Participant o1, Participant o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    private class PlayerStatComparatorByName implements Comparator<PlayerStat> {
        @Override
        public int compare(PlayerStat o1, PlayerStat o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    private List<ParticipantPlayer> createMatchParticipantPlayers() {
        if(matchParticipants == null)
            return new ArrayList<>();

        List<ParticipantPlayer> participantPlayers = new ArrayList<>();
        for(int i = 0; i < matchParticipants.size(); ++i) {
            Participant participant = matchParticipants.get(i);
            if(participant.getPlayerStats() != null) {
                for(int j = 0; j < participant.getPlayerStats().size(); ++j) {
                    PlayerStat playerStat = (PlayerStat) participant.getPlayerStats().get(j);
                    List<FrameOverview> playerFrameOverviews = setPlayerFrames(playerStat, participant);
                    String name = participant.getName() + " - " + playerStat.getName();
                    ParticipantPlayer participantPlayer = new ParticipantPlayer(name, playerFrameOverviews, playerStat, i, participant.getParticipantId());
                    participantPlayers.add(participantPlayer);
                }
            }
        }

        return participantPlayers;
    }

    private List<FrameOverview> setPlayerFrames(PlayerStat playerStat, Participant participant) {
        ParticipantStat participantStat = (ParticipantStat) participant.getParticipantStats().get(0);
        List<Frame> participantFrames = participantStat.getFrames();
        List<Frame> playerFrames = new ArrayList<>();
        List<FrameOverview> playerFrameOverviews = new ArrayList<>();
        String playerName = playerStat.getName();
        long playerId = playerStat.getPlayerId();
        byte i = 0;
        for(Frame frame : participantFrames) {
            if(frame.getPlayerId() == playerStat.getPlayerId()) {
                playerFrames.add(frame);

                byte frameScore = 0;
                List<Byte> rolls = new ArrayList<>();
                List<Roll> playedRolls = frame.getRolls();

                for(Roll roll : playedRolls) {
                    byte rollPoints = roll.getPoints();
                    frameScore += rollPoints;
                    rolls.add(rollPoints);
                }

                FrameOverview frameOverview = new FrameOverview(i, rolls, playerName, 0, playerId, participant.getId() , frameScore);
                playerFrameOverviews.add(frameOverview);

                ++i;
            }
        }

        updateScores(playerFrameOverviews, 0);
        playerStat.setFrames(playerFrames);

        return playerFrameOverviews;
    }

    private int getNextRollScore(List<FrameOverview> frameOverviews, final int positionFrom, final int rollsNumber) {
        int rollsRemaining = rollsNumber;
        int arraySize = frameOverviews.size();
        int readPosition = positionFrom + 1;
        int score = 0;
        while (rollsRemaining > 0 && readPosition < arraySize) {
            List<Byte> frameRolls = frameOverviews.get(readPosition).getRolls();
            score += frameRolls.get(0);
            if(frameRolls.size() >= rollsRemaining) {
                if(rollsRemaining == 2) {
                    score += frameRolls.get(1);
                }
                rollsRemaining = 0;
            } else {
                --rollsRemaining;
                ++readPosition;
            }
        }
        return score;
    }

    private void updateScores(List<FrameOverview> frameOverviews, final int positionFrom) {
        int arraySize = frameOverviews.size();
        for(int i = positionFrom; i < arraySize; ++i) {
            FrameOverview f = frameOverviews.get(i);
            int currentFrameScore = i == 0 ? 0 : frameOverviews.get(i-1).getCurrentScore();
            currentFrameScore += f.getFrameScore();
            if(f.getFrameScore() == maxFrameScore) {
                if(f.getRolls().get(0) == maxFrameScore){
                    currentFrameScore += getNextRollScore(frameOverviews, i, 2);
                } else {
                    currentFrameScore += getNextRollScore(frameOverviews, i, 1);
                }
            }
            f.setCurrentScore(currentFrameScore);
        }
    }

    private void bindParticipantsOnSpinner() {
        matchParticipantPlayers = createMatchParticipantPlayers();
        ParticipantPlayerAdapter participantSpinnerAdapter = new ParticipantPlayerAdapter(getContext(), android.R.layout.simple_spinner_item, matchParticipantPlayers);
        participantSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        participantSpinner.setAdapter(participantSpinnerAdapter);
        participantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bindDataOnView(new Intent());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //empty
            }
        });
    }

    class ParticipantPlayerAdapter extends ArrayAdapter<ParticipantPlayer> {
        List<ParticipantPlayer> participantPlayers;

        public ParticipantPlayerAdapter(@NonNull Context context, int resource, @NonNull List<ParticipantPlayer> objects) {
            super(context, resource, objects);
            participantPlayers = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView label = (TextView) super.getView(position, convertView, parent);
            //label.setTextColor(Color.BLACK);
            String title = participantPlayers.get(position).name;
            label.setText(title);
            return label;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView label = (TextView) super.getView(position, convertView, parent);
            //label.setTextColor(Color.BLACK);
            String title = participantPlayers.get(position).name;
            label.setText(title);
            return label;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
            return;

        ParticipantPlayer participantPlayer = (ParticipantPlayer) participantSpinner.getSelectedItem();
        Participant participant = matchParticipants.get(participantPlayer.matchParticipantReferencePosition);
        ParticipantStat participantStat = (ParticipantStat) participant.getParticipantStats().get(0);

        switch (requestCode) {
            case RequestCodes.ADD_FRAME: {
                FrameOverview newFrame = data.getParcelableExtra(ExtraConstants.EXTRA_FRAME_OVERVIEW);
                List<FrameOverview> frameOverviews = participantPlayer.frameOverviews;
                int lastPositionOld = frameOverviews.size() - 1;
                int oldScore = 0;
                if(lastPositionOld >= 0) {
                    oldScore = frameOverviews.get(lastPositionOld).getCurrentScore();
                }
                frameOverviews.add(newFrame);
                //participantPlayer.frameOverviews = frameOverviews;
                int positionToUpdateFrom = data.getIntExtra(ExtraConstants.EXTRA_POSITION_UPDATE_FROM, 0);
                updateScores(frameOverviews, positionToUpdateFrom);
                byte framesPlayedNumber = participantStat.getFramesPlayedNumber();
                int newScore = frameOverviews.get(lastPositionOld + 1).getCurrentScore();
                ++framesPlayedNumber;

                if(newScore != oldScore) {
                    int score = participantStat.getScore();
                    score += newScore - oldScore;
                    participantStat.setScore(score);
                }

                participantStat.setFramesPlayedNumber(framesPlayedNumber);

                bindDataOnView(new Intent());
                break;
            }
            case RequestCodes.EDIT_FRAME: {
                int position = data.getIntExtra(ExtraConstants.EXTRA_POSITION, -1);
                FrameOverview editedFrame = data.getParcelableExtra(ExtraConstants.EXTRA_FRAME_OVERVIEW);
                List<FrameOverview> frameOverviews = participantPlayer.frameOverviews;
                int positionToUpdateFrom = data.getIntExtra(ExtraConstants.EXTRA_POSITION_UPDATE_FROM, 0);
                int lastPositionOld = frameOverviews.size() - 1;
                int oldScore = frameOverviews.get(lastPositionOld).getCurrentScore();
                frameOverviews.set(position, editedFrame);
                updateScores(frameOverviews, positionToUpdateFrom);
                int newScore = frameOverviews.get(lastPositionOld).getCurrentScore();

                if(newScore != oldScore) {
                    int score = participantStat.getScore();
                    score += newScore - oldScore;
                    participantStat.setScore(score);
                }

                ((FrameOverviewAdapter) adapter).setItem(position, new FrameOverview(editedFrame) );
                adapter.notifyItemChanged(position);
                break;
            }
            case RequestCodes.REMOVE_FRAME: {
                int position = data.getIntExtra(ExtraConstants.EXTRA_POSITION, -1);
                participantPlayer.frameOverviews.remove(position);
                List<FrameOverview> frameOverviews = participantPlayer.frameOverviews;
                int positionFrom = frameOverviews.size() > 2 ? frameOverviews.size()-2 : 0;
                updateScores(participantPlayer.frameOverviews, positionFrom);
                adapter.delete(position);
                break;
            }
        }
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_FRAME_OVERVIEWS;
    }

    @Override
    protected boolean isDataSourceWorking() {
        return ParticipantService.isWorking(ParticipantService.ACTION_GET_BY_MATCH_ID_WITH_ALL_CONTENTS);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(ParticipantService.ACTION_GET_BY_MATCH_ID_WITH_ALL_CONTENTS);
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
                case ParticipantService.ACTION_GET_BY_MATCH_ID_WITH_ALL_CONTENTS: {
                    bindDataOnView(intent);
                    break;
                }
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
}
