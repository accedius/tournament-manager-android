package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class SportFragment extends Fragment {

    private static final String action = "org.cz.cvut.tournamentmanager.action";

    //private DataReceiver receiver = new DataReceiver();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        ArrayList<String> titles = getArguments().getStringArrayList("titles");
        View v = inflater.inflate(R.layout.activity_sport, container, false);
        TextView txt =(TextView) v.findViewById(R.id.sport_name);
        txt.setText(titles.get(0));
        return v;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        CompetitionListFragment clf = new CompetitionListFragment();
        ft.add(R.id.fragment_competitions_list, clf);
        ft.commit();

        // pridat fragment (swipe) - viz main activity
    }
}
