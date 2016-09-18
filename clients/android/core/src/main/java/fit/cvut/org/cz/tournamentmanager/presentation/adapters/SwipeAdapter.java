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
    Bundle arguments = null;

    public SwipeAdapter(FragmentManager fm, Bundle args) {
        super(fm);
        arguments = args;
        sport_packages = args.getParcelableArrayList("sport_packages");
    }

    @Override
    public Fragment getItem(int position) {
        SportFragment sf = new SportFragment();
        Bundle b = new Bundle();
        b.putParcelable("sport_package", sport_packages.get(position));
        b.putString("order_column", arguments.getString("order_column"));
        b.putString("order_type", arguments.getString("order_type"));

        sf.setArguments(b);
        return sf;
    }

    @Override
    public int getCount() {
        return sport_packages.size();
    }
}
