package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.SwipeAdapter;

public class SportsFragment extends Fragment {
    private ViewPager vp = null;

    public SportsFragment() {}

    public int getCurrentItem() {
        return vp.getCurrentItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment_view = inflater.inflate(R.layout.fragment_sports, container, false);

        vp = (ViewPager)fragment_view.findViewById(R.id.view_pager);
        SwipeAdapter sa = new SwipeAdapter(getChildFragmentManager(), getArguments());
        vp.setAdapter(sa);

        if (getArguments().containsKey("current_item"))
            vp.setCurrentItem(getArguments().getInt("current_item"));
        return vp;
    }
}
