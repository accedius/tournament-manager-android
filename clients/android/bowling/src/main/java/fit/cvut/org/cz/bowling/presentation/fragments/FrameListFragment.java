package fit.cvut.org.cz.bowling.presentation.fragments;

import android.view.View;

import java.util.List;

import fit.cvut.org.cz.bowling.business.entities.FrameOverview;
import fit.cvut.org.cz.bowling.presentation.adapters.FrameOverviewAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.AbstractListAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractListFragment;

public class FrameListFragment extends AbstractListFragment<FrameOverview> {

    @Override
    protected AbstractListAdapter getAdapter() {
        return new FrameOverviewAdapter(getResources()) {
            @Override
            protected void setOnClickListeners(View v, int position, long playerId, byte frameNumber, List<Integer> rolls, String playerName, int currentScore) {
                super.setOnClickListeners(v, position, playerId, frameNumber, rolls, playerName, currentScore);
            }
        };
    }

    @Override
    protected String getDataKey() {
        return null;
    }

    @Override
    public void askForData() {

    }

    @Override
    protected boolean isDataSourceWorking() {
        return false;
    }

    @Override
    protected void registerReceivers() {

    }

    @Override
    protected void unregisterReceivers() {

    }
}
