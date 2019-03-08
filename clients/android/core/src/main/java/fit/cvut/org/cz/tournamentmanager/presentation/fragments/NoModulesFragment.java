package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by kevin on 7.1.2017.
 */
public class NoModulesFragment extends Fragment {

    /**
     * NoModulesFragment creator.
     * @return NoModulesFragment instance
     */
    public static NoModulesFragment newInstance() {
        NoModulesFragment fragment = new NoModulesFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_no_modules, container, false);
        return v;
    }
}
