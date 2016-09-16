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

        // TODO: 16.9.2016 snazší cesta než 4x putString:::  b.putParcelable("sport_package", sport_packages.get(position));

        b.putString("sport_name", sport_packages.get(position).metaData.getString("sport_name"));
        b.putString("package_name", sport_packages.get(position).metaData.getString("package_name"));
        b.putString("activity_create_competition", sport_packages.get(position).metaData.getString("activity_create_competition"));
        b.putString("activity_detail_competition", sport_packages.get(position).metaData.getString("activity_detail_competition"));

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
