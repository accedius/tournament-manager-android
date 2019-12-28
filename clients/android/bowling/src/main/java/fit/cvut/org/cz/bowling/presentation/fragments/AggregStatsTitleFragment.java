package fit.cvut.org.cz.bowling.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.entities.communication.Constants;
import fit.cvut.org.cz.bowling.business.managers.CompetitionManager;
import fit.cvut.org.cz.bowling.business.managers.TournamentManager;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.TournamentService;
import fit.cvut.org.cz.tmlibrary.data.entities.Competition;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Fragment is used in
 */
public class AggregStatsTitleFragment extends Fragment {
    private BowlingPlayersStatsFragment statsFragment;

    public static AggregStatsTitleFragment newInstance(long id, boolean forComp) {
        AggregStatsTitleFragment fragment = new AggregStatsTitleFragment();
        Bundle args = new Bundle();

        if (forComp)
            args.putLong(ExtraConstants.EXTRA_COMP_ID, id);
        else
            args.putLong(ExtraConstants.EXTRA_TOUR_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_stats_title, container, false);
        setOrderingListeners(v);
        setDefaultOrder(v);
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Long id = (Long) getArguments().get(ExtraConstants.EXTRA_COMP_ID);

        boolean isTeamMatch = false;

        if(id != null) {
            final CompetitionManager cm = ManagerFactory.getInstance().getEntityManager(Competition.class);
            final Competition c = cm.getById(id);
            final CompetitionType ct = CompetitionTypes.getTypeByTypeId(c.getTypeId());

            if(c == null) {
                Log.e("AggregStatsTitleFragment","Competition " + id + " not found");
            } else {
                isTeamMatch = CompetitionTypes.teams().equals(ct);
            }
        } else if((id = (Long)getArguments().get(ExtraConstants.EXTRA_TOUR_ID)) != null) {
            final TournamentManager tm = ManagerFactory.getInstance().getEntityManager(Tournament.class);
            final Tournament t = tm.getById(id);
            final TournamentType tt = TournamentTypes.getMyTournamentType(t.getTypeId());

            if(t == null) {
                Log.e("AggregStatsTitleFragment","Tournament " + id + " not found");
            } else {
                isTeamMatch = TournamentTypes.teams().equals(tt);
            }
        }

        if(!isTeamMatch) {
            TextView tp = view.findViewById(R.id.stats_team_points);
            if(tp != null) {
                tp.setText(R.string.stat_match_point);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Long competitionID = getArguments().getLong(ExtraConstants.EXTRA_COMP_ID, -1);
        Long tournamentID = getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID, -1);

        if (competitionID != -1) {
            statsFragment = BowlingPlayersStatsFragment.newInstance(competitionID, true);
        } else {
            statsFragment = BowlingPlayersStatsFragment.newInstance(tournamentID, false);
        }

        if (getChildFragmentManager().findFragmentById(R.id.stats_list) == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.stats_list, statsFragment)
                    .commit();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_help, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public void refresh(){
        Fragment fr = getChildFragmentManager().findFragmentById(R.id.stats_list);
        if (fr != null && fr instanceof AbstractDataFragment) {
            ((AbstractDataFragment) fr).customOnResume();
            setDefaultOrder(getView());
        }
    }


    private void setDefaultOrder(View v) {
        deleteOtherOrders(v);
        TextView points = (TextView)v.findViewById(R.id.stats_points);
        points.setText(points.getText() + " "+Constants.DESC_SIGN);
    }

    private void deleteOtherOrders(View v) {
        HashMap<String, TextView> columns = getColumns(v);
        for (TextView textView : columns.values()) {
            if (textView.getText().toString().contains(Constants.DESC_SIGN) || textView.getText().toString().contains(Constants.ASC_SIGN)) {
                String text = textView.getText().toString();
                textView.setText(text.substring(0, text.length()-2));
            }
        }
    }

    private void setOrderingListeners(View v) {
        final HashMap<String, TextView> columns = getColumns(v);
        for (final Map.Entry<String, TextView> e : columns.entrySet()) {
            e.getValue().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    statsFragment.orderData(e.getKey(), columns);
                }
            });
        }
    }

    private HashMap<String, TextView> getColumns(View v) {
        HashMap<String, TextView> columns = new HashMap<>();
        columns.put(Constants.MATCHES,(TextView)v.findViewById(R.id.stats_games_played));
        columns.put(Constants.STRIKES, (TextView)v.findViewById(R.id.stats_strikes));
        columns.put(Constants.SPARES, (TextView)v.findViewById(R.id.stats_spares));
        columns.put(Constants.POINTS, (TextView)v.findViewById(R.id.stats_points));
        if (v.findViewById(R.id.stats_team_points) != null) {
            columns.put(Constants.TEAM_POINTS, (TextView) v.findViewById(R.id.stats_team_points));
            columns.put(Constants.STRIKES_AVG, (TextView) v.findViewById(R.id.stats_strikes_avg));
            columns.put(Constants.POINTS_AVG, (TextView) v.findViewById(R.id.stats_points_avg));
            columns.put(Constants.TEAM_POINTS_AVG, (TextView) v.findViewById(R.id.stats_team_points_avg));
        }
        return columns;
    }
}
