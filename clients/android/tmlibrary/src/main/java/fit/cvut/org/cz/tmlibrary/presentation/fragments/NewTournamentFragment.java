package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.DatePickerDialogFragment;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public abstract class NewTournamentFragment extends AbstractDataFragment {
    private static final String ARG_TOUR_ID = "arg_tour_id";
    private static final String ARG_COMP_ID = "arg_comp_id";

    /**
     *
     * @param tourId id of tournament
     * @param compId id of competition
     * @param clazz class of child fragment
     * @return
     */
    public static NewTournamentFragment newInstance(long tourId, long compId, Class<? extends NewTournamentFragment> clazz){
        NewTournamentFragment fragment = null;
        try {
            Constructor<? extends NewTournamentFragment> c = clazz.getConstructor();
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

        args.putLong(ARG_TOUR_ID, tourId);
        args.putLong(ARG_COMP_ID, compId);

        fragment.setArguments(args);
        return fragment;
    }

    private EditText note, name, startDate, endDate;
    private Calendar dStartDate = null, dEndDate = null;
    protected long tournamentId = -1;
    protected long competitionId = -1;

    private Tournament tournament = null;

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_new_tournament, container, false);

        note = (EditText) v.findViewById(R.id.et_note);
        name = (EditText) v.findViewById(R.id.et_name);
        startDate = (EditText) v.findViewById(R.id.et_startDate);
        endDate = (EditText) v.findViewById(R.id.et_endDate);
        //tilNote = (TextInputLayout) v.findViewById(R.id.til_note);

        if (getArguments() != null) {
            tournamentId = getArguments().getLong(ARG_TOUR_ID, -1);
            competitionId = getArguments().getLong(ARG_COMP_ID, -1);
        }

        if (tournamentId == -1) {
            setDatepicker(Calendar.getInstance(), Calendar.getInstance());
        }

        //We don't want user to write into editTexts
        startDate.setKeyListener(null);
        endDate.setKeyListener(null);

        return v;
    }

    private boolean validate(View v){
        if (name.getText().toString().isEmpty()) {
            Snackbar.make(v, R.string.invalidName, Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (competitionId == -1 && tournamentId == -1) {
            Snackbar.make(v, R.string.tournament_id_invalid, Snackbar.LENGTH_LONG).show();
            return false;
        }

        Snackbar.make(v, R.string.tournament_created, Snackbar.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void customOnResume() {
        if (tournamentId != -1)
            super.customOnResume();
    }

    @Override
    protected void customOnPause() {
        if (tournamentId != -1)
            super.customOnPause();
    }

    /**
     * Called when tournament in param needs to be created
     * @param t
     */
    protected abstract void saveTournament(Tournament t);

    /**
     * Called when tournament in param needs to be updated
     * @param t
     */
    protected abstract void updateTournament(Tournament t);

    /**
     *
     * @return String key of tournament received in Bundle from datasource
     */
    protected abstract String getTournamentKey();

    @Override
    protected void bindDataOnView(Intent intent) {
        tournament = intent.getParcelableExtra(getTournamentKey());
        bindTournamentOnView(tournament);
    }

    private void bindTournamentOnView(Tournament t) {
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDisplayDateFormat();
        Calendar argStart = Calendar.getInstance();
        Calendar argEnd = Calendar.getInstance();

        name.setText(t.getName());
        if (t.getStartDate() != null) {
            startDate.setText(dateFormat.format(t.getStartDate()));
            dStartDate = Calendar.getInstance();
            dStartDate.setTime(t.getStartDate());
            argStart = dStartDate;
        }
        if (t.getEndDate() != null) {
            endDate.setText(dateFormat.format(t.getEndDate()));
            dEndDate = Calendar.getInstance();
            dEndDate.setTime(t.getEndDate());
            argEnd = dEndDate;
        }
        note.setText(t.getNote());
        setDatepicker(argStart, argEnd);
    }

    // Set Datepicker dates to Tournament start and end
    private void setDatepicker(final Calendar start, final Calendar end) {
        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Bundle b = new Bundle();
                    b.putInt("y", start.get(Calendar.YEAR));
                    b.putInt("m", start.get(Calendar.MONTH));
                    b.putInt("d", start.get(Calendar.DAY_OF_MONTH));
                    DatePickerDialogFragment fragment = new DatePickerDialogFragment();
                    fragment.setArguments(b);
                    fragment.listener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            startDate.setText(String.format("%d. %d. %d", dayOfMonth, monthOfYear + 1, year));
                            dStartDate = Calendar.getInstance();
                            dStartDate.set(Calendar.YEAR, year);
                            dStartDate.set(Calendar.MONTH, monthOfYear);
                            dStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        }
                    };
                    fragment.show(getActivity().getSupportFragmentManager(), "TAG");
                }
            }
        });

        endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Bundle b = new Bundle();
                    b.putInt("y", end.get(Calendar.YEAR));
                    b.putInt("m", end.get(Calendar.MONTH));
                    b.putInt("d", end.get(Calendar.DAY_OF_MONTH));
                    DatePickerDialogFragment fragment = new DatePickerDialogFragment();
                    fragment.setArguments(b);
                    fragment.listener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            endDate.setText(String.format("%d. %d. %d",dayOfMonth, monthOfYear+1, year));
                            dEndDate = Calendar.getInstance();
                            dEndDate.set(Calendar.YEAR, year);
                            dEndDate.set(Calendar.MONTH, monthOfYear);
                            dEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        }
                    };
                    fragment.show(getActivity().getSupportFragmentManager(), "TAG");
                }
            }
        });
    }

    public Tournament getTournament() {
        Date sDate = null, eDate = null;
        if (dStartDate != null)
            sDate = dStartDate.getTime();
        if (dEndDate != null)
            eDate = dEndDate.getTime();

        return new Tournament(getArguments().getLong(ARG_TOUR_ID), competitionId, name.getText().toString(), sDate, eDate, note.getText().toString());
    }
}
