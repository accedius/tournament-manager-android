package fit.cvut.org.cz.hockey.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.hockey.R;
import fit.cvut.org.cz.hockey.presentation.fragments.NewHockeyCompetitionFragment;
import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class CreateCompetitionActivity extends AbstractToolbarActivity {
    @Override
    protected View injectView(ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.activity_basic_layout, parent, false);
    }

    @Override
    protected FloatingActionButton getFloatingActionButton(ViewGroup root) {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long id = getIntent().getLongExtra(CrossPackageComunicationConstants.EXTRA_ID, -1);

        if( getSupportFragmentManager().findFragmentById(R.id.container) == null )
            getSupportFragmentManager().beginTransaction().add(R.id.container, NewHockeyCompetitionFragment.newInstance( id, NewHockeyCompetitionFragment.class )).commit();
    }
}
