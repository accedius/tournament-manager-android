package fit.cvut.org.cz.tournamentmanager.presentation.fragments;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.adapters.SwipeAdapter;

public class SportsFragment extends Fragment {

    private ArrayList<ApplicationInfo> sport_packages;

    public SportsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment_view = inflater.inflate(R.layout.fragment_sports, container, false);

        sport_packages = getArguments().getParcelableArrayList("sport_packages");
        ViewPager vp = (ViewPager)fragment_view.findViewById(R.id.view_pager);
        SwipeAdapter sa = new SwipeAdapter(getActivity().getSupportFragmentManager(), sport_packages);
        vp.setAdapter(sa);
        return vp;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("SportSFragment", "Started");
        ViewPager vp = (ViewPager)getView().findViewById(R.id.view_pager);
        Log.d("SSF", "sport packages "+sport_packages.size());
        SwipeAdapter sa = new SwipeAdapter(getActivity().getSupportFragmentManager(), sport_packages);
        vp.setAdapter(sa);
    }
}
