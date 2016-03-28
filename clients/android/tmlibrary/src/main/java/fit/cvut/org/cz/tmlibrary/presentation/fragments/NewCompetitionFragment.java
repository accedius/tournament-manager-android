package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.app.DatePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.DatePicker;
import android.widget.EditText;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.DatePickerDialogFragment;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public abstract class NewCompetitionFragment extends AbstractDataFragment {

    private static final String ARG_ID = "arg_id";


    public static NewCompetitionFragment newInstance(long id, Class<? extends NewCompetitionFragment> clazz){
        NewCompetitionFragment fragment = null;
        try {
            Constructor<? extends NewCompetitionFragment> c = clazz.getConstructor();
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

        fragment.setArguments(args);
        return fragment;
    }

    private EditText note, name, type, startDate, endDate;
    private FloatingActionButton fab;
    private Calendar dStartDate = null, dEndDate = null;
    protected long competitionId = -1;

    private Competition competition = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_newcompetition, container, false);

        note = (EditText) v.findViewById(R.id.et_note);
        name = (EditText) v.findViewById(R.id.et_name);
        type = (EditText) v.findViewById(R.id.et_type);
        startDate = (EditText) v.findViewById(R.id.et_startDate);
        endDate = (EditText) v.findViewById(R.id.et_endDate);
        fab = (FloatingActionButton) v.findViewById(R.id.fab_edit);
        //tilNote = (TextInputLayout) v.findViewById(R.id.til_note);

        if (getArguments() != null)
            competitionId = getArguments().getLong(ARG_ID, -1);


        //We don't want user to write into editTexts
        startDate.setKeyListener(null);
        endDate.setKeyListener(null);


        //Instead we show dialog with date picker when the focus is gaied
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
                            dStartDate.set(Calendar.MONTH, monthOfYear + 1);
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
                            dEndDate.set(Calendar.MONTH, monthOfYear + 1);
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
                    competition = new Competition(competitionId, name.getText().toString(), sDate, eDate, note.getText().toString(), type.getText().toString());
                    saveCompetition(competition);
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
        Snackbar.make(v, "CompetitionCreated", Snackbar.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void customOnResume() {
        if (competitionId != -1)
            super.customOnResume();
    }

    @Override
    protected void customOnPause() {
        if (competitionId != -1)
            super.customOnPause();
    }

    protected abstract void saveCompetition(Competition c);

    protected final void bindCompetitionOnView(Competition c){
        this.competition = c;
    }
}
