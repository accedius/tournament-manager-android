package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.SwipeAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.SportFragment;

/**
 * Created by atgot_000 on 25. 3. 2016.
 */
public class SportActivity extends AbstractToolbarActivity {

    ViewPager vp;

    @Override
    protected View injectView(ViewGroup parent) {
        View v = getLayoutInflater().inflate(R.layout.activity_sport, parent, false);
        return v;
    }

    @Override
    protected FloatingActionButton getFloatingActionButton(ViewGroup root) {
        return null;
        //return new FloatingActionButton(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get list of installed sport packages
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        ArrayList<ApplicationInfo> sport_packages = new ArrayList<>();
        ArrayList<String> sport_names = new ArrayList<>();
        ArrayList<String> package_names = new ArrayList<>();

        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.metaData != null
                && packageInfo.metaData.containsKey("application_type") == true
                && packageInfo.metaData.get("application_type").equals(getString(R.string.tournament_manager_package))) {

                    sport_packages.add(packageInfo);

            }
        }

        vp = (ViewPager)findViewById(R.id.view_pager);
        SwipeAdapter sa = new SwipeAdapter(getSupportFragmentManager(), sport_packages);
        vp.setAdapter(sa);
    }
}
