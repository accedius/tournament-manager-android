package fit.cvut.org.cz.squash.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.fragments.NewSquashCompetitionFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewCompetitionFragment;

/**
 * Created by Vaclav on 28. 3. 2016.
 */
public class CreateCompetitionActivity extends AbstractToolbarActivity {
    @Override
    protected View injectView(ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.activity_single_container, parent, false);
    }

    @Override
    protected FloatingActionButton getFloatingActionButton(ViewGroup root) {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null){
            getSupportFragmentManager().beginTransaction().add(R.id.container, new NewSquashCompetitionFragment()).commit();
        }
    }
}
