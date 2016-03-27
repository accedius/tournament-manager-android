package fit.cvut.org.cz.tournamentmanager.presentation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.presentation.interfaces.IProgressInterface;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by Vaclav on 27. 3. 2016.
 */
public class ParentFragment extends Fragment implements IProgressInterface {

    private IProgressInterface parent = null;

    @Override
    public void showProgress() {
        Log.d("Fragment Progress", "second level progress shown");
        if (parent != null) parent.showProgress();
    }

    @Override
    public void hideProgress() {
        Log.d("Fragment Progress", "second level progress hidden");
        if (parent != null) parent.hideProgress();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof IProgressInterface)
            parent =  (IProgressInterface)context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        parent = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_parent, container, false);

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getChildFragmentManager().findFragmentById(R.id.container)== null){

            getChildFragmentManager().beginTransaction().add(R.id.container,new CompetitionListFragment()).commit();
        }
    }
}
