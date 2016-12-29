package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.entities.communication.Constants;
import fit.cvut.org.cz.squash.presentation.activities.CreateTournamentActivity;
import fit.cvut.org.cz.squash.presentation.activities.TournamentDetailActivity;
import fit.cvut.org.cz.squash.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.squash.presentation.dialogs.TournamentsDialog;
import fit.cvut.org.cz.squash.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.TournamentAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

import static fit.cvut.org.cz.tmlibrary.business.serialization.Constants.END;

/**
 * Allows usser to display tournaments in competition
 * Created by Vaclav on 5. 4. 2016.
 */
public class TournamentsListFragment extends AbstractListFragment<Tournament> {
    private CompetitionType type = null;
    private TournamentAdapter adapter = null;

    private String orderColumn = END;
    private String orderType = Constants.ORDER_DESC;

    public static TournamentsListFragment newInstance(long competitionId){
        TournamentsListFragment fragment = new TournamentsListFragment();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_COMP_ID, competitionId);
        fragment.setArguments(args);
        return fragment;
    }

    public TournamentsListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_sorting, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        adapter =  new TournamentAdapter(){
            @Override
            protected void setOnClickListeners(View v, final long tournamentId, final int position, final String name) {
                final Context c = getContext();

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(c, TournamentDetailActivity.class);
                        intent.putExtra(ExtraConstants.EXTRA_TOUR_ID, tournamentId);
                        intent.putExtra(ExtraConstants.EXTRA_COMP_ID, getArguments().getLong(ExtraConstants.EXTRA_COMP_ID));
                        intent.putExtra(AbstractTabActivity.ARG_TABMODE, TabLayout.MODE_SCROLLABLE);
                        intent.putExtra(ExtraConstants.EXTRA_TYPE, type.id);

                        startActivity(intent);
                    }
                });

                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        TournamentsDialog dialog = TournamentsDialog.newInstance(getArguments().getLong(ExtraConstants.EXTRA_COMP_ID), tournamentId, position, name);
                        dialog.show(getFragmentManager(), "EDIT_DELETE");

                        return false;
                    }
                });
            }
        };

        return adapter;
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_TOURNAMENT;
    }

    @Override
    public void askForData() {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_GET_BY_COMPETITION_ID, getContext());
        if (getArguments() != null)
            intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_COMP_ID));

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TournamentService.isWorking(TournamentService.ACTION_GET_BY_COMPETITION_ID);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(TournamentService.ACTION_GET_BY_COMPETITION_ID);
        filter.addAction(TournamentService.ACTION_DELETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(tReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(tReceiver);
    }

    public void orderData(final String type) {
        if (adapter == null) return;

        List<Tournament> tournaments = adapter.getData();
        if (orderColumn.equals(type) && orderType.equals(Constants.ORDER_ASC)) {
            orderType = Constants.ORDER_DESC;
            Collections.sort(tournaments, new Comparator<Tournament>() {
                @Override
                public int compare(Tournament ls, Tournament rs) {
                    return rs.getColumn(type).compareToIgnoreCase(ls.getColumn(type));
                }
            });
        } else {
            if (!orderColumn.equals(type)) {
                orderColumn = type;
            }
            orderType = Constants.ORDER_ASC;
            Collections.sort(tournaments, new Comparator<Tournament>() {
                @Override
                public int compare(Tournament ls, Tournament rs) {
                    return ls.getColumn(type).compareToIgnoreCase(rs.getColumn(type));
                }
            });
        }

        adapter.swapData(tournaments);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected FloatingActionButton getFAB(ViewGroup parent) {
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.fab_add, parent, false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long cId = getArguments().getLong(ExtraConstants.EXTRA_COMP_ID, -1);
                Intent intent = CreateTournamentActivity.newStartIntent(getContext(), cId, -1);
                startActivity(intent);
            }
        });

        return fab;
    }
    private BroadcastReceiver tReceiver = new TournamentsReceiver();
    public class TournamentsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressBar.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
            if (intent.getAction().equals(TournamentService.ACTION_GET_BY_COMPETITION_ID)) {
                TournamentsListFragment.super.bindDataOnView(intent);
                type = CompetitionTypes.competitionTypes(getResources())[intent.getIntExtra(ExtraConstants.EXTRA_TYPE, 0)];
            } else if (intent.getBooleanExtra(ExtraConstants.EXTRA_RESULT, false)) {
                    int position = intent.getIntExtra(ExtraConstants.EXTRA_POSITION, -1);
                    adapter.delete(position);
            } else {
                Snackbar.make(contentView, fit.cvut.org.cz.tmlibrary.R.string.failDeleteTournament, Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
