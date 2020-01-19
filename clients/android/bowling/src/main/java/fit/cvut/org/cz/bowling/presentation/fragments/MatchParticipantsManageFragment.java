package fit.cvut.org.cz.bowling.presentation.fragments;

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
import fit.cvut.org.cz.bowling.business.managers.interfaces.IParticipantManager;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.data.entities.ParticipantStat;
import fit.cvut.org.cz.bowling.presentation.activities.AddParticipantsActivity;
import fit.cvut.org.cz.bowling.presentation.adapters.MatchParticipantAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.ParticipantService;
import fit.cvut.org.cz.tmlibrary.business.managers.interfaces.IManager;
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
    private static final String SAVE_PART = "save_part";

    private MatchParticipantAdapter adapter;

    private List<Participant> participants = null;
    private List<ParticipantOverview> participantStats = null;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private ScrollView scrv;
    private long matchId;
    private Fragment thisFragment;
    private TournamentType tournamentType;


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
            participants = savedInstanceState.getParcelableArrayList(SAVE_PART);
        } else {
            IManagerFactory iManagerFactory = ManagerFactory.getInstance();
            IParticipantManager iParticipantManager = iManagerFactory.getEntityManager(Participant.class);
            participants = iParticipantManager.getByMatchId(matchId);
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
        outState.putParcelableArrayList(SAVE_PART, new ArrayList<>(participants));
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
        //aktualizování participantStats a swapnutí dat
        participantStats = new ArrayList<>();
        IManagerFactory iManagerFactory = ManagerFactory.getInstance();
        IManager manager;
        if(tournamentType.equals(TournamentTypes.individuals()))
            manager = iManagerFactory.getEntityManager(Player.class);
        else
            manager = iManagerFactory.getEntityManager(Team.class);
        for(int i = 0; i < participants.size(); i++)
        {
            String name = "";
            if(tournamentType.equals(TournamentTypes.individuals()))
                name = ((Player)manager.getById(participants.get(i).getParticipantId())).getName();
            else
                name = ((Team)manager.getById(participants.get(i).getParticipantId())).getName();
            ParticipantOverview ps = new ParticipantOverview(-1,participants.get(i).getParticipantId(),name,i>3?-1:10*i,(byte) i);
            participantStats.add(ps);
        }

        participantStats = orderParticipantStats(participantStats);
        adapter.swapData(participantStats);
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
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View fragmentView = inflater.inflate(R.layout.fragment_match_participant_wrapper, container, false);

        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.rv_part);
        scrv = (ScrollView) fragmentView.findViewById(R.id.scroll_v);

        adapter = new MatchParticipantAdapter(getContext(), this,tournamentType);

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
                ArrayList<Participant> omitParticipants = new ArrayList<>(participants);
                int option = tournamentType.id == TournamentTypes.type_individuals ? AddParticipantsFragment.OPTION_INDIVIDUALS : AddParticipantsFragment.OPTION_TEAMS;
                Intent intent = AddParticipantsActivity.newStartIntent(getContext(), option, matchId);
                intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_OMIT, omitParticipants);
                thisFragment.startActivityForResult(intent, BowlingFFAMatchStatsFragment.REQUEST_PART);
            }
        });
    }
}
