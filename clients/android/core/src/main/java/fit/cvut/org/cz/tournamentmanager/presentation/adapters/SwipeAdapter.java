package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import fit.cvut.org.cz.tournamentmanager.presentation.fragments.SportFragment;

/**
 * Created by kevin on 27.3.2016.
 */
public class SwipeAdapter extends FragmentPagerAdapter {
    ArrayList<String> sport_names;
    ArrayList<String> package_names;
    ArrayList<ApplicationInfo> sport_packages;

    //public SwipeAdapter(FragmentManager fm, ArrayList<String> sport_names, ArrayList<String> package_names) {
    public SwipeAdapter(FragmentManager fm, ArrayList<ApplicationInfo> sport_packages) {
        super(fm);
        /*this.sport_names = sport_names;
        this.package_names = package_names;*/
        this.sport_packages = sport_packages;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new SportFragment();
        Bundle b = new Bundle();

        b.putString("sport_name", sport_packages.get(position).metaData.getString("sport_name"));
        b.putString("package_name", sport_packages.get(position).metaData.getString("package_name"));

        f.setArguments(b);
        return f;
    }

    @Override
    public int getCount() {
        return sport_packages.size();
    }
}
