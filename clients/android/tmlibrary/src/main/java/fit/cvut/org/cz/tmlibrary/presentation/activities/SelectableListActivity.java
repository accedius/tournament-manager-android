package fit.cvut.org.cz.tmlibrary.presentation.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

/**
 * Created by Vaclav on 3. 4. 2016.
 */
public abstract class SelectableListActivity<T extends Parcelable> extends AbstractToolbarActivity {
    public static final String EXTRA_DATA = "extra_data";
    public static final String EXTRA_OMIT_DATA = "extra_omit_data";
    public static final String EXTRA_ID = "extra_id";

    @Override
    protected View injectView(ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.activity_single_fragment, parent, false);
    }

    @Override
    protected FloatingActionButton getFloatingActionButton(ViewGroup root) {
        return null;
    }

    protected abstract AbstractSelectableListFragment<T> getListFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, getListFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_finish) {
            AbstractSelectableListFragment<T> frag = (AbstractSelectableListFragment<T>) (getSupportFragmentManager().findFragmentById(R.id.fragment_container));
            ArrayList<T> data = null;

            if (frag != null) {
                data = frag.getSelectedData();
            }
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(EXTRA_DATA, data);

            setResult(Activity.RESULT_OK, intent);

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
