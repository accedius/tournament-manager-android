package fit.cvut.org.cz.bowling.presentation.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.InvocationTargetException;

import fit.cvut.org.cz.bowling.R;
import fit.cvut.org.cz.bowling.data.entities.PointConfiguration;
import fit.cvut.org.cz.bowling.presentation.adapters.EditPointConfigurationsAdapter;
import fit.cvut.org.cz.bowling.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

public abstract class PointConfigurationDetailFragment extends AbstractDataFragment {
    protected PointConfiguration pointConfiguration = null;
    protected RecyclerView recyclerView;
    protected long configurationId = -1;
    protected AbstractListAdapter<Float, ? extends RecyclerView.ViewHolder> adapter;

    public static PointConfigurationDetailFragment newInstance(long id, Class<? extends PointConfigurationDetailFragment> clazz){
        PointConfigurationDetailFragment fragment = null;
        try {
            fragment = clazz.getConstructor().newInstance();
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
        args.putLong(ExtraConstants.EXTRA_ID, id);

        fragment.setArguments(args);
        return fragment;
    }

    protected abstract void updateConfigurations (PointConfiguration pc);

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {
        View v = inflater.inflate(R.layout.fragment_point_configuration_detail, container, false);
        configurationId = getArguments().getLong(ExtraConstants.EXTRA_ID, -1);
        recyclerView = v.findViewById(R.id.recycler_view);
        adapter = new EditPointConfigurationsAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        return v;
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        PointConfiguration pc = intent.getParcelableExtra(ExtraConstants.EXTRA_CONFIGURATION);
        getActivity().setTitle(getResources().getString(R.string.spca_title_fragment) + " - " + pc.sidesNumber);
        adapter.swapData(pc.getConfigurationPlacePoints());
        this.pointConfiguration = pc;
        super.customOnPause();
    }
}
