package fit.cvut.org.cz.bowling.presentation.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.entities.ParticipantOverview;
import fit.cvut.org.cz.bowling.business.managers.TeamManager;
import fit.cvut.org.cz.bowling.business.managers.interfaces.IPlayerStatManager;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.activities.AddParticipantsActivity;
import fit.cvut.org.cz.bowling.presentation.activities.SelectTeamPlayersActivity;
import fit.cvut.org.cz.bowling.presentation.adapters.MatchParticipantAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.dialogs.DeleteParticipantDialog;
import fit.cvut.org.cz.bowling.presentation.services.ParticipantService;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IPackagePlayerManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.ITeamManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

public class MatchParticipantsManageFragment extends AbstractDataFragment {
    ParticipantReceiver participantReceiver = new ParticipantReceiver();

    private static final String SAVE_PART = "save_part";

    private MatchParticipantAdapter adapter;

    private List<Participant> matchParticipants = null;
    private List<ParticipantOverview> participantStatOverviews = null;
    private int managedParticipantPosition;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ScrollView scrv;
    private long matchId;
    private Fragment thisFragment;
    private TournamentType tournamentType;

    private static final class RequestCodes {
        static final int NEW_PARTICIPANT = 1;
        static final int DELETE = 2;
        static final int MANAGE = 3;
    }

    public static MatchParticipantsManageFragment newInstance(long matchId) {
        MatchParticipantsManageFragment fragment = new MatchParticipantsManageFragment();
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
        } else {
            IManagerFactory iManagerFactory = ManagerFactory.getInstance();
            Match match = iManagerFactory.getEntityManager(Match.class).getById(matchId);
            long tournamentId = match.getTournamentId();
            Tournament tournament = iManagerFactory.getEntityManager(Tournament.class).getById(tournamentId);
            tournamentType = TournamentTypes.getMyTournamentType(tournament.getTypeId());
        }
        thisFragment = this;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_PART, new ArrayList<>(matchParticipants));
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View fragmentView = inflater.inflate(R.layout.fragment_match_participant_wrapper, container, false);

        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.rv_part);
        scrv = (ScrollView) fragmentView.findViewById(R.id.scroll_v);

        adapter = getAdapter();

        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floatingbutton_add, (ViewGroup)fragmentView, false);
        ((ViewGroup) fragmentView).addView(fab);

        setOnClickListeners();

        return fragmentView;
    }

    private MatchParticipantAdapter getAdapter() {
        return new MatchParticipantAdapter(getContext(), tournamentType) {
            @Override
            protected void setOnClickListeners(View clickableView, View buttonView, final ParticipantOverview overview, final int position) {
                super.setOnClickListeners(clickableView, buttonView, overview, position);

                clickableView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        long participantId = overview.getParticipantId();
                        findParticipant(participantId);
                        DeleteParticipantDialog dialog = DeleteParticipantDialog.newInstance(overview.getParticipantId(), position, overview.getName());
                        dialog.setTargetFragment(thisFragment, RequestCodes.DELETE);
                        dialog.show(getFragmentManager(), "DELETE_PARTICIPANT_DIALOG");
                        return true;
                    }
                });

                buttonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long participantId = overview.getParticipantId();
                        Participant selectedParticipant = findParticipant(participantId);
                        if(selectedParticipant != null) {
                            Intent selectTeamPlayersIntent = SelectTeamPlayersActivity.newStartIntent(getContext(), overview.getParticipantId(), (ArrayList<PlayerStat>) selectedParticipant.getPlayerStats());
                            thisFragment.startActivityForResult(selectTeamPlayersIntent, RequestCodes.MANAGE);
                        }
                    }
                });
            }
        };
    }

    private Participant findParticipant(long participantId) {
        int i = 0;
        for(Participant participant : matchParticipants) {
            if(participant.getParticipantId() == participantId) {
                managedParticipantPosition = i;
                return participant;
            }
            ++i;
        }
        managedParticipantPosition = -1;
        return null;
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
                thisFragment.startActivityForResult(intent, RequestCodes.NEW_PARTICIPANT);
            }
        });
    }

    @Override
    public void askForData() {
        Intent intent = ParticipantService.newStartIntent(ParticipantService.ACTION_GET_BY_MATCH_ID_FOR_MANAGING, getContext());
        intent.putExtra(ExtraConstants.EXTRA_MATCH_ID, matchId);
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return ParticipantService.isWorking(ParticipantService.ACTION_GET_BY_MATCH_ID);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        if(matchParticipants == null) {
            matchParticipants = intent.getParcelableArrayListExtra(ExtraConstants.EXTRA_PARTICIPANTS);
            participantStatOverviews = new ArrayList<>();
            for (int i = 0; i < matchParticipants.size(); i++) {
                String name = matchParticipants.get(i).getName();
                Participant participant = matchParticipants.get(i);

                //Compatibility check, in older generate scripts participantStats weren't being made on match generation -> mb fix by database upgrade script
                if(participant.getParticipantStats() == null || participant.getParticipantStats().size() < 1) {
                    ParticipantStat ps = new ParticipantStat(participant.getId());
                    List<ParticipantStat> participantStats = new ArrayList<>();
                    participantStats.add(ps);
                    participant.setParticipantStats(participantStats);
                }

                ParticipantOverview po = getParticipantOverview(participant);
                participantStatOverviews.add(po);
            }
        }

        participantStatOverviews = orderParticipantStats(participantStatOverviews);
        adapter.swapData(participantStatOverviews);
    }

    private ParticipantOverview getParticipantOverview(Participant participant) {
        long participantId = participant.getParticipantId();
        ParticipantStat participantStat = (ParticipantStat) participant.getParticipantStats().get(0);
        long participantStatId = participantStat.getId();
        int score = participantStat.getScore();
        byte frame = participantStat.getFramesPlayedNumber();
        String name = participant.getName();
        return new ParticipantOverview(participantStatId, participantId, name, score, frame);
    }


    private class ParticipantStatComparatorByScore implements Comparator<ParticipantOverview> {
        @Override
        public int compare(ParticipantOverview o1, ParticipantOverview o2) {
            return o2.getScore()-o1.getScore();
        }
    }

    private List<ParticipantOverview> orderParticipantStats(List<ParticipantOverview> list) {
        ParticipantStatComparatorByScore comparatorByScore = new ParticipantStatComparatorByScore();
        Collections.sort(list,comparatorByScore);
        return list;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodes.NEW_PARTICIPANT: {
                if (resultCode != AddParticipantsActivity.RESULT_OK)
                    return;
                ArrayList<Participant> participantsToAdd = data.getParcelableArrayListExtra(ExtraConstants.EXTRA_DATA);
                if (participantsToAdd.size() > 0) {
                    integrateNewParticipants(participantsToAdd);
                }
                break;
            }
            case RequestCodes.DELETE: {
                if(resultCode != Activity.RESULT_OK)
                    return;
                int position = data.getIntExtra(ExtraConstants.EXTRA_POSITION, -1);
                participantStatOverviews.remove(position);
                matchParticipants.remove(managedParticipantPosition);
                break;
            }
            case RequestCodes.MANAGE: {
                if(resultCode != SelectTeamPlayersActivity.RESULT_OK)
                    return;
                List<Player> teamPlayers = data.getParcelableArrayListExtra(ExtraConstants.EXTRA_DATA);
                setTeamPlayers(teamPlayers);
                break;
            }
        }
        bindDataOnView(null);
    }

    private void removeParticipantOverview(long participantId) {
        int i = 0;
        for(ParticipantOverview overview : participantStatOverviews) {
            if(overview.getParticipantId() == participantId) {
                break;
            }
            ++i;
        }
        participantStatOverviews.remove(i);
    }

    private void setTeamPlayers(List<Player> newTeamPlayers) {
        Participant teamToSetPlayersFor = matchParticipants.get(managedParticipantPosition);
        List<PlayerStat> newPlayerStats = new ArrayList<>();
        for(Player player : newTeamPlayers) {
            PlayerStat newPlayerStat = new PlayerStat(teamToSetPlayersFor.getId(), player.getId());
            newPlayerStats.add(newPlayerStat);
        }
        teamToSetPlayersFor.setPlayerStats(newPlayerStats);
    }

    private void integrateNewParticipants(List<Participant> newParticipants) {
        IManagerFactory managerFactory = ManagerFactory.getInstance(getContext());
        switch (tournamentType.id) {
            case TournamentTypes.type_individuals:
                for (Participant newParticipant : newParticipants) {
                    PlayerStat individualStat = new PlayerStat(-1, newParticipant.getId());
                    individualStat.setName(newParticipant.getName());
                    List<PlayerStat> participantsPlayerStats = new ArrayList<>();
                    participantsPlayerStats.add(individualStat);
                    newParticipant.setPlayerStats(participantsPlayerStats);

                    setDefaultParticipantStat(newParticipant);

                    long participantId = newParticipant.getParticipantId();
                    String name = newParticipant.getName();
                    ParticipantOverview ps = new ParticipantOverview(-1, participantId, name, 30, (byte) 5);

                    participantStatOverviews.add(ps);
                }
                break;
            case TournamentTypes.type_teams:
                for (Participant newParticipant : newParticipants) {
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

                    setDefaultParticipantStat(newParticipant);

                    long participantId = newParticipant.getParticipantId();
                    String name = newParticipant.getName();
                    ParticipantOverview ps = new ParticipantOverview(-1, participantId, name, 30, (byte) 5);

                    participantStatOverviews.add(ps);
                }
                break;
        }

        matchParticipants.addAll(newParticipants);
    }

    private void setDefaultParticipantStat(Participant participant) {
        ArrayList<ParticipantStat> participantStats = new ArrayList<ParticipantStat>();
        ParticipantStat participantStat = new ParticipantStat(participant.getId(), 0, (byte) 0);
        participantStats.add(participantStat);
        participant.setParticipantStats(participantStats);
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(participantReceiver, new IntentFilter(ParticipantService.ACTION_GET_BY_MATCH_ID_FOR_MANAGING));
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
                case ParticipantService.ACTION_GET_BY_MATCH_ID_FOR_MANAGING: {
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
