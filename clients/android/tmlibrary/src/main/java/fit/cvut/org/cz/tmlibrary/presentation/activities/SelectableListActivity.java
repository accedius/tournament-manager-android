package fit.cvut.org.cz.tmlibrary.presentation.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.view.menu.ActionMenuItem;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.AbstractSelectableListFragment;

/**
 * Created by Vaclav on 3. 4. 2016.
 */
public abstract class SelectableListActivity<T extends Parcelable> extends AbstractToolbarActivity {
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
        getMenuInflater().inflate(R.menu.menu_cancel, menu);
        return true;
    }

    private void sendToSaveItems () {
        AbstractSelectableListFragment<T> frag = (AbstractSelectableListFragment<T>) (getSupportFragmentManager().findFragmentById(R.id.fragment_container));
        ArrayList<T> data = null;

        if (frag != null) {
            data = frag.getSelectedData();
        }
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(ExtraConstants.EXTRA_DATA, data);

        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_finish || itemId == android.R.id.home) {
            sendToSaveItems();
            return true;
        }
        else if (itemId == R.id.action_cancel) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        sendToSaveItems();
    }
}
