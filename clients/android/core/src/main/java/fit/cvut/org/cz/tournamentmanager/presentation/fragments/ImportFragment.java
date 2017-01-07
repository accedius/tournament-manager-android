package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

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
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tmlibrary.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.ConflictAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.ImportPlayerAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.ImportTournamentAdapter;

/**
 * Fragment for display info about imported Competition.
 */
public class ImportFragment extends Fragment {

    /**
     * Info about imported Tournaments.
     */
    protected ArrayList<TournamentImportInfo> tournaments;
    /**
     * Info about imported Players.
     */
    protected ArrayList<PlayerImportInfo> players;
    /**
     * Info about imported Conflicts.
     */
    protected ArrayList<Conflict> conflicts;

    /**
     * Adapter for Tournaments.
     */
    protected AbstractListAdapter tournamentsAdapter;
    /**
     * Adapter for Players.
     */
    protected AbstractListAdapter playersAdapter;
    /**
     * Adapter for Conflicts.
     */
    protected AbstractListAdapter conflictsAdapter;

    /**
     * Name of package.
     */
    protected String packageName;
    /**
     * Name of sport
     */
    protected String sportContext;
    /**
     * Path to exported service.
     */
    protected String packageService;
    /**
     * File content.
     */
    protected String jsonContent;

    /**
     * ImportFragment creator.
     * @return ImportFragment instance
     */
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
        packageName = args.getString(CrossPackageConstants.EXTRA_PACKAGE);
        sportContext = args.getString(CrossPackageConstants.EXTRA_SPORT_CONTEXT);
        packageService = args.getString(CrossPackageConstants.EXTRA_PACKAGE_SERVICE);
        tournaments = args.getParcelableArrayList(ExtraConstants.EXTRA_TOURNAMENTS);
        players = args.getParcelableArrayList(ExtraConstants.EXTRA_PLAYERS);
        conflicts = args.getParcelableArrayList(ExtraConstants.EXTRA_CONFLICTS);
        jsonContent = args.getString(CrossPackageConstants.EXTRA_JSON);

        tournamentsAdapter = new ImportTournamentAdapter((CompetitionImportInfo)args.get(ExtraConstants.EXTRA_COMPETITION), getResources());
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

    /**
     * Listener for confirm button.
     */
    public void onConfirmClick() {
        HashMap<String, String> playersModified = new HashMap<>();
        for (Conflict c : (ArrayList<Conflict>)conflictsAdapter.getData()) {
            playersModified.put(c.getTitle(), c.getAction());
        }

        Intent intent = new Intent();
        intent.setClassName(packageName, packageService);
        intent.putExtra(CrossPackageConstants.EXTRA_JSON, jsonContent);
        intent.putExtra(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sportContext);
        intent.putExtra(CrossPackageConstants.EXTRA_ACTION, CrossPackageConstants.ACTION_IMPORT_FILE_COMPETITION);
        Bundle b = new Bundle();
        b.putSerializable(CrossPackageConstants.EXTRA_CONFLICTS, playersModified);
        intent.putExtras(b);
        getContext().startService(intent);
        getActivity().finish();
    }
}
