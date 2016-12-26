package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.CompetitionImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.Conflict;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.PlayerImportInfo;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.TournamentImportInfo;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageCommunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.activities.ImportActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.ConflictAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.ImportPlayerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.ImportTournamentAdapter;

/**
 * Created by kevin on 28.10.2016.
 */
public class ImportFragment extends Fragment {

    protected ArrayList<TournamentImportInfo> tournaments;
    protected ArrayList<PlayerImportInfo> players;
    protected ArrayList<Conflict> conflicts;

    protected AbstractListAdapter tournamentsAdapter;
    protected AbstractListAdapter playersAdapter;
    protected AbstractListAdapter conflictsAdapter;

    protected String packageName;
    protected String sportService;
    protected String jsonContent;
    protected String sportContext;

    public static ImportFragment newInstance() {
        Bundle args = new Bundle();
        ImportFragment fragment = new ImportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        packageName = args.getString(CrossPackageCommunicationConstants.EXTRA_PACKAGE);
        sportService = args.getString(CrossPackageCommunicationConstants.EXTRA_SPORT_SERVICE);
        tournaments = args.getParcelableArrayList(ImportActivity.TOURNAMENTS);
        players = args.getParcelableArrayList(ImportActivity.PLAYERS);
        conflicts = args.getParcelableArrayList(ImportActivity.CONFLICTS);
        jsonContent = args.getString(CrossPackageCommunicationConstants.EXTRA_JSON);
        sportContext = args.getString(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT);

        tournamentsAdapter = new ImportTournamentAdapter((CompetitionImportInfo)args.get(ImportActivity.COMPETITION), getResources());
        playersAdapter = new ImportPlayerAdapter();
        conflictsAdapter = new ConflictAdapter(getContext());

        tournamentsAdapter.swapData(tournaments);
        playersAdapter.swapData(players);
        conflictsAdapter.swapData(conflicts);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_import, container, false);
        LinearLayoutManager tournamentLayoutManager = new LinearLayoutManager(getActivity());
        tournamentLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager playerLayoutManager = new LinearLayoutManager(getActivity());
        playerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        LinearLayoutManager conflictLayoutManager = new LinearLayoutManager(getActivity());
        conflictLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView tournamentRecyclerView = (RecyclerView) v.findViewById(R.id.import_tournaments);
        tournamentRecyclerView.setLayoutManager(tournamentLayoutManager);
        tournamentRecyclerView.setAdapter(tournamentsAdapter);

        RecyclerView playerRecyclerView = (RecyclerView) v.findViewById(R.id.import_players);
        playerRecyclerView.setLayoutManager(playerLayoutManager);
        playerRecyclerView.setAdapter(playersAdapter);

        RecyclerView conflictRecyclerView = (RecyclerView) v.findViewById(R.id.import_conflicts);
        conflictRecyclerView.setLayoutManager(conflictLayoutManager);
        conflictRecyclerView.setAdapter(conflictsAdapter);

        if (tournaments.isEmpty())
            v.findViewById(R.id.tournaments_header).setVisibility(View.GONE);

        if (players.isEmpty())
            v.findViewById(R.id.players_header).setVisibility(View.GONE);

        if (conflicts.isEmpty())
            v.findViewById(R.id.conflicts_header).setVisibility(View.GONE);

        if (!tournaments.isEmpty() || !players.isEmpty() || !conflicts.isEmpty())
            v.findViewById(R.id.competition_empty).setVisibility(View.GONE);

        Button confirmButton = (Button) v.findViewById(R.id.confirm_import);
        confirmButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                onConfirmClick();
            }
        });
        return v;
    }

    public void onConfirmClick() {
        HashMap<String, String> playersModified = new HashMap<>();
        for (Conflict c : (ArrayList<Conflict>)conflictsAdapter.getData()) {
            playersModified.put(c.getTitle(), c.getAction());
        }

        Intent intent = new Intent();
        intent.setClassName(packageName, sportService);
        intent.putExtra(CrossPackageCommunicationConstants.EXTRA_JSON, jsonContent);
        intent.putExtra(CrossPackageCommunicationConstants.EXTRA_SPORT_CONTEXT, sportContext);
        intent.putExtra(CrossPackageCommunicationConstants.EXTRA_ACTION, CrossPackageCommunicationConstants.ACTION_IMPORT_FILE_COMPETITION);
        Bundle b = new Bundle();
        b.putSerializable(CrossPackageCommunicationConstants.EXTRA_CONFLICTS, playersModified);
        intent.putExtras(b);
        getContext().startService(intent);
        getActivity().finish();
    }
}
