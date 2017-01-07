package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.communication.CrossPackageConstants;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.business.ManagerFactory;
import fit.cvut.org.cz.tournamentmanager.data.entities.Setting;
import fit.cvut.org.cz.tournamentmanager.presentation.helpers.PackagesInfo;

/**
 * Fragment to display list of Sports.
 */
public class SportsFragment extends Fragment {
    private DefaultViewPagerAdapter adapter = null;
    private CompetitionsListFragment[] fragments;
    private String[] titles;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Map<String, ApplicationInfo> contexts = PackagesInfo.getSportContexts(getContext());
        List<Setting> ignoredSports = ManagerFactory.getInstance(getContext()).getEntityManager(Setting.class).getAll();
        for (Setting s : ignoredSports) {
            if (contexts.containsKey(s.getSportName())) {
                contexts.remove(s.getSportName());
            }
        }

        titles = new String[contexts.size()];
        fragments = new CompetitionsListFragment[contexts.size()];
        int i = 0;
        for (Map.Entry<String, ApplicationInfo> entry : contexts.entrySet()) {
            String sport_name = entry.getKey();

            Bundle b = new Bundle();
            b.putParcelable(CrossPackageConstants.SPORT_PACKAGE, entry.getValue());
            b.putString(CrossPackageConstants.EXTRA_SPORT_CONTEXT, sport_name);

            CompetitionsListFragment clf = new CompetitionsListFragment();
            clf.setArguments(b);

            fragments[i] = clf;
            titles[i] = getResources().getString(getResources().getIdentifier(sport_name, "string", getContext().getPackageName()));
            i++;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sports, container, false);
        ViewPager pager = (ViewPager) v.findViewById(R.id.viewPager);
        pager.setAdapter(getAdapter());
        pager.setOffscreenPageLimit(1);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(pager);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        pager.setCurrentItem(0);
        return v;
    }

    private PagerAdapter getAdapter() {
        adapter = new DefaultViewPagerAdapter(getChildFragmentManager(), fragments, titles);
        return adapter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sport_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Method to order Competitions.
     * @param type column for order
     */
    public void orderData(final String type) {
        for (CompetitionsListFragment f : fragments) {
            f.orderData(type);
        }
    }
}
