package fit.cvut.org.cz.tournamentmanager.presentation.adapters;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import fit.cvut.org.cz.tournamentmanager.presentation.fragments.SportFragment;

/**
 * Created by kevin on 27.3.2016.
 */
public class SwipeAdapter extends FragmentPagerAdapter {
    ArrayList<ApplicationInfo> sport_packages;

    public SwipeAdapter(FragmentManager fm, ArrayList<ApplicationInfo> sport_packages) {
        super(fm);
        this.sport_packages = sport_packages;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new SportFragment();
        Bundle b = new Bundle();

        b.putString("sport_name", sport_packages.get(position).metaData.getString("sport_name"));
        b.putString("package_name", sport_packages.get(position).metaData.getString("package_name"));
        b.putString("activity_create_competition", sport_packages.get(position).metaData.getString("activity_create_competition"));
        b.putString("activity_detail_competition", sport_packages.get(position).metaData.getString("activity_detail_competition"));

        f.setArguments(b);
        return f;
    }

    @Override
    public int getCount() {
        return sport_packages.size();
    }
}
