package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tournamentmanager.R;

/**
 * Created by atgot_000 on 25. 3. 2016.
 */
public class CompetitionListActivity extends AbstractToolbarActivity {


    @Override
    protected View injectView(ViewGroup parent) {
        View v = getLayoutInflater().inflate(R.layout.activity_competitions_list, parent, false);
        return v;
    }

    @Override
    protected FloatingActionButton getFloatingActionButton(ViewGroup root) {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // data o baliccich,
        // pridat fragment (swipe) - viz main activity
    }
}
