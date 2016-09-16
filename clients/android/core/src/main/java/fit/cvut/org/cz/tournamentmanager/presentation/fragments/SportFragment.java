package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by Vaclav on 12. 3. 2016.
 */
public class SportFragment extends Fragment {
    private String sport_name;
    private String package_name;
    private String activity_create_competition;
    private String activity_detail_competition;

    private TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_sport_layout, container, false);
        tv = (TextView) v.findViewById(R.id.sport_name);
        Bundle b = getArguments();
        sport_name = b.getString("sport_name");
        package_name = b.getString("package_name");
        activity_create_competition = b.getString("activity_create_competition");
        activity_detail_competition = b.getString("activity_detail_competition");
        tv.setText(sport_name);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getChildFragmentManager().findFragmentById(R.id.fragment_competitions_list) == null) {
            CompetitionsListFragment clf = new CompetitionsListFragment();
            Bundle b = new Bundle();
            // TODO zbytečné znovuopakování všech argumentů, lepší poslat celý getArguments znovu
            b.putString("package_name", package_name);
            b.putString("sport_name", sport_name);
            b.putString("activity_detail_competition", activity_detail_competition);
            b.putString("activity_create_competition", activity_create_competition);
            b.putString("order_column", getArguments().getString("order_column"));
            b.putString("order_type", getArguments().getString("order_type"));
            clf.setAction(clf.getAction() + "." + package_name);
            clf.setArguments(b);
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_competitions_list, clf)
                    .commit();
        }
    }
}
