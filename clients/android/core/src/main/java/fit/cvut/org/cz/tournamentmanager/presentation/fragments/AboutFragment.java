package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tournamentmanager.BuildConfig;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.helpers.PackagesInfo;

/**
 * Fragment to portray about item
 */
public class AboutFragment extends Fragment {
    private CoordinatorLayout v;

    private int sportsCount = 0;
    private ArrayList<String> packageLabelVersion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        Map<String, ApplicationInfo> sports = PackagesInfo.getSportContexts(getContext());
        packageLabelVersion = new ArrayList<>();
        int i = 0;
        packageLabelVersion.add( getResources().getString(R.string.app_name) + " version: " + BuildConfig.VERSION_NAME);
        for (Map.Entry<String, ApplicationInfo> sport: sports.entrySet()) {
            String package_Label = sport.getValue().loadLabel(getContext().getPackageManager()).toString();
            String package_Version = "1.0";
            String package_name = sport.getValue().metaData.getString(CrossPackageConstants.PACKAGE_NAME);
            try {
                package_Version = getContext().getPackageManager().getPackageInfo(package_name, 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {}

            String lv = package_Label + " version: " + package_Version;
            if(!packageLabelVersion.contains(lv)) {
                packageLabelVersion.add(lv);
                ++i;
            }
        }
        sportsCount = i;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = (CoordinatorLayout) inflater.inflate(fit.cvut.org.cz.tmlibrary.R.layout.fragment_abstract_data, container, false);
        View fragmentAbout = inflater.inflate(R.layout.fragment_about, container, false);
        ListView listView = fragmentAbout.findViewById(R.id.version_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.list_item, android.R.id.text1, packageLabelVersion);
        listView.setAdapter(adapter);
        v.addView(fragmentAbout);
        return v;
    }

}
