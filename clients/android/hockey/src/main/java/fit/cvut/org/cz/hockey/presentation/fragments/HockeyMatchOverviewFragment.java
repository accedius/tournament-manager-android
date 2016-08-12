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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.business.entities.MatchScore;
import fit.cvut.org.cz.hockey.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.business.DateFormatFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Fragment for match overview
 * Created by atgot_000 on 22. 4. 2016.
 */
public class HockeyMatchOverviewFragment extends AbstractDataFragment {

    private TextView homeScore, awayScore, round, period, date, note;
    private ImageButton homePlus, homeMinus, awayPlus, awayMinus;
    private CheckBox overtime, shootouts;
    private int intHomeScore = -1, intAwayScore = -1;
    private int ot, so;

    private static String SAVE_HOME_SCORE = "save_home_score";
    private static String SAVE_AWAY_SCORE = "save_away_score";
    private static String SAVE_OVERTIME = "save_overtime";
    private static String SAVE_SHOOTOUTS = "save_shootouts";

    private static String ARG_ID = "arg_id";

    public static HockeyMatchOverviewFragment newInstance(long matchId){
        HockeyMatchOverviewFragment fragment = new HockeyMatchOverviewFragment();

        Bundle args = new Bundle();
        args.putLong(ARG_ID, matchId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void askForData() {
        Intent intent = MatchService.newStartIntent( MatchService.ACTION_FIND_BY_ID_FOR_OVERVIEW, getContext() );
        intent.putExtra(MatchService.EXTRA_ID, getArguments().getLong(ARG_ID));

        getContext().startService(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
        {
            intHomeScore = savedInstanceState.getInt(SAVE_HOME_SCORE);
            intAwayScore = savedInstanceState.getInt(SAVE_AWAY_SCORE);
            ot = savedInstanceState.getInt(SAVE_OVERTIME);
            so = savedInstanceState.getInt(SAVE_SHOOTOUTS);
        }
        else
        {
            intHomeScore = -1;
            intAwayScore = -1;
            ot = -1;
            so = -1;
        }
    }

    @Override
    protected boolean isDataSourceWorking() {
        return MatchService.isWorking( MatchService.ACTION_FIND_BY_ID_FOR_OVERVIEW);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        ScoredMatch match = intent.getParcelableExtra( MatchService.EXTRA_MATCH );

        getActivity().setTitle(getResources().getString(R.string.match_header) + " – " +
                match.getHomeName() + " " + getResources().getString(R.string.vs) + " " + match.getAwayName());

        if( intHomeScore == -1 && intAwayScore == -1) {
            intHomeScore = match.getHomeScore();
            intAwayScore = match.getAwayScore();
        }
        homeScore.setText(String.valueOf(intHomeScore));
        awayScore.setText( String.valueOf(intAwayScore) );
        round.setText(String.valueOf(match.getRound()));
        period.setText( String.valueOf(match.getPeriod()) );
        if( match.getDate()!= null ) {
            DateFormat df = new SimpleDateFormat("dd. MM. yyyy");
            date.setText(df.format(match.getDate()));
        }
        else date.setText( "--" );
        note.setText(match.getNote());

        if( ot == -1 && so == -1 ) {
            MatchScore matchScore = intent.getParcelableExtra(MatchService.EXTRA_MATCH_SCORE);
            if( matchScore == null ){
                ot = 0;
                so = 0;
                return;
            }
            if (matchScore.isOvertime()) {
                overtime.setChecked(true);
                ot = 1;
            }
            else ot = 0;
            if (matchScore.isShootouts()) {
                shootouts.setChecked(true);
                so = 1;
            }
            else so = 0;
        }
        else
        {
            overtime.setChecked( ot != 0 );
            shootouts.setChecked( so != 0 );
        }
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(SAVE_OVERTIME, ot);
        outState.putInt(SAVE_SHOOTOUTS, so);
        outState.putInt(SAVE_HOME_SCORE, intHomeScore);
        outState.putInt(SAVE_AWAY_SCORE, intAwayScore);
    }

    /**
     * Set listeners for all buttons. This method is called in inject view
     */
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
        overtime.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!overtime.isChecked())
                {
                    shootouts.setChecked(false);
                    ot = 0;
                    so = 0;
                }
                if(overtime.isChecked()) ot = 1;
                else ot = 0;
            }
        });
        shootouts.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(shootouts.isChecked()) {
                    so = 1;
                    overtime.setChecked(true);
                    ot = 1;
                }
                else so = 0;
            }
        });
    }

    public MatchScore getScore()
    {
        MatchScore res = new MatchScore( getArguments().getLong(ARG_ID), intHomeScore, intAwayScore, so != 0, ot != 0 );
        return res;
    }
}
