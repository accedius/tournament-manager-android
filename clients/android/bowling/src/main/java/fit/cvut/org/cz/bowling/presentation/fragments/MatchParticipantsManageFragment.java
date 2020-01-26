package fit.cvut.org.cz.bowling.presentation.fragments;

import android.app.Activity;
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
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.data.entities.PlayerStat;
import fit.cvut.org.cz.bowling.presentation.activities.AddParticipantsActivity;
import fit.cvut.org.cz.bowling.presentation.activities.SelectTeamPlayersActivity;
import fit.cvut.org.cz.bowling.presentation.adapters.MatchParticipantAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.dialogs.DeleteParticipantDialog;
import fit.cvut.org.cz.bowling.presentation.services.ParticipantService;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManagerFactory;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.Player;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

public class MatchParticipantsManageFragment extends AbstractDataFragment {
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
            IParticipantManager iParticipantManager = iManagerFactory.getEntityManager(Participant.class);
            matchParticipants = iParticipantManager.getByMatchId(matchId);
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
                        DeleteParticipantDialog dialog = DeleteParticipantDialog.newInstance(overview.getParticipantId(), position, overview.getName());
                        dialog.setTargetFragment(thisFragment, RequestCodes.DELETE);
                        dialog.show(getFragmentManager(), "DELETE_PARTICIPANT_DIALOG");
                        return true;
                    }
                });

                buttonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Participant selectedParticipant = matchParticipants.get(position);
                        managedParticipantPosition = position;
                        Intent selectTeamPlayersIntent = SelectTeamPlayersActivity.newStartIntent(getContext(), overview.getParticipantId(), (ArrayList<PlayerStat>) selectedParticipant.getPlayerStats());
                        thisFragment.startActivityForResult(selectTeamPlayersIntent, RequestCodes.MANAGE);
                    }
                });
            }
        };
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
        Intent intent = ParticipantService.newStartIntent(ParticipantService.ACTION_GET_BY_MATCH_ID, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_MATCH_ID));
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return ParticipantService.isWorking(ParticipantService.ACTION_GET_BY_MATCH_ID);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        //aktualizování participantStatOverviews a swapnutí dat
        participantStatOverviews = new ArrayList<>();
        for(int i = 0; i < matchParticipants.size(); i++)
        {
            String name = matchParticipants.get(i).getName();
            ParticipantOverview ps = new ParticipantOverview(-1, matchParticipants.get(i).getParticipantId(),name,i>3?-1:10*i,(byte) i);
            participantStatOverviews.add(ps);
        }

        participantStatOverviews = orderParticipantStats(participantStatOverviews);
        adapter.swapData(participantStatOverviews);
    }

    private class ParticipantStatComparatorByScore implements Comparator<ParticipantOverview>
    {
        @Override
        public int compare(ParticipantOverview o1, ParticipantOverview o2) {
            return o2.getScore()-o1.getScore();
        }
    }
    private List<ParticipantOverview> orderParticipantStats(List<ParticipantOverview> list)
    {
        ParticipantStatComparatorByScore comparatorByScore = new ParticipantStatComparatorByScore();
        Collections.sort(list,comparatorByScore);
        return list;
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(ParticipantService.ACTION_GET_BY_MATCH_ID));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
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
                matchParticipants.remove(position);
                bindDataOnView(null);
                break;
            }
            case RequestCodes.MANAGE: {
                if(resultCode != SelectTeamPlayersActivity.RESULT_OK)
                    return;
                List<Player> teamPlayers = data.getParcelableArrayListExtra(ExtraConstants.EXTRA_DATA);
                setTeamPlayers(teamPlayers);
                bindDataOnView(null);
                break;
            }
        }
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
}