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
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.entities.Competition;
import fit.cvut.org.cz.tmlibrary.business.entities.CompetitionType;
import fit.cvut.org.cz.tmlibrary.business.helpers.CompetitionTypes;
import fit.cvut.org.cz.tmlibrary.business.helpers.DateFormatter;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.DatePickerDialogFragment;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public abstract class NewCompetitionFragment extends AbstractDataFragment {
    protected static final String ARG_ID = "arg_id";

    /**
     * Constructor for this fragment with id of competition that needs to update
     * @param id
     * @param clazz
     * @return
     */
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

    private TextView type_label;
    private EditText note, name, startDate, endDate;
    private AppCompatSpinner type;
    private Calendar dStartDate = null, dEndDate = null;
    private ArrayAdapter<CompetitionType> adapter;
    protected long competitionId = -1;

    private Competition competition = null;

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_new_competition, container, false);

        note = (EditText) v.findViewById(R.id.et_note);
        name = (EditText) v.findViewById(R.id.et_name);
        type = (AppCompatSpinner) v.findViewById(R.id.sp_type);
        type_label = (TextView) v.findViewById(R.id.tv_competition_type_label);

        if (getArguments() != null)
            competitionId = getArguments().getLong(ARG_ID , -1);

        //We do not want to change competition type if it is already created
        if (competitionId != -1) {
            type.setEnabled(false);
            //type.setClickable(false);
        }

        if (!isTypeChoosable()) {
            type.setVisibility(View.GONE);
            type_label.setVisibility(View.GONE);
        }

        startDate = (EditText) v.findViewById(R.id.et_startDate);
        endDate = (EditText) v.findViewById(R.id.et_endDate);
        //tilNote = (TextInputLayout) v.findViewById(R.id.til_note);

        //We don't want user to write into editTexts
        startDate.setKeyListener(null);
        endDate.setKeyListener(null);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(CompetitionTypes.competitionTypes(getResources()));

        type.setAdapter(adapter);

        if (competitionId == -1) {
            setDatepicker(Calendar.getInstance(), Calendar.getInstance());
        }

        return v;
    }

    @Override
    public void customOnResume() {
        if (competitionId != -1)
            super.customOnResume();
    }

    @Override
    protected void customOnPause() {
        if (competitionId != -1)
            super.customOnPause();
    }

    /**
     *
     * @return String key of competition gotten in Bundle of Intent when receiving from service
     */
    protected abstract String getCompetitionKey();

    /**
     *
     * @return true, if you want to permit choosing between team and individuals
     */
    protected boolean isTypeChoosable() { return true; }

    /**
     *
     * @return default competition type if type is not chooseable
     */
    abstract protected CompetitionType defaultCompetitionType();

    @Override
    protected void bindDataOnView(Intent intent) {
        competition = intent.getParcelableExtra(getCompetitionKey());
        bindCompetitionOnView(competition);
    }

    private void bindCompetitionOnView(final Competition c){
        SimpleDateFormat dateFormat = DateFormatter.getInstance().getDisplayDateFormat();
        Calendar argStart = Calendar.getInstance();
        Calendar argEnd = Calendar.getInstance();

        name.setText(c.getName());
        if (c.getStartDate() != null) {
            startDate.setText(dateFormat.format(c.getStartDate()));
            dStartDate = Calendar.getInstance();
            dStartDate.setTime(c.getStartDate());
            argStart = dStartDate;
        }
        if (c.getEndDate() != null) {
            endDate.setText(dateFormat.format(c.getEndDate()));
            dEndDate = Calendar.getInstance();
            dEndDate.setTime(c.getEndDate());
            argEnd = dEndDate;
        }
        note.setText(c.getNote());

        setDatepicker(argStart, argEnd);
        int index = c.getType().id;
        type.setSelection(index);
    }

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

    public Competition getCompetition() {
        Date sDate = null, eDate = null;
        if (dStartDate != null)
            sDate = dStartDate.getTime();
        if (dEndDate != null)
            eDate = dEndDate.getTime();

        CompetitionType t = defaultCompetitionType();
        if (isTypeChoosable()) {
            t = (CompetitionType) type.getSelectedItem();
        }
        return new Competition(getArguments().getLong(ARG_ID), name.getText().toString(), sDate, eDate, note.getText().toString(), t);
    }
}
