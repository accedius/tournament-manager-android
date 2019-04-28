package fit.cvut.org.cz.bowling.presentation.fragments;

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

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.entities.communication.Constants;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.TournamentAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

import static fit.cvut.org.cz.tmlibrary.business.serialization.Constants.END;

public class BowlingTournamentsListFragment extends AbstractListFragment<Tournament> {
    private long competitionId;
    private String orderColumn = END;
    private String orderType = Constants.ORDER_DESC;

    private TournamentsListReceiver myReceiver = new TournamentsListReceiver();

    public static BowlingTournamentsListFragment newInstance(long id) {
        BowlingTournamentsListFragment fragment = new BowlingTournamentsListFragment();
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_COMP_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null)
            competitionId = getArguments().getLong(ExtraConstants.EXTRA_COMP_ID);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected AbstractListAdapter getAdapter() {
        return new TournamentAdapter(){
            @Override
            protected void setOnClickListeners(View v, long tournamentId, final int position, final String name) {
                final long compId = competitionId;
                final long tourId = tournamentId;

                v.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             /*Intent intent = new Intent(getContext(), ShowTournamentActivity.class);
                                             intent.putExtra(ExtraConstants.EXTRA_COMP_ID, compId);
                                             intent.putExtra(ExtraConstants.EXTRA_TOUR_ID, tourId);
                                             intent.putExtra(AbstractTabActivity.ARG_TABMODE, TabLayout.MODE_SCROLLABLE);
                                             startActivity(intent);*/
                                         }
                                     }
                );
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        /*TournamentsDialog dialog = TournamentsDialog.newInstance(tourId, compId, position, name);
                        dialog.show(getFragmentManager(), "EDIT_DELETE");*/
                        return true;
                    }
                } );
                super.setOnClickListeners(v, tournamentId, position, name);
            }
        };
    }

    @Override
    protected String getDataKey() {
        return ExtraConstants.EXTRA_TOURNAMENTS;
    }

    @Override
    public void askForData() {
        Intent intent = TournamentService.newStartIntent(TournamentService.ACTION_GET_ALL, getContext());
        intent.putExtra(ExtraConstants.EXTRA_COMP_ID, competitionId);
        getActivity().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return TournamentService.isWorking(TournamentService.ACTION_GET_ALL);
    }

    @Override
    protected void registerReceivers() {
        IntentFilter filter = new IntentFilter(TournamentService.ACTION_GET_ALL);
        filter.addAction(TournamentService.ACTION_DELETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(myReceiver, filter);
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(myReceiver);
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
        FloatingActionButton fab = (FloatingActionButton) LayoutInflater.from(getContext()).inflate(R.layout.floatingbutton_add, parent, false);

        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       long compId = getArguments().getLong(ExtraConstants.EXTRA_COMP_ID, -1);
                                       /*Intent intent = CreateTournamentActivity.newStartIntent(getContext(), -1, compId);
                                       startActivity(intent);*/
                                   }
                               }
        );

        return fab;
    }

    public class TournamentsListReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            contentView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            switch (action) {
                case TournamentService.ACTION_GET_ALL: {
                    BowlingTournamentsListFragment.super.bindDataOnView(intent);
                    break;
                }
                case TournamentService.ACTION_DELETE: {
                    if (intent.getBooleanExtra(ExtraConstants.EXTRA_RESULT, false)) {
                        int position = intent.getIntExtra(ExtraConstants.EXTRA_POSITION, -1);
                        adapter.delete(position);
                    } else {
                        View v = getView();
                        if (v != null)
                            Snackbar.make(v, R.string.tournament_not_empty_error, Snackbar.LENGTH_LONG).show();
                    }
                    break;
                }
            }
        }
    }
}
