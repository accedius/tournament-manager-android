package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.data.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.data.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.data.entities.TournamentType;
import fit.cvut.org.cz.tmlibrary.data.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.data.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.data.helpers.TournamentTypes;
import fit.cvut.org.cz.tmlibrary.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.DatePickerDialogFragment;

/**
 * Fragment for create or edit Tournament.
 */
public abstract class NewTournamentFragment extends AbstractDataFragment {
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

        args.putLong(ExtraConstants.EXTRA_TOUR_ID, tourId);
        args.putLong(ExtraConstants.EXTRA_COMP_ID, compId);

        fragment.setArguments(args);
        return fragment;
    }

    private TextView type_label;
    private EditText note, name, startDate, endDate;
    private AppCompatSpinner type;
    private Calendar dStartDate = null, dEndDate = null;
    private ArrayAdapter<TournamentType> adapter;
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
        type_label = (TextView) v.findViewById(R.id.tv_tournament_type_label);
        type = (AppCompatSpinner) v.findViewById(R.id.sp_type);

        if (getArguments() != null) {
            tournamentId = getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID, -1);
            competitionId = getArguments().getLong(ExtraConstants.EXTRA_COMP_ID, -1);
        }

        if(isTypeChoosable()) {
            type_label.setVisibility(View.VISIBLE);
            type.setVisibility(View.VISIBLE);
        }

        if (tournamentId == -1) {
            setDatepicker(Calendar.getInstance(), Calendar.getInstance());
        } else {
            type.setEnabled(false);
        }

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(TournamentTypes.tournamentTypes(getResources()));

        type.setAdapter(adapter);

        //We don't want user to write into editTexts
        startDate.setKeyListener(null);
        endDate.setKeyListener(null);

        return v;
    }

    /**
     *
     * @return true, if you want to permit choosing between team and individuals, by default false
     */
    protected boolean isTypeChoosable() { return false; }

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

    /**
     *
     * @return default competition type if type is not chooseable
     */
    protected TournamentType defaultTournamentType() {
        return TournamentTypes.teams();
    }

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

        int index = t.getTypeId();
        type.setSelection(index);
    }

    // Set Datepicker dates to Tournament start and end
    private void setDatepicker(final Calendar start, final Calendar end) {
        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Bundle b = new Bundle();
                    b.putInt(ExtraConstants.EXTRA_YEAR, start.get(Calendar.YEAR));
                    b.putInt(ExtraConstants.EXTRA_MONTH, start.get(Calendar.MONTH));
                    b.putInt(ExtraConstants.EXTRA_DAY, start.get(Calendar.DAY_OF_MONTH));
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
                    b.putInt(ExtraConstants.EXTRA_YEAR, end.get(Calendar.YEAR));
                    b.putInt(ExtraConstants.EXTRA_MONTH, end.get(Calendar.MONTH));
                    b.putInt(ExtraConstants.EXTRA_DAY, end.get(Calendar.DAY_OF_MONTH));
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

        TournamentType t = defaultTournamentType();
        if(isTypeChoosable()) {
            t = (TournamentType) type.getSelectedItem();
        }
        return new Tournament(getArguments().getLong(ExtraConstants.EXTRA_TOUR_ID), competitionId, name.getText().toString(), sDate, eDate, note.getText().toString(), t);
    }
}
