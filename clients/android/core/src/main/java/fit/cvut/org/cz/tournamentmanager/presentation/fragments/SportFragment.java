package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.pm.ApplicationInfo;
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
    private ApplicationInfo sport_package;
    private String package_name = "";

    private TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_sport_layout, container, false);
        tv = (TextView) v.findViewById(R.id.sport_name);
        Bundle b = getArguments();
        sport_package = b.getParcelable("sport_package");
        package_name = sport_package.metaData.getString("sport_name");
        tv.setText(package_name);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getChildFragmentManager().findFragmentById(R.id.fragment_competitions_list) == null) {
            CompetitionsListFragment clf = new CompetitionsListFragment();
            Bundle b = new Bundle();
            b.putParcelable("sport_package", sport_package);
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
