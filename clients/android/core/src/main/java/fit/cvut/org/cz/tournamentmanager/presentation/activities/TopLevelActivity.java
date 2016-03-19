package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by Vaclav on 18. 3. 2016.
 */
public class TopLevelActivity extends AbstractToolbarActivity {
    @Override
    protected View injectView(ViewGroup parent) {
        return LayoutInflater.from(this).inflate(R.layout.activity_top_level, parent, false);
    }

    @Override
    protected FloatingActionButton getFloatingActionButton() {
        return null;
    }
}
