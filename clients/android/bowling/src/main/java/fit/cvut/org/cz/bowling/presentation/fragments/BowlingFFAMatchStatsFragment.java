package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.TeamManager;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.activities.AddParticipantsActivity;
import fit.cvut.org.cz.bowling.presentation.activities.AddPlayersActivity;
import fit.cvut.org.cz.bowling.presentation.adapters.MatchPlayerStatisticsAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.StatsService;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Fragment is used in
 */
public class BowlingFFAMatchStatsFragment extends AbstractDataFragment {
    private static final String SAVE_LIST = "save_list";
    private static final String SAVE_PART = "save_part";

    public static final int REQUEST_PART = 1;
    public static final int REQUEST_EDIT = 3;


    private MatchPlayerStatisticsAdapter adapter;

    private List<Participant> matchParticipants = null;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ScrollView scrv;
    private long matchId;
    List<List<PlayerStat>> participantPlayerStats;
    List<PlayerStat> matchPlayerStats;
    private Fragment thisFragment;
    private TournamentType tournamentType;
    private Match oldMatch;

    //Map<ParticipantId, Pair<IndexInOldMatchParticipantsIfExisted, ActionToDo>> - used to find by Participant's ParticipantId, optional - to see if existed originally and in that case that is the index in oldMatch.getParticipants().get(ourIndex), also stores data about db action for an object
    Map<Long, PairIndexAction<Integer, Byte>> participantsActionMap;

    private class PairIndexAction<L, R> {
        public L indexInOriginalParticipantsList;
        public R actionToDo;

        public PairIndexAction(L indexInOriginalParticipantsList, R actionToDo) {
            this.indexInOriginalParticipantsList = indexInOriginalParticipantsList;
            this.actionToDo = actionToDo;
        }

        public L first() {
            return indexInOriginalParticipantsList;
        }

        public void setIndexInOriginalParticipantsList(L indexInOriginalParticipantsList) {
            this.indexInOriginalParticipantsList = indexInOriginalParticipantsList;
        }

        public R second() {
            return actionToDo;
        }

        public void setActionToDo(R actionToDo) {
            this.actionToDo = actionToDo;
        }
    }

    private static final byte NO_ACTION = 0;
    private static final byte ACTION_ADD = 1;
    private static final byte ACTION_DELETE = 2;
    private static final byte ACTION_EDIT = 3;

    public Bundle getMatchParticipantPlayerStats () {
        List<Participant> oldParticipants = oldMatch.getParticipants();
        List<Participant> newParticipants = new ArrayList<>();
        for(Participant currentParticipant : matchParticipants) {
            PairIndexAction<Integer, Byte> participantInfo = participantsActionMap.get(currentParticipant.getParticipantId());
            if(participantInfo.indexInOriginalParticipantsList == -1) {
                newParticipants.add(currentParticipant);
            }
        }

        ArrayList<Participant> participantsToAdd = new ArrayList<>(); ArrayList<Participant> participantsToDelete = new ArrayList<>();
        ArrayList<PlayerStat> playerStatsToAdd = new ArrayList<>(); ArrayList<PlayerStat> playerStatsToEdit = new ArrayList<>(); ArrayList<PlayerStat> playerStatsToDelete = new ArrayList<>();

        //TODO: iterate through old and new participant lists and sort them and their player stats into corresponding arrays
        for(Participant oldParticipant : oldParticipants) {
            byte action = participantsActionMap.get(oldParticipant.getParticipantId()).actionToDo;
            switch (action) {
                case ACTION_EDIT: {

                    break;
                }
                case ACTION_DELETE: {

                    break;
                }
                default: {

                    break;
                }
            }
        }

        for(Participant newParticipant : newParticipants) {
            participantsToAdd.add(newParticipant);
            //TODO
        }

        Bundle matchParticipantsAndPlayers = new Bundle();
        matchParticipantsAndPlayers.putParcelableArrayList(ExtraConstants.PARTICIPANTS_TO_CREATE, participantsToAdd);
        matchParticipantsAndPlayers.putParcelableArrayList(ExtraConstants.PARTICIPANTS_TO_DELETE, participantsToDelete);

        matchParticipantsAndPlayers.putParcelableArrayList(ExtraConstants.PLAYER_STATS_TO_CREATE, playerStatsToAdd);
        matchParticipantsAndPlayers.putParcelableArrayList(ExtraConstants.PLAYER_STATS_TO_UPDATE, playerStatsToEdit);
        matchParticipantsAndPlayers.putParcelableArrayList(ExtraConstants.PLAYER_STATS_TO_DELETE, playerStatsToDelete);

        return matchParticipantsAndPlayers;
    }

    public static BowlingFFAMatchStatsFragment newInstance(long matchId) {
        BowlingFFAMatchStatsFragment fragment = new BowlingFFAMatchStatsFragment();
        Bundle b = new Bundle();
        b.putLong(ExtraConstants.EXTRA_MATCH_ID, matchId);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        matchId = getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID, -1);

        if (savedInstanceState != null) {
            matchParticipants = savedInstanceState.getParcelableArrayList(SAVE_PART);
            for(int i = 0; i < matchParticipants.size(); i++) {
                List<PlayerStat> tmp = savedInstanceState.getParcelableArrayList(SAVE_LIST + i);
                participantPlayerStats.set(i, tmp);
                matchPlayerStats.addAll(tmp);
                //TODO save and load actionIndices
            }
        } else {
            IManagerFactory iManagerFactory = ManagerFactory.getInstance();
            IParticipantManager iParticipantManager = iManagerFactory.getEntityManager(Participant.class);
            matchParticipants = iParticipantManager.getByMatchId(matchId);
            participantPlayerStats = null;
            oldMatch = iManagerFactory.getEntityManager(Match.class).getById(matchId);
            long tournamentId = oldMatch.getTournamentId();
            Tournament tournament = iManagerFactory.getEntityManager(Tournament.class).getById(tournamentId);
            tournamentType = TournamentTypes.getMyTournamentType(tournament.getTypeId());
        }
        thisFragment = this;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_match_stats, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_PART, new ArrayList<>(matchParticipants));
        for(int i = 0; i < matchParticipants.size(); i++)
            outState.putParcelableArrayList(SAVE_LIST+i, new ArrayList<>(participantPlayerStats.get(i)));
    }

    @Override
    public void customOnResume(){
        registerReceivers();
        if (!isDataSourceWorking())
            askForData();
        progressBar.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
    }

    @Override
    public void askForData() {
        Intent intent = StatsService.newStartIntent(StatsService.ACTION_GET_MATCH_PLAYER_STATISTICS, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID));
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return StatsService.isWorking(StatsService.ACTION_GET_MATCH_PLAYER_STATISTICS);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if (participantPlayerStats == null) {
            IManagerFactory iManagerFactory = ManagerFactory.getInstance(getContext());
            participantPlayerStats = new ArrayList<List<PlayerStat>>();
            matchPlayerStats = new ArrayList<>();
            participantsActionMap = new TreeMap<>();
            List<Participant> oldMatchParticipants = oldMatch.getParticipants();
            for (int i = 0; i < matchParticipants.size(); i++) {
                List<PlayerStat> tmp = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_STATS+i);
                String participantName;
                if(tournamentType.equals(TournamentTypes.teams())) {
                    Team team = iManagerFactory.getEntityManager(Team.class).getById(matchParticipants.get(i).getId());
                    participantName = team.getName();
                    tmp.get(0).setParticipantName(participantName);
                } else {
                    participantName = tmp.get(0).getName();
                    //tmp.get(0).setParticipantName(null); already null by default
                    //TODO more if needed
                }
                participantPlayerStats.add(tmp);
                matchPlayerStats.addAll(tmp);
                oldMatchParticipants.get(i).setPlayerStats(new ArrayList<>(tmp));
                matchParticipants.get(i).setName(participantName);
                matchParticipants.get(i).setPlayerStats(tmp);
                participantsActionMap.put(matchParticipants.get(i).getParticipantId(), new PairIndexAction<>(i, NO_ACTION));
            }
        }
        adapter.swapData(matchPlayerStats);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(StatsService.ACTION_GET_MATCH_PLAYER_STATISTICS));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View fragmentView = inflater.inflate(R.layout.fragment_ffa_match_stats_wrapper, container, false);

        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.rv_participants);
        scrv = (ScrollView) fragmentView.findViewById(R.id.scroll_v);

        adapter = new MatchPlayerStatisticsAdapter(getContext(), this);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floatingbutton_add, (ViewGroup)fragmentView, false);
        ((ViewGroup) fragmentView).addView(fab);

        setOnClickListeners();

        return fragmentView;
    }

    private void setOnClickListeners() {
        scrv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fab.hide();
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    fab.show();
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Participant> omitParticipants = new ArrayList<>(matchParticipants);
                int option = tournamentType.id == TournamentTypes.type_individuals ? AddParticipantsFragment.OPTION_INDIVIDUALS : AddParticipantsFragment.OPTION_TEAMS;
                Intent intent = AddParticipantsActivity.newStartIntent(getContext(), option, matchId);
                intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_OMIT, omitParticipants);
                thisFragment.startActivityForResult(intent, BowlingFFAMatchStatsFragment.REQUEST_PART);
            }
        });
    }

    /**
     * If should (checks if exists, existed before etc.), adds participant to list of participants to add in db for match
     * @param newParticipant
     * @return if pushed
     */
    private boolean pushParticipantToActionMapAdd(Participant newParticipant) {
        PairIndexAction<Integer, Byte> participantInfo = participantsActionMap.get(newParticipant.getParticipantId());
        if(participantInfo != null && participantInfo.actionToDo == ACTION_DELETE) {
                participantInfo.setActionToDo(NO_ACTION);
                Participant oldParticipant = oldMatch.getParticipants().get(participantInfo.indexInOriginalParticipantsList);
                newParticipant = new Participant(oldParticipant);
                newParticipant.setId(oldParticipant.getId());
                List<ParticipantStat> oldPS = new ArrayList<>( (ArrayList<ParticipantStat>) oldParticipant.getParticipantStats());
                List<PlayerStat> oldPlS = new ArrayList<>( (ArrayList<PlayerStat>) oldParticipant.getPlayerStats());
                newParticipant.setParticipantStats(oldPS);
                newParticipant.setPlayerStats(oldPlS);
                return false;
        }
        participantInfo = new PairIndexAction<>(-1, ACTION_ADD);
        participantsActionMap.put(newParticipant.getParticipantId(), participantInfo);
        return true;
        /*for(Participant oldParticipant : oldMatch.getParticipants()) {
            if(newParticipant.getParticipantId() == oldParticipant.getParticipantId()){
                newParticipant = new Participant(oldParticipant);
                newParticipant.setId(oldParticipant.getId());
                List<ParticipantStat> oldPS = new ArrayList<>( (ArrayList<ParticipantStat>) oldParticipant.getParticipantStats());
                List<PlayerStat> oldPlS = new ArrayList<>( (ArrayList<PlayerStat>) oldParticipant.getPlayerStats());
                newParticipant.setParticipantStats(oldPS);
                newParticipant.setPlayerStats(oldPlS);

                for(int i = 0; i < participantsToDelete.size(); ++i) {
                    if(participantsToDelete.get(i).getParticipantId() == newParticipant.getParticipantId()) {
                        participantsToDelete.remove(i);
                        break;
                    }
                }
                return;
            }
        }*/
    }

    private void integrateNewParticipants (List<Participant> newParticipants) {
        IManagerFactory managerFactory = ManagerFactory.getInstance(getContext());
        switch (tournamentType.id) {
            case TournamentTypes.type_individuals:
                for (Participant newParticipant : newParticipants) {
                    if(pushParticipantToActionMapAdd(newParticipant)) {
                        PlayerStat individualStat = new PlayerStat(-1, newParticipant.getId());
                        individualStat.setName(newParticipant.getName());
                        List<PlayerStat> participantsPlayerStats = new ArrayList<>();
                        participantsPlayerStats.add(individualStat);
                        newParticipant.setPlayerStats(participantsPlayerStats);
                        newParticipant.setParticipantStats(new ArrayList<ParticipantStat>());
                        participantPlayerStats.add(participantsPlayerStats);
                        matchPlayerStats.addAll(participantsPlayerStats);
                    }
                }
                break;
            case TournamentTypes.type_teams:
                for (Participant newParticipant : newParticipants) {
                    if(pushParticipantToActionMapAdd(newParticipant)) {
                        TeamManager teamManager = managerFactory.getEntityManager(Team.class);
                        Team participantTeam = teamManager.getById(newParticipant.getParticipantId());
                        List<Player> teamPlayers = participantTeam.getPlayers();
                        List<PlayerStat> participantsPlayerStats = new ArrayList<>();
                        for (Player teamPlayer : teamPlayers) {
                            PlayerStat individualStat = new PlayerStat(-1, newParticipant.getId());
                            individualStat.setName(teamPlayer.getName());
                            participantsPlayerStats.add(individualStat);
                        }
                        if (participantsPlayerStats.size() > 0)
                            participantsPlayerStats.get(0).setParticipantName(newParticipant.getName());
                        newParticipant.setPlayerStats(participantsPlayerStats);
                        newParticipant.setParticipantStats(new ArrayList<ParticipantStat>());
                        participantPlayerStats.add(participantsPlayerStats);
                        matchPlayerStats.addAll(participantsPlayerStats);
                    }
                }
                break;
        }
        matchParticipants.addAll(newParticipants);
        adapter.swapData(matchPlayerStats);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_PART:
                if (resultCode != AddPlayersActivity.RESULT_OK)
                    return;
                ArrayList<Participant> participantsToAdd = data.getParcelableArrayListExtra(ExtraConstants.EXTRA_DATA);
                if(participantsToAdd.size() > 0) {
                    integrateNewParticipants(participantsToAdd);
                }
                break;
        }
    }

    /**
     * Method to obtain statistics of players of current participants
     * @return list of statistics of players participating in match
     */
    public List<PlayerStat> getPlayerStats() {
        ArrayList<PlayerStat> res = new ArrayList<>(matchPlayerStats);
        return res;
    }

    /**
     * Starts an edit all activity with current data
     */
    public void editAll() {
        //participantPlayerStats = getHomeList();

        //Intent intent = EditAtOnceActivity.newStartIntent(getContext(), participantPlayerStats, tmpAwayStats);
        //startActivityForResult(intent, REQUEST_EDIT);
    }

    /**
     * Sets stats of a player. Used by edit stats dialog
     * @param participantId is id of the participant
     * @param position position in data
     * @param statistic statistic to be changed to
     */
    public void setPlayerStats(final long participantId, final int position, PlayerStat statistic) {
        Log.d("List de Bug","partindex = " + participantId + "; position = " + position);
        PlayerStat playerStatToSet = matchPlayerStats.get(position);
        if(playerStatToSet.getPoints() != statistic.getPoints() || playerStatToSet.getSpares() != statistic.getSpares() || playerStatToSet.getStrikes() != statistic.getStrikes()) {
            playerStatToSet.setPoints(statistic.getPoints());
            playerStatToSet.setSpares(statistic.getSpares());
            playerStatToSet.setStrikes(statistic.getStrikes());

            long actorId = getParticipantIdByPlayerStatPosition(position);
            participantsActionMap.get(actorId).setActionToDo(ACTION_EDIT);

            adapter.notifyItemChanged(position);
        }
    }

    private boolean pushParticipantToActionMapDelete (Participant participantToDelete) {
        PairIndexAction<Integer, Byte> participantInfo = participantsActionMap.get(participantToDelete.getParticipantId());
        if(participantInfo.actionToDo == ACTION_ADD) {
            participantsActionMap.remove(participantToDelete.getParticipantId());
            return false;
        }
        participantInfo.actionToDo = ACTION_DELETE;
        return true;
    }

    /**
     * removes player from team
     * @param participantId is id of the participant
     * @param position position in data to be removed
     */
    public void removePlayer(final long participantId, final int position){
        int positionOffset = 0;
        int participantIndex = 0;
        for(Participant participant : matchParticipants){
            if(participant.getId() != participantId) {
                positionOffset += participantPlayerStats.get(participantIndex).size();
            } else {
                break;
            }
            ++participantIndex;
        }
        int playerIndex = position - positionOffset;
        List<PlayerStat> playerStats = participantPlayerStats.get(participantIndex);
        playerStats.remove(playerIndex);
        matchPlayerStats.remove(position);
        adapter.delete(position);
        if(playerStats.size() == 0) {
            Participant participantToDelete = matchParticipants.get(participantIndex);
            pushParticipantToActionMapDelete(participantToDelete);
            matchParticipants.remove(participantIndex);
            participantPlayerStats.remove(participantIndex);
        }
    }

    private int getParticipantIndexById(long participantId) {
        int partindex = 0;
        for (int i = 0; i < matchParticipants.size(); i++) {
            if (matchParticipants.get(i).getId() == participantId) {
                partindex = i;
                break;
            }
        }
        return partindex;
    }

    private long getParticipantIdByPlayerStatPosition (final int position) {
        if(tournamentType.id == TournamentTypes.type_individuals) {
            return matchParticipants.get(position).getParticipantId();
        } else {
            int positionOffset = position;
            int participantIndex = 0;
            for (int i = 0; i < matchParticipants.size(); ++i) {
                if(participantPlayerStats.get(i).size() < 1)
                    continue;

                if (participantPlayerStats.get(i).size() <= positionOffset) {
                    positionOffset -= participantPlayerStats.get(i).size();
                } else {
                    return matchParticipants.get(i).getParticipantId();
                }
            }
            return -1;
        }
    }
}
