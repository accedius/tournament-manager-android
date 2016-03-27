package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.app.DatePickerDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.DatePicker;
import android.widget.EditText;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.presentation.dialogs.DatePickerDialogFragment;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class NewCompetitionFragment extends AbstractDataFragment {

    private static final String ARG_ID = "arg_id";

    public static NewCompetitionFragment newInstance(long id){
        NewCompetitionFragment fragment = new NewCompetitionFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    private EditText note, name, type, startDate, endDate;
    private FloatingActionButton fab;
    protected long competitionId = -1;


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

        if (getArguments() != null)
            competitionId = getArguments().getLong(ARG_ID, -1);


        startDate.setKeyListener(null);
        endDate.setKeyListener(null);

        //getActivity().getS

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment fragment = new DatePickerDialogFragment();
                fragment.listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDate.setText(String.format("%d.%d.%d",dayOfMonth, monthOfYear+1, year));
                    }
                };
                fragment.show(getActivity().getSupportFragmentManager(), "TAG");
            }

        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment fragment = new DatePickerDialogFragment();
                fragment.listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDate.setText(String.format("%d.%d.%d",dayOfMonth, monthOfYear+1, year));
                    }
                };
                fragment.show(getActivity().getSupportFragmentManager(), "TAG");
            }

        });

        note.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    fab.setVisibility(View.GONE);
                } else fab.setVisibility(View.VISIBLE);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "CompetitionCreated", Snackbar.LENGTH_SHORT).show();
            }
        });




        return v;
    }

    @Override
    protected void askForData() {

    }

    @Override
    protected boolean isDataSourceWorking() {
        return true;
    }

    @Override
    protected void bindDataOnView(Intent intent) {

    }

    @Override
    protected void registerReceivers() {

    }

    @Override
    protected void unregisterReceivers() {

    }

    @Override
    protected void sendForData() {
        if (competitionId != -1)
            super.sendForData();
    }
}
