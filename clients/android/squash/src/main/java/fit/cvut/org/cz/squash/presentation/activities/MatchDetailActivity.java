package fit.cvut.org.cz.squash.presentation.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;

import fit.cvut.org.cz.squash.presentation.IGetSetsInterface;
import fit.cvut.org.cz.squash.presentation.fragments.SetsFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;

/**
 * Created by Vaclav on 24. 4. 2016.
 */
public class MatchDetailActivity extends AbstractTabActivity {

    private IGetSetsInterface setsInterface = null;

    public void setSetsInterface(IGetSetsInterface setsInterface) {
        this.setsInterface = setsInterface;
    }

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        return new DefaultViewPagerAdapter(manager, new Fragment[]{new SetsFragment()}, new String[]{"Sets"});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_finish, menu);

        return true;
    }
}

