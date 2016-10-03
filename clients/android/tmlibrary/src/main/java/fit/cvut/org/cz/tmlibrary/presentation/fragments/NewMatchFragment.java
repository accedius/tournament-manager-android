package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.business.entities.MatchParticipant;
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
     * @param tournamentId id of tournament
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
    private Calendar dDate = null;
    protected long id = -1, tournamentId = -1;
    private EditText mDate, period, round, note;

    private ArrayAdapter<MatchParticipant> homePartAdapter, awayPartAdapter;

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_new_match, container, false);

        mDate = (EditText) v.findViewById(R.id.et_date);
        period = (EditText) v.findViewById(R.id.et_period);
        round = (EditText) v.findViewById(R.id.et_round);
        note = (EditText) v.findViewById(R.id.et_note);
        homeTeamSpinner = (AppCompatSpinner) v.findViewById(R.id.m_home);
        awayTeamSpinner = (AppCompatSpinner) v.findViewById(R.id.m_away);

        if (getArguments() != null) {
            id = getArguments().getLong(ARG_ID, -1);
            tournamentId = getArguments().getLong(ARG_TOUR_ID, -1);
        }
        mDate.setKeyListener(null);

        //if the match is created, we do not allow changing teams
        if (id != -1) {
            homeTeamSpinner.setEnabled(false);
            awayTeamSpinner.setEnabled(false);
        } else {
            setDatepicker(Calendar.getInstance());
        }

        return v;
    }

    public ScoredMatch getMatch() {
        int sPeriod, sRound;
        try {
            sPeriod = Integer.valueOf(period.getText().toString());
            sRound = Integer.valueOf(round.getText().toString());
        } catch(NumberFormatException ex) {
            return null;
        }
        Date sDate = null;
        if (dDate != null)
            sDate = dDate.getTime();
        long homeTeamId = ((MatchParticipant) homeTeamSpinner.getSelectedItem()).getParticipantId();
        long awayTeamId = ((MatchParticipant) awayTeamSpinner.getSelectedItem()).getParticipantId();

        ScoredMatch sm = new ScoredMatch(id, tournamentId, null, sDate, false, note.getText().toString(), sPeriod, sRound);
        if (id == -1) {
            sm.setHomeParticipantId(homeTeamId);
            sm.setAwayParticipantId(awayTeamId);
        }

        return sm;
    }

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

    @Override
    protected void bindDataOnView(Intent intent) {
        ScoredMatch smatch = null;
        if (id != -1) {
            smatch = intent.getParcelableExtra(getMatchKey());
            bindMatchOnView(smatch);
        }
        ArrayList<MatchParticipant> participants = intent.getParcelableArrayListExtra(getTournamentParticipantsKey());
        bindParticipantsOnView(participants, smatch);
    }

    private void bindMatchOnView(ScoredMatch match) {
        Calendar argDate = Calendar.getInstance();
        if (match.getDate() != null) {
            mDate.setText(DateFormatter.getInstance().getDisplayDateFormat().format(match.getDate()));
            dDate = Calendar.getInstance();
            dDate.setTime(match.getDate());
            argDate = dDate;
        }

        setDatepicker(argDate);
        period.setText(Integer.toString(match.getPeriod()));
        round.setText(Integer.toString(match.getRound()));
        note.setText(match.getNote());
    }

    private void bindParticipantsOnView(ArrayList<MatchParticipant> participants, ScoredMatch match) {
        homePartAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, participants);
        homePartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        homeTeamSpinner.setAdapter(homePartAdapter);

        awayPartAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, participants);
        awayPartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        awayTeamSpinner.setAdapter(awayPartAdapter);

        if (match != null) {
            int hIndex = homePartAdapter.getPosition(findParticipant(participants, match.getHomeParticipantId()));
            int aIndex = awayPartAdapter.getPosition(findParticipant(participants, match.getAwayParticipantId()));

            homeTeamSpinner.setSelection(hIndex);
            awayTeamSpinner.setSelection(aIndex);
        }
    }

    private MatchParticipant findParticipant(ArrayList<MatchParticipant> participants, long id) {
        for (MatchParticipant part : participants)
            if (part.getParticipantId() == id)
                return part;

        return null;
    }

    // Set Datepicker date
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
                            mDate.setText(String.format("%d. %d. %d", dayOfMonth, monthOfYear + 1, year));
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
