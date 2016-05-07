package fit.cvut.org.cz.squash.presentation.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.entities.PointConfig;
import fit.cvut.org.cz.squash.presentation.services.PointConfigService;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractDataFragment;

/**
 * Created by Vaclav on 19. 4. 2016.
 */
public class PointConfigFragment extends AbstractDataFragment {

    private static final String ARG_ID = "arg_id";
    private PointConfig cfg = null;
    private TextView w, l, d;

    private void editCfg(){

        cfg.setWin(Integer.parseInt(w.getText().toString()));
        cfg.setDraw(Integer.parseInt(d.getText().toString()));
        cfg.setLoss(Integer.parseInt(l.getText().toString()));

        Intent intent = PointConfigService.newStartIntent(PointConfigService.ACTION_EDIT_CFG, getContext());
        intent.putExtra(PointConfigService.EXTRA_CFG, cfg);
        getContext().startService(intent);
    }

    public static PointConfigFragment newInstance(long id){
        PointConfigFragment f = new PointConfigFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        f.setArguments(args);
        return f;
    }

    @Override
    public void askForData() {
        Intent intent = PointConfigService.newStartIntent(PointConfigService.ACTION_GET_BY_ID, getContext());
        intent.putExtra(PointConfigService.EXTRA_ID, getArguments().getLong(ARG_ID, -1));
        getContext().startService(intent);
    }

    @Override
    protected boolean isDataSourceWorking() {
        return PointConfigService.isWorking(PointConfigService.ACTION_GET_BY_ID);
    }

    @Override
    protected void bindDataOnView(Intent intent) {
        cfg = intent.getParcelableExtra(PointConfigService.EXTRA_CFG);
        w.setText(Integer.toString(cfg.getWin()));
        l.setText(Integer.toString(cfg.getLoss()));
        d.setText(Integer.toString(cfg.getDraw()));
    }

    @Override
    protected void registerReceivers() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter(PointConfigService.ACTION_GET_BY_ID));
    }

    @Override
    protected void unregisterReceivers() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

    @Override
    protected View injectView(LayoutInflater inflater, ViewGroup container) {

        View v = inflater.inflate(R.layout.fragment_point_config, container, false);
        w = (EditText) v.findViewById(R.id.tv_wins);
        l = (EditText) v.findViewById(R.id.tv_loss);
        d = (EditText) v.findViewById(R.id.tv_draws);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_edit);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCfg();
                getActivity().finish();
            }
        });

        return v;
    }
}
