package fit.cvut.org.cz.hockey.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.MatchScore;
import fit.cvut.org.cz.hockey.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.business.DateFormatFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Created by atgot_000 on 22. 4. 2016.
 */
public class HockeyMatchOverviewFragment extends AbstractDataFragment {

    private TextView homeName, awayName, homeScore, awayScore, round, period, date, note;
    private ImageButton homePlus, homeMinus, awayPlus, awayMinus;
    private CheckBox overtime, shootouts;
    private int intHomeScore, intAwayScore;

    private static String ARG_ID = "arg_id";

    public static HockeyMatchOverviewFragment newInstance(long matchId){
        HockeyMatchOverviewFragment fragment = new HockeyMatchOverviewFragment();

        Bundle args = new Bundle();
        args.putLong(ARG_ID, matchId);

        fragment.setArguments( args );
        return fragment;
    }

    @Override
    protected void askForData() {
        Intent intent = MatchService.newStartIntent( MatchService.ACTION_FIND_BY_ID_FOR_OVERVIEW, getContext() );
        intent.putExtra(MatchService.EXTRA_ID, getArguments().getLong(ARG_ID));

        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return MatchService.isWorking( MatchService.ACTION_FIND_BY_ID_FOR_OVERVIEW);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        ScoredMatch match = intent.getParcelableExtra( MatchService.EXTRA_MATCH );

        intHomeScore = match.getHomeScore();
        intAwayScore = match.getAwayScore();
        homeName.setText( match.getHomeName() );
        awayName.setText( match.getAwayName() );
        homeScore.setText( String.valueOf(intHomeScore) );
        awayScore.setText( String.valueOf(intAwayScore) );
        round.setText( String.valueOf(match.getRound()) );
        period.setText( String.valueOf(match.getPeriod()) );
        date.setText(DateFormatFactory.getInstance().getDateFormat().format(match.getDate()));
        note.setText( match.getNote() );

        MatchScore matchScore = intent.getParcelableExtra( MatchService.EXTRA_MATCH_SCORE );

        if( matchScore.isOvertime() ) overtime.setChecked( true );
        if( matchScore.isShootouts()) shootouts.setChecked( true );
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver( receiver, new IntentFilter(MatchService.ACTION_FIND_BY_ID_FOR_OVERVIEW));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_match_view, container, false);

        homeName = (TextView) v.findViewById(R.id.tv_home);
        awayName = (TextView) v.findViewById(R.id.tv_away);
        homeScore = (TextView) v.findViewById(R.id.tv_home_score);
        awayScore = (TextView) v.findViewById(R.id.tv_away_score);
        round = (TextView) v.findViewById(R.id.tv_round);
        period = (TextView) v.findViewById(R.id.tv_period);
        date = (TextView) v.findViewById(R.id.tv_date);
        note = (TextView) v.findViewById(R.id.tv_note);

        homePlus = (ImageButton) v.findViewById(R.id.bt_ht_plus);
        awayPlus = (ImageButton) v.findViewById(R.id.bt_at_plus);
        homeMinus = (ImageButton) v.findViewById(R.id.bt_ht_minus);
        awayMinus = (ImageButton) v.findViewById(R.id.bt_at_minus);

        overtime = (CheckBox) v.findViewById(R.id.cb_overtime);
        shootouts = (CheckBox) v.findViewById(R.id.cb_shootouts);

        setOnClickListeners();

        return v;
    }

    private void setOnClickListeners()
    {
        homePlus.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intHomeScore++;
                homeScore.setText( String.valueOf( intHomeScore ) );
            }
        });
        awayPlus.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intAwayScore++;
                awayScore.setText( String.valueOf( intAwayScore ) );
            }
        });
        homeMinus.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(intHomeScore <= 0) return;
                intHomeScore--;
                homeScore.setText( String.valueOf( intHomeScore ) );
            }
        });
        awayMinus.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(intAwayScore <= 0) return;
                intAwayScore--;
                awayScore.setText( String.valueOf( intAwayScore ) );
            }
        });
    }
}
