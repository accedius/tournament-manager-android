package fit.cvut.org.cz.bowling.presentation.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import fit.cvut.org.cz.bowling.presentation.services.ParticipantService;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;

public class IndividualComplexStatsFragment extends BowlingAbstractMatchStatsListFragment<FrameOverview> {

    protected static List<FrameOverview> frameOverviews = new ArrayList<>();
    protected static int participantSelectedIndex = 0;
    protected static TournamentType tournamentType = null;
    protected static List<List<FrameOverview>> participantsFrameOverviews = new ArrayList<>();
    protected static List<List<Player>> matchPlayers = new ArrayList<>();
    protected static List<Player> participantPlayers = new ArrayList<>();
    protected static ArrayList<Participant> matchParticipants = new ArrayList<>();
    private Spinner participantSpinner = null;
    private ArrayAdapter<Participant> participantArrayAdapter = null;
    protected static boolean bindFromDialogInsertions = false;
    protected static boolean participantsBinded = false;
    protected static final int REQUEST_CODE_UPDATE_LOCALLY = 1337;
    private static Fragment thisFragment;
    private long matchId;

    final static int maxFramesPerPlayer = ConstraintsConstants.tenPinMatchParticipantMaxFrames;
    final static int maxFrameScore = ConstraintsConstants.tenPinFrameMaxScore;
    final static int maxScore = ConstraintsConstants.tenPinMatchParticipantMaxScore;

    //@Override
    public Bundle getMatchStatsOld() {
        Bundle bundle = new Bundle();
        List<ParticipantStat> participantStatsToCreate = new ArrayList<>(), participantStatsToUpdate = new ArrayList<>();
        List<Frame> framesToCreate = new ArrayList<>(), framesToUpdate = new ArrayList<>(), framesToDelete = new ArrayList<>(), notChangedFramesButToAddRollsTo = new ArrayList<>();
        List<Roll> rollsToCreate = new ArrayList<>(), rollsToUpdate = new ArrayList<>(), rollsToDelete = new ArrayList<>();

        //whether or not the game is completed by all participants
        boolean isMatchPlayed = false;
        byte participantsWhoCompletedGameNumber = 0;

        int participantIndex = 0;
        for(Participant participant : matchParticipants) {
            //-----------------------------------------------------------
            int points = 0, framesPlayed = 0;
            PlayerStat playerStat = (PlayerStat) participant.getPlayerStats().get(0);
            playerStat.setStrikes(0);
            playerStat.setSpares(0);
            //-----------------------------------------------------------

            List<FrameOverview> overviews = participantsFrameOverviews.get(participantIndex);
            ParticipantStat stat;

            boolean noPreviousStats = participant.getParticipantStats().isEmpty();
            boolean participantStatToUpdate = true;

            //whether or not previous stats and new ones are different [ParticipantStat = PS]
            boolean isPreviousAndNewDifferentPS = true;

            if(noPreviousStats) {
                stat = new ParticipantStat();
                stat.setParticipantId(participant.getId());
                stat.setFramesPlayedNumber((byte)0);
                stat.setScore(0);
                participantStatToUpdate = false;
            } else {
                stat = (ParticipantStat) participant.getParticipantStats().get(0);
            }
            byte previousFramesNumber = stat.getFramesPlayedNumber();
            byte framesNumber = (byte) overviews.size();
            int score = framesNumber < 1 ? 0 : overviews.get(framesNumber-1).getCurrentScore();
            if(framesNumber < previousFramesNumber && previousFramesNumber > 0 && stat.getFrames() != null && !stat.getFrames().isEmpty()) {
                List<Frame> participantFrames = stat.getFrames();
                framesToDelete.addAll(participantFrames.subList(framesNumber, previousFramesNumber));
            } else if(previousFramesNumber == framesNumber && participantStatToUpdate && score == stat.getScore()){
                isPreviousAndNewDifferentPS = false;
            }

            stat.setFramesPlayedNumber(framesNumber);
            if(framesNumber < 1){
                stat.setScore(0);
                stat.setFrames(new ArrayList<Frame>());
            } else {

                //-----------------------------------------------------------
                framesPlayed = framesNumber;
                points = overviews.get(framesNumber - 1).getCurrentScore();
                //-----------------------------------------------------------

                stat.setScore(overviews.get(framesNumber - 1).getCurrentScore());
                List<Frame> participantFrames = stat.getFrames();
                if(participantFrames == null) {
                    participantFrames = new ArrayList<>();
                }
                int frameIndex = 0;
                for(FrameOverview overview : overviews) {

                    //-----------------------------------------------------------
                    int frameScore = 0;
                    for(Byte roll : overview.getRolls()) {
                        frameScore += roll;
                    }
                    addStSpFromFrame(overview.getRolls(), frameScore, frameIndex+1, playerStat);
                    //-----------------------------------------------------------

                    boolean frameToUpdate = true;

                    //whether or not previous stats and new ones are different [F = Frame]
                    boolean isPreviousAndNewDifferentF = true;

                    if(frameIndex >= participantFrames.size()) {
                        Frame frame = new Frame();
                        frame.setMatchId(matchId);
                        frame.setFrameNumber( (byte) (frameIndex+1));
                        frame.setParticipantId(participant.getId());
                        frame.setRolls(new ArrayList<Roll>());
                        participantFrames.add(frame);
                        frameToUpdate = false;
                    }
                    Frame frame = participantFrames.get(frameIndex);
                    frame.setPlayerId(overview.getPlayerId());

                    //no need to control other values, because participantId, frameNumber are the same, if the entity existed before
                    if(frame.getPlayerId() == overview.getPlayerId()){
                        isPreviousAndNewDifferentF = false;
                    }

                    int previousRollsNumber = frame.getRolls().size();
                    int rollsNumber = overview.getRolls().size();
                    List<Roll> frameRolls = frame.getRolls();
                    if(previousRollsNumber > rollsNumber) {
                        rollsToDelete.addAll(frameRolls.subList(rollsNumber, previousRollsNumber));
                    }
                    byte rollNumber = 1;
                    for(Byte overviewRoll : overview.getRolls()){
                        boolean rollToUpdate = true;

                        //whether or not previous stats and new ones are different [Roll = R]
                        boolean isPreviousAndNewDifferentR = true;

                        if(rollNumber > frameRolls.size()) {
                            Roll roll = new Roll();
                            frameRolls.add(roll);
                            rollToUpdate = false;
                        }
                        Roll roll = frameRolls.get(rollNumber - 1);

                        //no need to control other values, because frameId, rollNumber are the same, if the entity existed before
                        if(roll.getPoints() == overviewRoll && roll.getPlayerId() == frame.getPlayerId()){
                            isPreviousAndNewDifferentR = false;
                        }

                        roll.setFrameId(frame.getId());
                        roll.setPlayerId(frame.getPlayerId());
                        roll.setRollNumber(rollNumber);
                        roll.setPoints(overviewRoll);

                        if(rollToUpdate) {
                            if(isPreviousAndNewDifferentR){
                                rollsToUpdate.add(roll);
                            }
                        } else {
                            //little trick to identify different rolls before frame is created in database (frameId == 0)
                            roll.setId(frame.getParticipantId());
                            roll.setFrameId(frame.getFrameNumber());

                            if(frameToUpdate && !isPreviousAndNewDifferentF){
                                notChangedFramesButToAddRollsTo.add(frame);
                            }

                            rollsToCreate.add(roll);
                        }

                        ++rollNumber;
                    }
                    frame.setRolls(frameRolls);

                    if(frameToUpdate) {
                        if(isPreviousAndNewDifferentF) {
                            framesToUpdate.add(frame);
                        }
                    } else {
                        framesToCreate.add(frame);
                    }

                    ++frameIndex;
                }

                //participant has played all 10 frames => his game is completed
                if(frameIndex == ConstraintsConstants.tenPinMatchParticipantMaxFrames-1){
                    ++participantsWhoCompletedGameNumber;
                }

                stat.setFrames(participantFrames);

                //-----------------------------------------------------------
                playerStat.setPoints(points);
                playerStat.setFramesPlayedNumber((byte) framesPlayed);
                //-----------------------------------------------------------
            }

            if(noPreviousStats){
                List<ParticipantStat> stats = new ArrayList<>();
                stats.add(stat);
                participant.setParticipantStats(stats);
            }

            if(participantStatToUpdate) {
                if(isPreviousAndNewDifferentPS){
                    participantStatsToUpdate.add(stat);
                }
            } else {
                participantStatsToCreate.add(stat);
            }

            ++participantIndex;
        }

        if(participantsWhoCompletedGameNumber == matchParticipants.size()) {
            isMatchPlayed = true;
        }

        bundle.putBoolean(EXTRA_BOOLEAN_IS_MATCH_PLAYED, isMatchPlayed);

        bundle.putParcelableArrayList(PARTICIPANT_STATS_TO_CREATE, (ArrayList<? extends Parcelable>) participantStatsToCreate);
        bundle.putParcelableArrayList(PARTICIPANT_STATS_TO_UPDATE, (ArrayList<? extends Parcelable>) participantStatsToUpdate);

        bundle.putParcelableArrayList(FRAMES_TO_CREATE, (ArrayList<? extends Parcelable>) framesToCreate);
        bundle.putParcelableArrayList(FRAMES_TO_UPDATE, (ArrayList<? extends Parcelable>) framesToUpdate);
        bundle.putParcelableArrayList(FRAMES_TO_DELETE, (ArrayList<? extends Parcelable>) framesToDelete);
        bundle.putParcelableArrayList(NOT_CHANGED_FRAMES_BUT_TO_ADD_ROLLS_TO, (ArrayList<? extends Parcelable>) notChangedFramesButToAddRollsTo);

        bundle.putParcelableArrayList(ROLLS_TO_CREATE, (ArrayList<? extends Parcelable>) rollsToCreate);
        bundle.putParcelableArrayList(ROLLS_TO_UPDATE, (ArrayList<? extends Parcelable>) rollsToUpdate);
        bundle.putParcelableArrayList(ROLLS_TO_DELETE, (ArrayList<? extends Parcelable>) rollsToDelete);

        return bundle;
    }

    private void addStSpFromFrame (List<Byte> rolls, int frameScore, int frameNumber, PlayerStat playerStat) {
        int strikes = 0;
        int spares = 0;
        int i = frameNumber;
        int j = rolls.size();
        if(frameScore == maxFrameScore && i != maxFramesPerPlayer) {
            if (j == 1) {
                ++strikes;
            } else {
                ++spares;
            }
        } else if (i == maxFramesPerPlayer) {
            //basically all possibilities with strikes or spares (dot "." is any number of knocked down pins < 10) -> X(X(X)), X(./), ./(X)
            if(frameScore >= maxFrameScore && j > 2) {
                byte roll1 = rolls.get(0), roll2 = rolls.get(1), roll3 = rolls.get(2);
                if(roll1 == maxFrameScore) {
                    ++strikes;
                    if(roll2 == maxFrameScore) {
                        ++strikes;
                        if(roll3 == maxFrameScore) {
                            ++strikes;
                        }
                    } else if (roll2 + roll3 == maxFrameScore) {
                        ++spares;
                    }
                } else {
                    if (roll2 + roll1 == maxFrameScore) {
                        ++spares;
                        if(roll3 == maxFrameScore){
                            ++strikes;
                        }
                    }
                }
            }
        }

        if(strikes > 0) {
            playerStat.setStrikes(playerStat.getStrikes() + strikes);
        }
        if(spares > 0) {
            playerStat.setSpares(playerStat.getSpares() + spares);
        }
    }

    @Override
    public Bundle getMatchStats() {
        Bundle matchResults = getMatchStatsOld();
        return matchResults;
    }

    @Override
    public List<Participant> getMatchParticipants() {
        return matchParticipants;
    }

    public static IndividualComplexStatsFragment newInstance(long matchId) {
        IndividualComplexStatsFragment fragment = new IndividualComplexStatsFragment();

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
        bindFromDialogInsertions = false;
        participantSelectedIndex = 0;
        participantsFrameOverviews = null;
        frameOverviews = null;
        matchPlayers = null;
        participantPlayers = null;
        participantsBinded = false;

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
    protected AbstractListAdapter getAdapter() {
        return new FrameOverviewAdapter(tournamentType) {
            @Override
            protected void setOnClickListeners(View v, final int position, final long playerId, final byte frameNumber, List<Byte> rolls, String playerName, int currentScore) {
                super.setOnClickListeners(v, position, playerId, frameNumber, rolls, playerName, currentScore);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = frameNumber - 1;
                        EditFrameListDialog dialog = EditFrameListDialog.newInstance(index);
                        dialog.setTargetFragment(thisFragment, 0);
                        dialog.show(getFragmentManager(), "dialogEdit");
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int index = frameNumber - 1;
                        String title = "Frame #" + frameNumber;
                        EditDeleteFrameDialog dialog = EditDeleteFrameDialog.newInstance(index, title);
                        dialog.setTargetFragment(thisFragment, 0);
                        dialog.show(getFragmentManager(), "dialogEditDelete");
                        return true;
                    }
                });
            }
        };
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
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && frameOverviews.size()<10)
                        fab.show();

                    else fab.hide();
                }
            });
        }

        return fragmentView;
    }

    private int getNextRollScore(final int positionFrom, final int rollsNumber) {
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

    private void updateScores(final int positionFrom) {
        int arraySize = frameOverviews.size();
        for(int i = positionFrom; i < arraySize; ++i) {
            FrameOverview f = frameOverviews.get(i);
            int currentFrameScore = i == 0 ? 0 : frameOverviews.get(i-1).getCurrentScore();
            currentFrameScore += f.getFrameScore();
            if(f.getFrameScore() == maxFrameScore) {
                if(f.getRolls().get(0) == maxFrameScore){
                    currentFrameScore += getNextRollScore(i, 2);
                } else {
                    currentFrameScore += getNextRollScore(i, 1);
                }
            }
            f.setCurrentScore(currentFrameScore);
        }
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if(bindFromDialogInsertions) {
            bindFromDialogInsertions = false;
            //Check if match is played to set MatchEditStatsFragment's CheckBox partialDataPropagation checked attribute to true or false
            int participantsWhoNotCompletedGameNumber = participantsFrameOverviews.size();
            for(List<FrameOverview> overviews : participantsFrameOverviews) {
                if(overviews.size() == ConstraintsConstants.tenPinMatchParticipantMaxFrames)
                    --participantsWhoNotCompletedGameNumber;
            }
            if(getParentFragment() != null) {
                if(participantsWhoNotCompletedGameNumber == 0 ) {
                    getParentFragment().onActivityResult(getTargetRequestCode(), 1, null);
                } else {
                    getParentFragment().onActivityResult(getTargetRequestCode(), 0, null);
                }
            }
        } else {
            if(participantsFrameOverviews == null) {
                matchPlayers = new ArrayList<>();
                matchParticipants = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PARTICIPANTS);
                if(!matchParticipants.isEmpty()){
                    participantsFrameOverviews = new ArrayList<>();
                }
                IManagerFactory iManagerFactory = ManagerFactory.getInstance();
                int index = 0;
                for(Participant participant : matchParticipants) {
                    if(participant.getParticipantStats() == null) {
                        List<ParticipantStat> participantStats = new ArrayList<>();
                        ParticipantStat participantStat = new ParticipantStat(participant.getId(), 0, (byte) 0);
                        participantStats.add(participantStat);
                        participant.setParticipantStats(participantStats);
                    }

                    matchPlayers.add(new ArrayList<Player>());
                    participantPlayers = matchPlayers.get(index);

                    List<PlayerStat> playerStats = (List<PlayerStat>) participant.getPlayerStats();
                    for(PlayerStat stat : playerStats) {
                        Player player = iManagerFactory.getEntityManager(Player.class).getById(stat.getPlayerId());
                        participantPlayers.add(player);
                    }

                    String name;
                    long participantId = participant.getParticipantId();
                    if (tournamentType.equals(TournamentTypes.individuals())) {
                        Player player = iManagerFactory.getEntityManager(Player.class).getById(participantId);
                        name = player.getName();
                    } else {
                        Team team = iManagerFactory.getEntityManager(Team.class).getById(participantId);
                        if(team != null) {
                            name = team.getName();
                        } else {
                            name = "NONAME";
                        }
                    }
                    participant.setName(name);

                    participantsFrameOverviews.add(new ArrayList<FrameOverview>());
                    frameOverviews = participantsFrameOverviews.get(index);

                    if(participant.getParticipantStats() != null && !participant.getParticipantStats().isEmpty()){
                        ParticipantStat participantStat = (ParticipantStat) participant.getParticipantStats().get(0);
                        List<Frame> frames = participantStat.getFrames();
                        byte i = 1;

                        for(Frame frame : frames) {
                            byte frameScore = 0;
                            List<Byte> rolls = new ArrayList<>();
                            List<Roll> playedRolls = frame.getRolls();

                            for(Roll roll : playedRolls) {
                                byte rollPoints = roll.getPoints();
                                frameScore += rollPoints;
                                rolls.add(rollPoints);
                            }

                            long playerId = frame.getPlayerId();
                            Player player = iManagerFactory.getEntityManager(Player.class).getById(playerId);
                            String playerName = player.getName();
                            FrameOverview frameOverview = new FrameOverview(i, rolls, playerName, 0, playerId, participantStat.getParticipantId(), frameScore);
                            frameOverviews.add(frameOverview);

                            ++i;
                        }

                    }

                    updateScores(0);
                    ++index;
                }
            }
            intent.removeExtra(ExtraConstants.EXTRA_PARTICIPANTS);
        }
        if(participantsFrameOverviews == null) {
            fab.hide();
            return;
        }
        if(!participantsBinded){
            bindParticipantsOnView(matchParticipants);
            participantsBinded = true;
        }
        if(frameOverviews.size() >= 10 || participantSelectedIndex == -1) {
            fab.hide();
        } else if(fab.isOrWillBeHidden()) {
            fab.show();
        }
        intent.putParcelableArrayListExtra(getDataKey(), (ArrayList<FrameOverview>) frameOverviews);

        super.bindDataOnView(intent);
    }

    private void bindParticipantsOnView(ArrayList<Participant> matchParticipants) {
        participantArrayAdapter = new ArrayAdapter<Participant>(getContext(), android.R.layout.simple_spinner_item, matchParticipants);
        participantArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        participantSpinner.setAdapter(participantArrayAdapter);
        participantSpinner.setSelection(participantSelectedIndex);
        participantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                participantSelectedIndex = position;
                frameOverviews = participantsFrameOverviews.get(position);
                participantPlayers = matchPlayers.get(position);
                bindDataOnView(new Intent());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //fab.hide();
            }
        });
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        Context context = getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        FloatingActionButton fab = (FloatingActionButton) layoutInflater.inflate(R.layout.floatingbutton_add, parent, false);

        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       int index = frameOverviews.size();
                                       EditFrameListDialog dialog = EditFrameListDialog.newInstance(index);
                                       dialog.setTargetFragment(thisFragment, 0);
                                       dialog.show(getFragmentManager(), "dialogCreate");
                                   }
                               }
        );

        return fab;
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_FRAME_OVERVIEWS;
    }

    @Override
    public void askForData() {
        if(bindFromDialogInsertions) {
            Intent intent = new Intent();
            bindDataOnView(intent);
        } else {
            Intent intent = ParticipantService.newStartIntent(ParticipantService.ACTION_GET_BY_MATCH_ID_WITH_ALL_CONTENTS, getContext());
            Long matchId = getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID, -1);
            intent.putExtra(ExtraConstants.EXTRA_MATCH_ID, matchId);
            getContext().startService(intent);
        }
    }

    @Override
    protected boolean isDataSourceWorking() {
        return ParticipantService.isWorking(ParticipantService.ACTION_GET_BY_MATCH_ID_WITH_ALL_CONTENTS);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(ParticipantService.ACTION_GET_BY_MATCH_ID_WITH_ALL_CONTENTS);
        //filter.addAction(ParticipantService.ACTION_DELETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(participantReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(participantReceiver);
    }


    private BroadcastReceiver participantReceiver = new ParticipantReceiver();

    private class ParticipantReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            progressBar.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
            switch (action) {
                case ParticipantService.ACTION_GET_BY_MATCH_ID_WITH_ALL_CONTENTS: {
                    participantsBinded = false;
                    bindDataOnView(intent);
                    break;
                }
                /*case ParticipantService.ACTION_DELETE: {
                    break;
                }*/
            }
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_UPDATE_LOCALLY) {
            updateScores(resultCode);
            bindDataOnView(new Intent());
        } else {
            progressBar.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
        }
    }

    public static class EditFrameListDialog extends DialogFragment {
        TextView roll1, roll2, roll3;
        int position;
        FrameOverview frameOverview = null;
        int frameNumber;
        boolean toCreate = false;
        boolean initialInput;

        public static EditFrameListDialog newInstance(int arrayListPosition) {
            EditFrameListDialog dialog = new EditFrameListDialog();
            Bundle args = new Bundle();
            args.putInt(ExtraConstants.EXTRA_SELECTED, arrayListPosition);
            dialog.setArguments(args);
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            initialInput = true;

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setPositiveButton(fit.cvut.org.cz.tmlibrary.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //empty, because must be overridden in onResume in order not to close on bad input and instead check for constraints (https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked)
                }
            });

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_frame_throws, null);
            builder.setView(v);

            roll1 = v.findViewById(R.id.throw_1_input);
            roll2 = v.findViewById(R.id.throw_2_input);
            roll3 = v.findViewById(R.id.throw_3_input);
            position = getArguments().getInt(ExtraConstants.EXTRA_SELECTED);
            frameNumber = position + 1;
            if(position >= 0 && position < frameOverviews.size() ) {
                frameOverview = frameOverviews.get(position);
            } else if (position == frameOverviews.size()) {
                toCreate = true;
                frameOverview = new FrameOverview();
                frameOverview.setFrameNumber( (byte) frameNumber);
                if(tournamentType.equals(TournamentTypes.individuals())){
                    Player player = participantPlayers.get(0);
                    frameOverview.setPlayerName(player.getName());
                    frameOverview.setPlayerId(player.getId());
                } else {
                    //TODO if needed
                }
            }
            if (frameNumber == maxFramesPerPlayer)
                v.findViewById(R.id.throw_3_input_layout).setVisibility(View.VISIBLE);
            builder.setTitle(getResources().getString(R.string.frame_num) + frameNumber + ": " + frameOverview.getPlayerName());

            initialInput = false;

            return builder.create();
        }

        @Override
        public void onResume() {
            super.onResume();
            final AlertDialog dialog = (AlertDialog) getDialog();
            if(dialog != null) {
                Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean returnToken = false;
                        int pinsInRoll1 = 0;
                        int pinsInRoll2 = 0;
                        int pinsInRoll3 = 0;
                        if (!roll1.getText().toString().isEmpty() ) {
                            pinsInRoll1 = Integer.parseInt(roll1.getText().toString());
                        }

                        if (!roll2.getText().toString().isEmpty() ) {
                            pinsInRoll2 = Integer.parseInt(roll2.getText().toString());
                        }

                        if (frameNumber == maxFramesPerPlayer && !roll3.getText().toString().isEmpty() ) {
                            pinsInRoll3 = Integer.parseInt(roll3.getText().toString());
                        }

                        ArrayList<Byte> rolls = new ArrayList<>();

                        if( pinsInRoll1 > maxFrameScore || pinsInRoll1 < 0){
                            TextInputLayout til = getDialog().findViewById(R.id.throw_1_input_layout);
                            til.setError(getResources().getString(R.string.flf_roll_format_violated) + " " + maxFrameScore + "!");
                            returnToken = true;
                        } else rolls.add((byte) pinsInRoll1);

                        if( pinsInRoll2 > maxFrameScore || pinsInRoll2 < 0 ) {
                            TextInputLayout til = getDialog().findViewById(R.id.throw_2_input_layout);
                            til.setError(getResources().getString(R.string.flf_roll_format_violated) + " " + maxFrameScore + "!");
                            returnToken = true;
                        } else if (frameNumber == maxFramesPerPlayer || pinsInRoll1 != maxFrameScore) {
                            rolls.add((byte) pinsInRoll2);
                        }

                        if (frameNumber == maxFramesPerPlayer) {
                            if (pinsInRoll3 > maxFrameScore || pinsInRoll3 < 0) {
                                TextInputLayout til = getDialog().findViewById(R.id.throw_3_input_layout);
                                til.setError(getResources().getString(R.string.flf_roll_format_violated) + " " + maxFrameScore + "!");
                                returnToken = true;
                            } else if (pinsInRoll1 + pinsInRoll2 >= maxFrameScore) {
                                rolls.add((byte) pinsInRoll3);
                            }
                        }

                        if(returnToken)
                            return;

                        if ( pinsInRoll1 + pinsInRoll2 > maxFrameScore && ( frameNumber != maxFramesPerPlayer || pinsInRoll1 != maxFrameScore) ) {
                            TextInputLayout til = getDialog().findViewById(R.id.throw_2_input_layout);
                            til.setError(getResources().getString(R.string.flf_regular_frame_too_many_pins_error) + " " + maxFrameScore + "!");
                            returnToken = true;
                        }

                        if (pinsInRoll1 + pinsInRoll2 < maxFrameScore
                                && frameNumber == maxFramesPerPlayer
                                && pinsInRoll3 > 0) {
                            TextInputLayout til = getDialog().findViewById(R.id.throw_2_input_layout);
                            til.setError(getResources().getString(R.string.flf_last_frame_invalid_extra_roll_error));
                            returnToken = true;
                        }

                        if(returnToken)
                            return;

                        frameOverview.setRolls(rolls);
                        byte score = (byte)(pinsInRoll1 + pinsInRoll2 + pinsInRoll3);
                        int resultCodeUpdateFrom = position >= 2 ? position - 2 : 0 ;
                        frameOverview.setFrameScore(score);
                        if(toCreate) {
                            frameOverviews.add(frameOverview);
                        }

                        if (getTargetFragment() != null)
                            getTargetFragment().onActivityResult(REQUEST_CODE_UPDATE_LOCALLY, resultCodeUpdateFrom, null);
                        dialog.dismiss();
                    }
                });
            }
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            if(frameOverview != null && frameOverview.getRolls()!=null && !frameOverview.getRolls().isEmpty() ) {
                String previousRoll1, previousRoll2, previousRoll3;
                int playedRolls = frameOverview.getRolls().size();
                previousRoll1 = String.format(Locale.getDefault(), "%d", frameOverview.getRolls().get(0));
                roll1.setText(previousRoll1);
                if(playedRolls > 1) {
                    previousRoll2 = String.format(Locale.getDefault(), "%d", frameOverview.getRolls().get(1));
                    roll2.setText(previousRoll2);
                }
                if(playedRolls > 2) {
                    previousRoll3 = String.format(Locale.getDefault(), "%d", frameOverview.getRolls().get(2));
                    roll3.setText(previousRoll3);
                }
            }
            roll1.setFocusableInTouchMode(true);
            roll1.requestFocus();
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    public static class EditDeleteFrameDialog extends DialogFragment {
        int position;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            position = getArguments().getInt(ExtraConstants.EXTRA_POSITION);
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
            String[] items;
            if(position == frameOverviews.size()-1){
                items = new String[]{ getActivity().getString(R.string.edit), getActivity().getString(R.string.delete) };
            } else {
                items = new String[]{ getActivity().getString(R.string.edit)};
            }
            builder.setItems( items, supplyListener());

            builder.setTitle(getArguments().getString(ExtraConstants.EXTRA_TITLE));
            return builder.create();
        }

        protected DialogInterface.OnClickListener supplyListener() {
            return new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            EditFrameListDialog dialogEdit = EditFrameListDialog.newInstance(position);
                            dialogEdit.setTargetFragment(thisFragment, 0);
                            dialogEdit.show(getFragmentManager(), "dialogEdit");
                            dialog.dismiss();
                            break;
                        case 1:
                            frameOverviews.remove(position);
                            if (getTargetFragment() != null){
                                int resultCodeUpdateFrom = position >= 2 ? position - 2 : 0 ;
                                getTargetFragment().onActivityResult(REQUEST_CODE_UPDATE_LOCALLY, resultCodeUpdateFrom, null);
                            }
                            dialog.dismiss();
                            break;
                    }
                    dialog.dismiss();
                }
            };
        }

        public static EditDeleteFrameDialog newInstance (int position, String title) {
            EditDeleteFrameDialog dialog = new EditDeleteFrameDialog();
            Bundle args = new Bundle();
            args.putInt(ExtraConstants.EXTRA_POSITION, position);
            args.putString(ExtraConstants.EXTRA_TITLE, title);
            dialog.setArguments(args);
            return dialog;
        }
    }
}
