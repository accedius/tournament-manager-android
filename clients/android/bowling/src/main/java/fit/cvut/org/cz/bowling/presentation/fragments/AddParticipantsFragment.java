package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.presentation.adapters.SelectParticipantAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.PlayerService;
import fit.cvut.org.cz.bowling.presentation.services.TeamService;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractSelectableListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.vh.OneActionViewHolder;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

public class AddParticipantsFragment extends AbstractSelectableListFragment<Participant> {
    public static final int OPTION_INDIVIDUALS = 1;
    public static final int OPTION_TEAMS = 2;

    private String serviceAction;
    private int option;
    private long matchId;

    /**
     * return new instance of this fragment with set arguments
     * @param option option where to get data from (individuals or teams)
     * @param matchId id of match to add to
     * @param omitParticipants participants to omit from the list
     * @return new instance of this fragment
     */
    public static AddParticipantsFragment newInstance(int option, long matchId, ArrayList<Participant> omitParticipants){
        AddParticipantsFragment fragment = new AddParticipantsFragment();
        Bundle b = new Bundle();
        b.putInt(ExtraConstants.EXTRA_OPTION, option);
        b.putLong(ExtraConstants.EXTRA_ID, matchId);
        b.putParcelableArrayList(ExtraConstants.EXTRA_OMIT, omitParticipants);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        option = getArguments().getInt(ExtraConstants.EXTRA_OPTION);
        matchId = getArguments().getLong(ExtraConstants.EXTRA_ID);
        switch (option) {
            case OPTION_INDIVIDUALS:
                serviceAction = PlayerService.ACTION_GET_PLAYERS_IN_TOURNAMENT_BY_MATCH_ID;
                break;
            case OPTION_TEAMS:
                serviceAction = TeamService.ACTION_GET_TEAMS_IN_TOURNAMENT_BY_MATCH_ID;
                break;
        }
    }

    @Override
    public void askForData() {
        Intent intent;
        switch (option) {
            case OPTION_INDIVIDUALS:
                intent = PlayerService.newStartIntent(serviceAction, getContext());
                break;
            case OPTION_TEAMS:
                intent = TeamService.newStartIntent(serviceAction, getContext());
                break;
            default:
                intent = new Intent();
                break;
        }
        intent.putExtra(ExtraConstants.EXTRA_ID, matchId);

        getContext().startService(intent);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        ArrayList<Participant> omitParticipants = getArguments().getParcelableArrayList(ExtraConstants.EXTRA_OMIT);
        ArrayList<Participant> participantsToShow = new ArrayList<>();
        switch (option) {
            case OPTION_INDIVIDUALS:
                ArrayList<Player> individuals = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PLAYERS);
                for(Player individual : individuals) {
                    boolean flagNotOmit = true;
                    for (Participant omitParticipant : omitParticipants) {
                        if(individual.getId() == omitParticipant.getParticipantId()) {
                            flagNotOmit = false;
                            break;
                        }
                    }
                    if(flagNotOmit) {
                        Participant participantFromIndividual = new Participant(matchId, individual.getId(), null);
                        participantFromIndividual.setName(individual.getName());
                        /*PlayerStat individualStat = new PlayerStat(-1, individual.getId());
                        List<PlayerStat> participantPlayerStats = new ArrayList<>();
                        participantPlayerStats.add(individualStat);
                        participantFromIndividual.setPlayerStats(participantPlayerStats);
                        participantFromIndividual.setParticipantStats(new ArrayList<ParticipantStat>());*/
                        participantsToShow.add(participantFromIndividual);
                    }
                }
                break;
            case OPTION_TEAMS:
                intent.putIntegerArrayListExtra(getDataSelectedKey(), new ArrayList<Integer>());

                ArrayList<Team> teams = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_TEAMS);
                for (Team team : teams) {
                    boolean flagNotOmit = true;
                    for(Participant omitParticipant : omitParticipants) {
                        if(team.getId() == omitParticipant.getParticipantId()){
                            flagNotOmit = false;
                            break;
                        }
                    }
                    if(flagNotOmit) {
                        Participant participantFromTeam = new Participant(matchId, team.getId(), null);
                        participantFromTeam.setName(team.getName());
                        participantsToShow.add(participantFromTeam);
                    }
                }
                break;
        }
        intent.putParcelableArrayListExtra(getDataKey(), participantsToShow);
        super.bindDataOnView(intent);
    }

    @Override
    protected AbstractSelectableListAdapter<Participant, ? extends OneActionViewHolder> getAdapter() {
        return new SelectParticipantAdapter();
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_PARTICIPANTS;
    }

    @Override
    protected String getDataSelectedKey() {
        return ExtraConstants.EXTRA_SELECTED;
    }

    @Override
    protected boolean isDataSourceWorking() {
        if (serviceAction == null) return true;
        switch (option) {
            case OPTION_INDIVIDUALS:
                return PlayerService.isWorking(serviceAction);
            case OPTION_TEAMS:
                return TeamService.isWorking(serviceAction);
        }
        return false;
    }

    @Override
    protected void registerReceivers() {
        if (serviceAction == null) return;
        Context context = getContext();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.registerReceiver(receiver, new IntentFilter(serviceAction));
    }

    @Override
    protected void unregisterReceivers() {
        Context context = getContext();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);
        localBroadcastManager.unregisterReceiver(receiver);
    }
}
