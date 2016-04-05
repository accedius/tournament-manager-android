package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import fit.cvut.org.cz.tmlibrary.business.entities.Tournament;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.DatePickerDialogFragment;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public abstract class NewTournamentFragment extends AbstractDataFragment {

    private static final String ARG_ID = "arg_id";
    private static final String ARG_COMPETIITON_ID = "arg_competition_id";

    /**
     *
     * @param id id of tournament for update or id of Competition this tournament belongs to depends on forCompetition param
     * @param forComeptition if true id is this tournaments competition
     * @param clazz class of child fragment
     * @return
     */
    public static NewTournamentFragment newInstance(long id, boolean forComeptition, Class<? extends NewTournamentFragment> clazz){
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

        if (forComeptition) args.putLong(ARG_COMPETIITON_ID, id);
        else args.putLong(ARG_ID, id);

        fragment.setArguments(args);
        return fragment;
    }



    private EditText note, name, startDate, endDate;
    private FloatingActionButton fab;
    private Calendar dStartDate = null, dEndDate = null;
    protected long tournamentId = -1;
    protected long competitionId = -1;

    private Tournament tournament = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_new_tournament, container, false);

        note = (EditText) v.findViewById(R.id.et_note);
        name = (EditText) v.findViewById(R.id.et_name);
        startDate = (EditText) v.findViewById(R.id.et_startDate);
        endDate = (EditText) v.findViewById(R.id.et_endDate);
        fab = (FloatingActionButton) v.findViewById(R.id.fab_edit);
        //tilNote = (TextInputLayout) v.findViewById(R.id.til_note);

        if (getArguments() != null){
            tournamentId = getArguments().getLong(ARG_ID, -1);
            competitionId = getArguments().getLong(ARG_COMPETIITON_ID, -1);
        }



        //We don't want user to write into editTexts
        startDate.setKeyListener(null);
        endDate.setKeyListener(null);

        //Instead we show dialog with date picker when the focus is gained
        startDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerDialogFragment fragment = new DatePickerDialogFragment();
                    fragment.listener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            startDate.setText(String.format("%d.%d.%d", dayOfMonth, monthOfYear + 1, year));
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

        //here as well
        endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    DatePickerDialogFragment fragment = new DatePickerDialogFragment();
                    fragment.listener = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            endDate.setText(String.format("%d.%d.%d",dayOfMonth, monthOfYear+1, year));
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDataSourceWorking() && validate(v)){
                    Date sDate = null; Date eDate = null;
                    if (dStartDate != null) sDate = dStartDate.getTime();
                    if (dEndDate != null) eDate = dEndDate.getTime();
                    if (tournamentId == -1){
                        tournament = new Tournament(tournamentId, competitionId, name.getText().toString(), sDate, eDate, note.getText().toString());
                        saveTournament(tournament);
                    }
                    else{
                        tournament.setName(name.getText().toString());
                        tournament.setStartDate(sDate);
                        tournament.setEndDate(eDate);
                        tournament.setNote(note.getText().toString());
                        updateTournament(tournament);
                    }
                    getActivity().finish();
                }
            }
        });

        return v;
    }

    private boolean validate(View v){
        if (name.getText().toString().isEmpty()){
            Snackbar.make(v, R.string.invalidName, Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (competitionId == -1 && tournamentId == -1){
            Snackbar.make(v, R.string.tournament_id_invalid, Snackbar.LENGTH_LONG).show();
            return false;
        }

        Snackbar.make(v, "Tournament created", Snackbar.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void customOnResume() {
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

    private void bindTournamentOnView(Tournament t){
        name.setText(t.getName());
        if (t.getStartDate() != null){
            startDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(t.getStartDate()));
            dStartDate = Calendar.getInstance();
            dStartDate.setTime(t.getStartDate());
        }
        if (t.getEndDate() != null){
            endDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(t.getEndDate()));
            dEndDate = Calendar.getInstance();
            dEndDate.setTime(t.getStartDate());
        }
        note.setText(t.getNote());

    }
}
