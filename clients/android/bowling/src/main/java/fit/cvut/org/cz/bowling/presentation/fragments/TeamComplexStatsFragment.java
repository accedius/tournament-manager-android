package fit.cvut.org.cz.bowling.presentation.fragments;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.util.Pair;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.entities.FrameOverview;
import fit.cvut.org.cz.bowling.business.entities.ParticipantSharedViewModel;
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
    ParticipantPlayerAdapter participantSpinnerAdapter;
    protected static List<Participant> matchParticipants;
    protected static List<ParticipantPlayer> matchParticipantPlayers;
    protected long matchId;
    boolean isSwitchedBetweenInputModes;
    protected TournamentType tournamentType;
    private Fragment thisFragment;
    int maxFrameScore = ConstraintsConstants.tenPinFrameMaxScore;
    int maxFramesPerPlayer = ConstraintsConstants.tenPinMatchParticipantMaxFrames;


    private ParticipantSharedViewModel participantSharedViewModel;

    private class ParticipantPlayer {
        String name;
        List<FrameOverview> frameOverviews;
        PlayerStat playerStat;
        int matchParticipantReferencePosition;
        long participantId;

        public ParticipantPlayer() {}

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
        //whether or not the game is completed by all participants
        boolean isMatchPlayed = false;
        byte participantsWhoCompletedGameNumber = 0;

        //TODO make more sophisticated method (toAdd, toDelete, toEdit scheme)
        for(Participant participant : matchParticipants) {
            ParticipantStat participantStat = (ParticipantStat) participant.getParticipantStats().get(0);
            participantStat.setFrames(new ArrayList<Frame>());
            participantStat.setScore(0);
            participantStat.setFramesPlayedNumber((byte) 0);
            List<ParticipantStat> stats = new ArrayList<>();
            stats.add(participantStat);
            participant.setParticipantStats(stats);
        }

        for(ParticipantPlayer participantPlayer : matchParticipantPlayers) {
            PlayerStat playerStat = participantPlayer.playerStat;
            playerStat.setStrikes(0);
            playerStat.setSpares(0);

            List<FrameOverview> frameOverviews = participantPlayer.frameOverviews;
            Participant participant = matchParticipants.get(participantPlayer.matchParticipantReferencePosition);
            ParticipantStat participantStat = (ParticipantStat) participant.getParticipantStats().get(0);
            List<Frame> newFrames = new ArrayList<>();
            byte i = 0;
            for(FrameOverview overview : frameOverviews) {
                ++i;
                Frame newFrame = new Frame(matchId, participant.getId(), i, overview.getPlayerId());
                List<Byte> rolls = overview.getRolls();
                byte j = 1;
                List<Roll> newRolls = new ArrayList<>();
                int frameScore = 0;
                for(Byte roll : rolls) {
                    frameScore += roll;
                    Roll newRoll = new Roll(newFrame.getId(), j, overview.getPlayerId(), roll);
                    newRolls.add(newRoll);
                    ++j;
                }

                addStSpFromFrame(rolls, frameScore, i, playerStat);

                newFrame.setRolls(newRolls);
                newFrames.add(newFrame);
            }
            int points = 0;
            if(i > 0) {
                points = frameOverviews.get(i - 1).getCurrentScore();
            }
            playerStat.setPoints(points);
            playerStat.setFramesPlayedNumber(i);

            int participantScore = participantStat.getScore() + points;
            participantStat.setScore(participantScore);
            int participantFrames = participantStat.getFramesPlayedNumber() + i;
            participantStat.setFramesPlayedNumber((byte) participantFrames);
            participantStat.getFrames().addAll(newFrames);
        }

        for(Participant participant : matchParticipants) {
            ParticipantStat participantStat = (ParticipantStat) participant.getParticipantStats().get(0);
            if(participantStat.getFramesPlayedNumber() == ConstraintsConstants.tenPinMatchParticipantMaxFrames * participant.getPlayerStats().size())
                ++participantsWhoCompletedGameNumber;
        }
        if(participantsWhoCompletedGameNumber == matchParticipants.size())
            isMatchPlayed = true;

        Bundle matchStatsBundle = new Bundle();
        matchStatsBundle.putBoolean(EXTRA_BOOLEAN_IS_MATCH_PLAYED, isMatchPlayed);
        matchStatsBundle.putParcelableArrayList(ExtraConstants.EXTRA_PARTICIPANTS, (ArrayList<? extends Parcelable>) matchParticipants);
        return matchStatsBundle;
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
    public List<Participant> getMatchParticipants() {
        return matchParticipants;
    }

    public static TeamComplexStatsFragment newInstance(long matchId, boolean isSwitchedBetweenInputModes) {
        TeamComplexStatsFragment fragment = new TeamComplexStatsFragment();

        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_MATCH_ID, matchId);
        args.putBoolean(ExtraConstants.EXTRA_BOOLEAN_IS_INPUT_TYPE_CHANGED, isSwitchedBetweenInputModes);
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
        isSwitchedBetweenInputModes = getArguments().getBoolean(ExtraConstants.EXTRA_BOOLEAN_IS_INPUT_TYPE_CHANGED);
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
                    if ( newState == RecyclerView.SCROLL_STATE_IDLE && matchParticipants != null && matchParticipants.size() > 0 && ((ParticipantPlayer) participantSpinner.getSelectedItem()).frameOverviews.size() != maxFramesPerPlayer ) {
                        fab.show();
                    } else {
                        fab.hide();
                    }
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        participantSharedViewModel = ViewModelProviders.of(requireActivity()).get(ParticipantSharedViewModel.class);

        participantSharedViewModel.getToAdd().observe(getViewLifecycleOwner(), new Observer<List<Participant>>() {
            @Override
            public void onChanged(@Nullable List<Participant> participants) {
                if(participants != null && participants.size() > 0) {
                    if(matchParticipants == null) {
                        matchParticipants = new ArrayList<>();
                    }
                    addParticipantPlayers(participants);
                    matchParticipants.addAll(participants);
                    switchRecyclerViewsProgressBar();
                    participantSpinnerAdapter = null;
                    bindParticipantsOnSpinner();
                }
            }
        });

        participantSharedViewModel.getToDelete().observe(getViewLifecycleOwner(), new Observer<Participant>() {
            @Override
            public void onChanged(@Nullable Participant participant) {
                if(participant == null)
                    return;

                boolean foundAndRemoved = removeParticipant(participant);
                if(foundAndRemoved) {
                    //switchRecyclerViewsProgressBar();
                    participantSpinnerAdapter = null;
                    bindParticipantsOnSpinner();
                }
            }
        });

        participantSharedViewModel.getToManage().observe(getViewLifecycleOwner(), new Observer<Participant>() {
            @Override
            public void onChanged(@Nullable Participant participant) {
                if(participant == null)
                    return;

                Pair<Participant, Integer> foundParticipant = findParticipant(participant.getParticipantId());
                if(foundParticipant == null)
                    return;
                Participant participantToChange = foundParticipant.first;
                int position = foundParticipant.second;
                ParticipantStat participantStat = (ParticipantStat) participantToChange.getParticipantStats().get(0);
                List<PlayerStat> oldPlayerStats = (List<PlayerStat>) participantToChange.getPlayerStats();
                List<PlayerStat> newPlayerStats = (List<PlayerStat>) participant.getPlayerStats();
                List<PlayerStat> toAdd = new ArrayList<>();
                List<PlayerStat> toRemove = new ArrayList<>();

                //pair -> action, position in array
                Map<Long, Pair<Integer, PlayerStat>> actionMap = new HashMap<>();
                int remove = 0, stay = 1;

                for(PlayerStat ps : oldPlayerStats) {
                    actionMap.put(ps.getPlayerId(), new Pair<>(remove, ps));
                }

                for(PlayerStat newPlayerStat : newPlayerStats) {
                    long playerId = newPlayerStat.getPlayerId();
                    if(actionMap.get(playerId) != null) {
                        actionMap.put(playerId, new Pair<>(stay, null));
                    } else {
                        toAdd.add(newPlayerStat);
                    }
                }

                for(Map.Entry<Long, Pair<Integer, PlayerStat>> entry : actionMap.entrySet()) {
                    if(entry.getValue().first == remove) {
                        PlayerStat playerStat = entry.getValue().second;
                        int score = participantStat.getScore() - playerStat.getPoints();
                        int frames = participantStat.getFramesPlayedNumber() - playerStat.getFramesPlayedNumber();
                        participantStat.setScore(score);
                        participantStat.setFramesPlayedNumber((byte) frames);
                        toRemove.add(playerStat);
                    }
                }

                participantSharedViewModel.setToChangeStat(participantToChange);

                if(toRemove.size() > 0) {
                    removePlayerStatsFromParticipant(toRemove, participantToChange, position);
                }
                if(toAdd.size() > 0) {
                    addPlayerStatsToParticipant(toAdd, participantToChange, position);
                }

                participantSpinnerAdapter = null;
                bindParticipantsOnSpinner();
            }
        });
    }

    private void addParticipantPlayers(List<Participant> participants) {
        int i = matchParticipants.size();
        for(Participant newParticipant : participants) {
            ParticipantStat participantStat = (ParticipantStat) newParticipant.getParticipantStats().get(0);
            if(participantStat.getFrames() == null) {
                participantStat.setFrames(new ArrayList<>());
            }
            if(newParticipant.getPlayerStats() != null) {
                for(int j = 0; j < newParticipant.getPlayerStats().size(); ++j) {
                    PlayerStat playerStat = (PlayerStat) newParticipant.getPlayerStats().get(j);
                    List<FrameOverview> playerFrameOverviews = setPlayerFrames(playerStat, newParticipant);
                    String name = newParticipant.getName() + " - " + playerStat.getName();
                    ParticipantPlayer participantPlayer = new ParticipantPlayer(name, playerFrameOverviews, playerStat, i, newParticipant.getParticipantId());
                    matchParticipantPlayers.add(participantPlayer);
                }
                ++i;
            }
        }
        if(i > matchParticipants.size()) {
            orderParticipantPlayers();
        }
    }

    private void addPlayerStatsToParticipant(List<PlayerStat> toAdd, Participant participant, int position) {
        for(PlayerStat playerStat : toAdd) {
            //(String name, List<FrameOverview> frameOverviews, PlayerStat playerStat, int matchParticipantReferencePosition, long participantId)
            String name = participant.getName() + " - " + playerStat.getName();
            ParticipantPlayer participantPlayer = new ParticipantPlayer(name, new ArrayList<FrameOverview>(), playerStat, position, participant.getParticipantId());
            matchParticipantPlayers.add(participantPlayer);
        }
        orderParticipantPlayers();
        List<PlayerStat> stats = (List<PlayerStat>) participant.getPlayerStats();
        stats.addAll(toAdd);
        participant.setPlayerStats(stats);
    }

    private void removePlayerStatsFromParticipant(List<PlayerStat> toRemove, Participant participant, int position) {
        List<ParticipantPlayer> ppToRemove = new ArrayList<>();
        for(ParticipantPlayer participantPlayer : matchParticipantPlayers) {
            if(participantPlayer.matchParticipantReferencePosition == position) {
                for(PlayerStat playerStat : toRemove) {
                    if(playerStat.getPlayerId() == participantPlayer.playerStat.getPlayerId()) {
                        ppToRemove.add(participantPlayer);
                        break;
                    }
                }
            }
        }
        if(ppToRemove.size() > 0) {
            matchParticipantPlayers.removeAll(ppToRemove);
        }
        List<PlayerStat> stats = (List<PlayerStat>) participant.getPlayerStats();
        stats.removeAll(toRemove);
        participant.setPlayerStats(stats);
    }

    private boolean removeParticipant(Participant participantToRemove) {
        int i = 0;
        boolean found = false;
        for(Participant participant : matchParticipants) {
            if(participant.getParticipantId() == participantToRemove.getParticipantId()) {
                found = true;
                break;
            }
            ++i;
        }
        if(found) {
            List<ParticipantPlayer> toDelete = new ArrayList<>();
            for(ParticipantPlayer participantPlayer : matchParticipantPlayers) {
                if(participantPlayer.matchParticipantReferencePosition > i) {
                    int refPos = participantPlayer.matchParticipantReferencePosition - 1;
                    participantPlayer.matchParticipantReferencePosition = refPos;
                } else if(participantPlayer.matchParticipantReferencePosition == i) {
                    toDelete.add(participantPlayer);
                }
            }
            matchParticipantPlayers.removeAll(toDelete);
            matchParticipants.remove(i);
            return true;
        }
        return false;
    }

    private Pair<Participant, Integer> findParticipant(long participantId) {
        int position = 0;
        for(Participant participant : matchParticipants) {
            if(participantId == participant.getParticipantId()) {
                return new Pair<>(participant, position);
            }
            ++position;
        }
        return null;
    }

    private List<ParticipantPlayer> orderParticipantPlayers() {
        ParticipantPlayerComparatorByName comparatorByName = new ParticipantPlayerComparatorByName();
        Collections.sort(matchParticipantPlayers, comparatorByName);
        return matchParticipantPlayers;
    }

    private class ParticipantPlayerComparatorByName implements Comparator<ParticipantPlayer> {
        @Override
        public int compare(ParticipantPlayer o1, ParticipantPlayer o2) {
            return o1.name.compareTo(o2.name);
        }
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if(matchParticipants == null) {
            matchParticipants = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PARTICIPANTS);
            if (matchParticipants == null || matchParticipants.size() < 1) {
                fab.hide();
            }
            orderParticipantsAndTheirPlayers(matchParticipants);
            bindParticipantsOnSpinner();
            return;
        }

        if(participantSpinner.getSelectedItem() == null && matchParticipantPlayers != null){
            bindParticipantsOnSpinner();
            return;
        }

        if (matchParticipants.size() < 1) {
            fab.hide();
        } else {
            ParticipantPlayer participantPlayer = (ParticipantPlayer) participantSpinner.getSelectedItem();
            List<FrameOverview> frameOverviews = participantPlayer.frameOverviews;
            if(frameOverviews.size() >= maxFramesPerPlayer) {
                fab.hide();
            } else {
                fab.show();
            }
            intent.putParcelableArrayListExtra(getDataKey(), (ArrayList<? extends Parcelable>) frameOverviews);
            super.bindDataOnView(intent);
        }
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
            if(isSwitchedBetweenInputModes) {
                ParticipantStat participantStat = (ParticipantStat) participant.getParticipantStats().get(0);
                participantStat.setScore(0);
                participantStat.setFramesPlayedNumber((byte) 0);
                participantSharedViewModel.setToChangeStat(participant);
            }
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
        isSwitchedBetweenInputModes = false;

        return participantPlayers;
    }

    private List<FrameOverview> setPlayerFrames(PlayerStat playerStat, Participant participant) {
        ParticipantStat participantStat = (ParticipantStat) participant.getParticipantStats().get(0);
        List<Frame> participantFrames = participantStat.getFrames();
        List<Frame> playerFrames = new ArrayList<>();
        List<FrameOverview> playerFrameOverviews = new ArrayList<>();
        String playerName = playerStat.getName();
        long playerId = playerStat.getPlayerId();
        byte i = 1;
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
        if(matchParticipantPlayers == null || participantSpinnerAdapter == null) {
            if(matchParticipantPlayers == null) {
                matchParticipantPlayers = createMatchParticipantPlayers();
            }
            participantSpinnerAdapter = new ParticipantPlayerAdapter(getContext(), android.R.layout.simple_spinner_item, matchParticipantPlayers);
            participantSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        participantSpinner.setAdapter(participantSpinnerAdapter);
        participantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bindDataOnView(new Intent());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fab.hide();
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

    private void setIsMatchPlayedInParentFragment(){
        //Check if match is played to set MatchEditStatsFragment's CheckBox validForStats checked to true or false
        int participantsWhoNotCompletedGameNumber = matchParticipants.size();
        for(Participant participant : matchParticipants){
            ParticipantStat overallStat = (ParticipantStat) participant.getParticipantStats().get(0);
            if( overallStat.getFramesPlayedNumber() == ConstraintsConstants.tenPinMatchParticipantMaxFrames * participant.getPlayerStats().size() )
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

                participantStat.setFramesPlayedNumber(framesPlayedNumber);

                if(newScore != oldScore) {
                    int score = participantStat.getScore();
                    score += newScore - oldScore;
                    participantStat.setScore(score);

                    participantSharedViewModel.setToChangeStat(participant);
                }

                setIsMatchPlayedInParentFragment();

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

                    participantSharedViewModel.setToChangeStat(participant);
                }

                //((FrameOverviewAdapter) adapter).setItem(position, new FrameOverview(editedFrame) );
                bindDataOnView(new Intent());
                break;
            }
            case RequestCodes.REMOVE_FRAME: {
                int position = data.getIntExtra(ExtraConstants.EXTRA_POSITION, -1);
                FrameOverview overview = participantPlayer.frameOverviews.get(position);
                int oldScore = overview.getCurrentScore();
                participantPlayer.frameOverviews.remove(position);
                List<FrameOverview> frameOverviews = participantPlayer.frameOverviews;
                int positionFrom = frameOverviews.size() > 2 ? frameOverviews.size()-2 : 0;
                updateScores(participantPlayer.frameOverviews, positionFrom);
                bindDataOnView(new Intent());

                int newScore = 0;
                if(participantPlayer.frameOverviews.size() > 0) {
                    newScore = participantPlayer.frameOverviews.get(position-1).getCurrentScore();
                }

                byte newFramesCount = participantStat.getFramesPlayedNumber();
                --newFramesCount;
                participantStat.setFramesPlayedNumber(newFramesCount);

                if(newScore != oldScore) {
                    int score = participantStat.getScore();
                    score += newScore - oldScore;
                    participantStat.setScore(score);

                    participantSharedViewModel.setToChangeStat(participant);
                }

                setIsMatchPlayedInParentFragment();

                if(position + 1 == maxFramesPerPlayer) {
                    fab.show();
                }

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

    private class ParticipantReceiver extends BroadcastReceiver {
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
