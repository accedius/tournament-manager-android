package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import fit.cvut.org.cz.tmlibrary.R;

/**
 * Created by Vaclav on 25. 3. 2016.
 */
public class NewCompetitionFragment extends Fragment {

    private EditText note, name, type, startDate, endDate;
//    private TextView startDate, endDate;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_newcompetition, container, false);

        note = (EditText) v.findViewById(R.id.et_note);
        name = (EditText) v.findViewById(R.id.et_name);
        type = (EditText) v.findViewById(R.id.et_type);

        startDate = (EditText) v.findViewById(R.id.et_startDate);
        endDate = (EditText) v.findViewById(R.id.et_endDate);

        startDate.setKeyListener(null);
        endDate.setKeyListener(null);

        //SimpleDateFormat sdf = new SimpleDateFormat("YYYY-mm-dd");

        //startDate.setText("start: ");
        //endDate.setText("end: ");

        return v;
    }
}
