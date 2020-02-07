package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.Match;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.bowling.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.data.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;


/**
 * Fragment is used in
 */
public class BowlingMatchOverviewFragment extends AbstractDataFragment {
    private TextView round, period, date, note;
    private Long tournament_id;

    public static BowlingMatchOverviewFragment newInstance(long matchId){
        BowlingMatchOverviewFragment fragment = new BowlingMatchOverviewFragment();

        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_ID, matchId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void askForData() {
        Intent intent = MatchService.newStartIntent(MatchService.ACTION_FIND_BY_ID_FOR_OVERVIEW, getContext());
        intent.putExtra(ExtraConstants.EXTRA_ID, getArguments().getLong(ExtraConstants.EXTRA_ID));

        getContext().startService(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_match_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return MatchService.isWorking(MatchService.ACTION_FIND_BY_ID_FOR_OVERVIEW);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        Match match = intent.getParcelableExtra(ExtraConstants.EXTRA_MATCH);
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDisplayDateFormat();

        tournament_id = match.getTournamentId();

        getActivity().setTitle(getResources().getString(fit.cvut.org.cz.tmlibrary.R.string.match) + " – " + (match.getDate()!=null?dateFormat.format(match.getDate()):"[no Date]"));
                //match.getHomeName() + " " + getResources().getString(R.string.vs) + " " + match.getAwayName());

        round.setText(String.valueOf(match.getRound()));
        period.setText(String.valueOf(match.getPeriod()));
        if (match.getDate()!= null) {
            date.setText(DateFormatter.getInstance().getDisplayDateFormat().format(match.getDate()));
        }
        else date.setText("--");
        note.setText(match.getNote());

    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(MatchService.ACTION_FIND_BY_ID_FOR_OVERVIEW));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_match_view, container, false);


        round = (TextView) v.findViewById(R.id.tv_round);
        period = (TextView) v.findViewById(R.id.tv_period);
        date = (TextView) v.findViewById(R.id.tv_date);
        note = (TextView) v.findViewById(R.id.tv_note);


        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public Long getTournamentId() {
        return tournament_id;
    }
}
