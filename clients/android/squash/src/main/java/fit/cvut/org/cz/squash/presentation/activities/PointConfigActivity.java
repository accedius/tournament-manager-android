package fit.cvut.org.cz.squash.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.squash.R;
import fit.cvut.org.cz.squash.business.entities.PointConfig;
import fit.cvut.org.cz.squash.presentation.fragments.PointConfigFragment;
import fit.cvut.org.cz.squash.presentation.services.PointConfigService;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == fit.cvut.org.cz.tmlibrary.R.id.action_finish) {
            PointConfig pointConfig = ((PointConfigFragment)(getSupportFragmentManager().findFragmentById(R.id.container))).getPointConfig();
            if (pointConfig == null) {
                Snackbar.make(findViewById(android.R.id.content), getString(R.string.not_all_fields_error), Snackbar.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
            }

            Intent intent = PointConfigService.newStartIntent(PointConfigService.ACTION_EDIT_CFG, this);
            intent.putExtra(PointConfigService.EXTRA_CFG, pointConfig);

            startService(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
