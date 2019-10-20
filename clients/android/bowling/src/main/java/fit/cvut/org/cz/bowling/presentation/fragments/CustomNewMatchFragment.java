package fit.cvut.org.cz.bowling.presentation.fragments;

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

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.business.ManagerFactory;
import fit.cvut.org.cz.bowling.business.managers.TeamManager;
import fit.cvut.org.cz.tmlibrary.data.entities.Match;
import fit.cvut.org.cz.tmlibrary.data.entities.Participant;
import fit.cvut.org.cz.tmlibrary.data.entities.ParticipantType;
import fit.cvut.org.cz.tmlibrary.data.entities.Team;
import fit.cvut.org.cz.tmlibrary.data.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.DatePickerDialogFragment;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewMatchFragment;

/**
 * Fragment for create or edit Match.
 */
public abstract class CustomNewMatchFragment extends AbstractDataFragment {
    /**
     * Constructor for this fragment with id of competition that needs to update
     * @param tournamentId id of tournament
     * @return fragment instance
     */
    public static CustomNewMatchFragment newInstance(long id, long tournamentId, Class<? extends CustomNewMatchFragment> clazz) {
        CustomNewMatchFragment fragment = constructNewInstance(clazz);
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_ID, id);
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, tournamentId);

        fragment.setArguments(args);
        return fragment;
    }

    public static CustomNewMatchFragment newInstance(long tournamentId, Class<? extends CustomNewMatchFragment> clazz) {
        CustomNewMatchFragment fragment = constructNewInstance(clazz);
        Bundle args = new Bundle();
        args.putLong(ExtraConstants.EXTRA_TOUR_ID, tournamentId);

        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates new instance of clazz
     * @param clazz     class based on CustomNewMatchFragment
     * @return          instance of clazz
     */
    private static CustomNewMatchFragment constructNewInstance(Class<? extends CustomNewMatchFragment> clazz) {
        CustomNewMatchFragment fragment = null;
        try {
            Constructor<? extends CustomNewMatchFragment> c = clazz.getConstructor();
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
        return fragment;
    }

    private Calendar dDate = null;
    protected long id = -1, tournamentId = -1;
    private EditText mDate, period, round, note;


    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_new_ffa_match, container, false);

        mDate = (EditText) v.findViewById(fit.cvut.org.cz.tmlibrary.R.id.et_date);
        period = (EditText) v.findViewById(fit.cvut.org.cz.tmlibrary.R.id.et_period);
        round = (EditText) v.findViewById(fit.cvut.org.cz.tmlibrary.R.id.et_round);
        note = (EditText) v.findViewById(fit.cvut.org.cz.tmlibrary.R.id.et_note);

        if (getArguments() != null) {
            id = getArguments().getLong(ExtraConstants.EXTRA_ID, -1);
            tournamentId = getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID, -1);
        }
        mDate.setKeyListener(null);

        if(id == -1) {
            setDatepicker(Calendar.getInstance());
        }

        return v;
    }

    public Match getMatch() {
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

        //return new Match(id, tournamentId, sDate, false, note.getText().toString(), sPeriod, sRound);


        long participantId = 0;


        Match sm = new Match(id, tournamentId, sDate, false, note.getText().toString(), sPeriod, sRound);
        if (id == -1) {sm.addParticipant(new Participant(id, participantId, ParticipantType.home.toString())); }

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
        Match smatch = null;
        if (id != -1) {
            smatch = intent.getParcelableExtra(getMatchKey());
            bindMatchOnView(smatch);
        }
        ArrayList<Participant> participants = intent.getParcelableArrayListExtra(getTournamentParticipantsKey());
        bindParticipantsOnView(participants, smatch);
    }

    private void bindMatchOnView(Match match) {
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

    private void bindParticipantsOnView(ArrayList<Participant> participants, Match match) {
        //TODO figure out what this is
    }

    private Participant findParticipant(ArrayList<Participant> participants, long id) {
        for (Participant participant : participants)
            if (participant.getParticipantId() == id)
                return participant;

        return null;
    }

    // Set Datepicker date
    private void setDatepicker(final Calendar date) {
        mDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Bundle b = new Bundle();
                    b.putInt(ExtraConstants.EXTRA_YEAR, date.get(Calendar.YEAR));
                    b.putInt(ExtraConstants.EXTRA_MONTH, date.get(Calendar.MONTH));
                    b.putInt(ExtraConstants.EXTRA_DAY, date.get(Calendar.DAY_OF_MONTH));
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