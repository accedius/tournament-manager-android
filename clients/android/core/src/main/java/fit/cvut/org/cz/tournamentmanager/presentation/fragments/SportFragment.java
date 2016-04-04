package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class SportFragment extends Fragment {

    private static final String action = "org.cz.cvut.tournamentmanager.action";

    //private DataReceiver receiver = new DataReceiver();

    private TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate(R.layout.fragment_sport_layout, container, false);
        tv = (TextView) v.findViewById(R.id.sport_name);
        Bundle b = getArguments();
        String p = b.getString("package_name");
        String s = b.getString("sport_name");
        tv.setText(s);

        if (getChildFragmentManager().findFragmentById(R.id.fragment_competitions_list) == null) {
            CompetitionsListFragment clf = new CompetitionsListFragment();
            clf.setAction(clf.getAction() + "." + p);
            clf.setArguments(b);
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_competitions_list, clf)
                    .commit();
        }

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
