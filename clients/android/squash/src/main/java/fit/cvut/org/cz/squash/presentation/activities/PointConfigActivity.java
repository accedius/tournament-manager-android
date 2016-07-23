package fit.cvut.org.cz.squash.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.presentation.fragments.PointConfigFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;

/**This Activity accomodates PointConfigFragment
 * @see fit.cvut.org.cz.squash.presentation.fragments.PointConfigFragment
 * Created by Vaclav on 19. 4. 2016.
 */
public class PointConfigActivity extends AbstractToolbarActivity {
    public static final String ARG_ID = "arg_id";

    @Override
    protected View injectView(ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.activity_single_container, parent, false );
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long id = getIntent().getLongExtra(ARG_ID, -1);

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null){
            getSupportFragmentManager().beginTransaction().add(R.id.container, PointConfigFragment.newInstance(id)).commit();
        }

    }
}
