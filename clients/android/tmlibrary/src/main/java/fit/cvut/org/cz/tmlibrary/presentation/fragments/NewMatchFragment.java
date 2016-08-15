package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.DateFormatFactory;
import fit.cvut.org.cz.tmlibrary.business.entities.NewMatchSpinnerParticipant;
import fit.cvut.org.cz.tmlibrary.business.entities.ScoredMatch;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.DatePickerDialogFragment;

/**
 * Created by atgot_000 on 10. 4. 2016.
 */
public abstract class NewMatchFragment extends AbstractDataFragment  {

    private static final String ARG_ID = "arg_id";
    private static final String ARG_TOUR_ID = "arg_tour_id";

    /**
     * Constructor for this fragment with id of competition that needs to update
     * @param id
     * @param clazz
     * @return
     */
    public static NewMatchFragment newInstance(long id, long tournamentId, Class<? extends NewMatchFragment> clazz){
        NewMatchFragment fragment = null;
        try {
            Constructor<? extends NewMatchFragment> c = clazz.getConstructor();
            fragment = c.newInstance();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putLong(ARG_TOUR_ID, tournamentId);

        fragment.setArguments(args);
        return fragment;
    }

    public static NewMatchFragment newInstance(long tournamentId, Class<? extends NewMatchFragment> clazz){
        NewMatchFragment fragment = null;
        try {
            Constructor<? extends NewMatchFragment> c = clazz.getConstructor();
            fragment = c.newInstance();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Bundle args = new Bundle();
        args.putLong(ARG_TOUR_ID, tournamentId);

        fragment.setArguments(args);
        return fragment;
    }

    private AppCompatSpinner homeTeamSpinner, awayTeamSpinner;
    private FloatingActionButton fab;
    private Calendar dDate = null;
    protected long id = -1, tournamentId = -1;
    private EditText mDate, period, round, note;

    private ArrayAdapter<NewMatchSpinnerParticipant> homePartAdapter, awayPartAdapter;

    private ScoredMatch ourMatch = null;

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_new_match, container, false);

        mDate = (EditText) v.findViewById( R.id.et_date );
        period = (EditText) v.findViewById( R.id.et_period );
        round = (EditText) v.findViewById( R.id.et_round );
        note = (EditText) v.findViewById( R.id.et_note );
        homeTeamSpinner = (AppCompatSpinner) v.findViewById(R.id.m_home);
        awayTeamSpinner = (AppCompatSpinner) v.findViewById(R.id.m_away);

        if (getArguments() != null) {
            id = getArguments().getLong(ARG_ID, -1 );
            tournamentId = getArguments().getLong( ARG_TOUR_ID, -1 );
        }

        //if the match is created, we do not allow changing teams
        if (id != -1) {
            homeTeamSpinner.setEnabled( false );
            awayTeamSpinner.setEnabled( false );
            setDatepicker(Calendar.getInstance());
        }

        fab = (FloatingActionButton) v.findViewById(R.id.fab_edit);

        mDate.setKeyListener( null );

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDataSourceWorking() && validate(v)) {
                    Date sDate = null;
                    if (dDate != null) sDate = dDate.getTime();
                    int sPeriod = Integer.valueOf(period.getText().toString());
                    int sRound = Integer.valueOf(round.getText().toString());
                    long homeTeamId = ((NewMatchSpinnerParticipant) homeTeamSpinner.getSelectedItem()).getParticipantId();
                    long awayTeamId = ((NewMatchSpinnerParticipant) awayTeamSpinner.getSelectedItem()).getParticipantId();

                    if (id == -1) {
                        ScoredMatch match = new ScoredMatch();
                        match.setHomeParticipantId(homeTeamId);
                        match.setAwayParticipantId(awayTeamId);
                        match.setPeriod(sPeriod);
                        match.setRound(sRound);
                        match.setNote(note.getText().toString());
                        match.setDate(sDate);
                        match.setTournamentId( tournamentId );
                        saveMatch(match);
                    } else {
                        ScoredMatch match = ourMatch;
                        match.setId( id );
                        match.setPeriod(sPeriod);
                        match.setRound(sRound);
                        match.setNote(note.getText().toString());
                        match.setDate(sDate);
                        match.setTournamentId( tournamentId );
                        updateMatch(match);
                    }
                    getActivity().finish();
                }
            }
        });

        return v;
    }

    /**
     * Called with new ScoredMatch in param
     * @param match
     */
    protected abstract void saveMatch( ScoredMatch match );

    /**
     * Called when match should be updated
     * @param match
     */
    protected abstract void updateMatch( ScoredMatch match );

    /**
     *
     * @return String key of match received in Bundle from datasource
     */
    protected abstract String getMatchKey();

    /**
     *
     * @return String key of tournament participants received  in Bundle from datasource
     */
    protected abstract String getTournamentParticipantsKey();

    private boolean validate( View v ) {
        if( period.getText().toString().isEmpty() || round.getText().toString().isEmpty() ) {
            Snackbar.make(v, R.string.invalidRoundPeriod, Snackbar.LENGTH_LONG).show();
            return false;
        }

        if( homeTeamSpinner.getSelectedItemId() == awayTeamSpinner.getSelectedItemId() ) {
            Snackbar.make(v, R.string.invalidParticipants, Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        ScoredMatch smatch = null;
        if( id != -1 ) {
            smatch = intent.getParcelableExtra(getMatchKey());
            bindMatchOnView(smatch);
            ourMatch = smatch;
        }
        ArrayList<NewMatchSpinnerParticipant> participants = intent.getParcelableArrayListExtra( getTournamentParticipantsKey() );
        bindParticipantsOnView( participants, smatch );
    }

    private void bindMatchOnView( ScoredMatch match ) {
        if (match.getDate() != null){
            mDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(match.getDate()));
            dDate = Calendar.getInstance();
            dDate.setTime(match.getDate());
        }

        setDatepicker(dDate);
        period.setText( Integer.toString( match.getPeriod() ) );
        round.setText(Integer.toString(match.getRound()));
        note.setText(match.getNote());
    }

    private void bindParticipantsOnView( ArrayList<NewMatchSpinnerParticipant> participants, ScoredMatch match ) {
        homePartAdapter = new ArrayAdapter<NewMatchSpinnerParticipant>(getContext(), android.R.layout.simple_spinner_item, participants );
        homePartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        homeTeamSpinner.setAdapter( homePartAdapter );

        awayPartAdapter = new ArrayAdapter<NewMatchSpinnerParticipant>(getContext(), android.R.layout.simple_spinner_item, participants );
        awayPartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        awayTeamSpinner.setAdapter(awayPartAdapter);

        if( match != null ) {
            int hIndex = homePartAdapter.getPosition(findParticipant(participants, match.getHomeParticipantId()));
            int aIndex = awayPartAdapter.getPosition(findParticipant(participants, match.getAwayParticipantId()));

            homeTeamSpinner.setSelection(hIndex);
            awayTeamSpinner.setSelection(aIndex);
        }
    }

    private NewMatchSpinnerParticipant findParticipant( ArrayList<NewMatchSpinnerParticipant> participants, long id ) {
        for( NewMatchSpinnerParticipant part : participants )
            if( part.getParticipantId() == id )
                return part;

        return null;
    }

    // Set Datepicker dates to Tournament start and end
    private void setDatepicker(final Calendar date) {
        mDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Bundle b = new Bundle();
                    b.putInt("y", date.get(Calendar.YEAR));
                    b.putInt("m", date.get(Calendar.MONTH));
                    b.putInt("d", date.get(Calendar.DAY_OF_MONTH));
                    DatePickerDialogFragment fragment = new DatePickerDialogFragment();
                    fragment.setArguments(b);
                    fragment.listener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mDate.setText(String.format("%d.%d.%d", dayOfMonth, monthOfYear + 1, year));
                            dDate = Calendar.getInstance();
                            dDate.set(Calendar.YEAR, year);
                            dDate.set(Calendar.MONTH, monthOfYear);
                            dDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        }
                    };
                    fragment.show(getActivity().getSupportFragmentManager(), "TAG");
                }
            }
        });
    }

}
