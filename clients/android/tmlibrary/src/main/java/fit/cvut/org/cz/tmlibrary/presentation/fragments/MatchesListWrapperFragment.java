package fit.cvut.org.cz.tmlibrary.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import fit.cvut.org.cz.tmlibrary.R;

/**
 * Created by Vaclav on 10. 4. 2016.
 */
public abstract class MatchesListWrapperFragment extends Fragment {
    public static final String ARG_ID = "arg_id";

    public static MatchesListWrapperFragment newInstance(long id, Class<? extends MatchesListWrapperFragment> clazz){
        MatchesListWrapperFragment fragment = null;
        try {
            Constructor<? extends MatchesListWrapperFragment> c = clazz.getConstructor();
            fragment = c.newInstance();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Bundle args = new Bundle();

        args.putLong(ARG_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_matches_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getChildFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            getChildFragmentManager().beginTransaction().add(R.id.fragment_container, supplyFragment(getArguments().getLong(ARG_ID))).commit();
        }
    }

    /**
     *
     * @param tournamentId
     * @return fragment with list of matches
     */
    protected abstract Fragment supplyFragment(long tournamentId);
    public void refresh(){
        Fragment fr = getChildFragmentManager().findFragmentById(R.id.fragment_container);
        if (fr != null && fr instanceof AbstractDataFragment) {
            ((AbstractDataFragment) fr).customOnResume();
        }
    }
}
