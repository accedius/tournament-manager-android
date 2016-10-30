package fit.cvut.org.cz.squash.presentation.activities;

import android.support.v4.app.Fragment;

import fit.cvut.org.cz.squash.presentation.fragments.ImportFragment;

/**
 * Created by kevin on 30.10.2016.
 */
public class ImportActivity extends fit.cvut.org.cz.tmlibrary.presentation.activities.ImportActivity {
    @Override
    public Fragment getImportFragment() {
        return ImportFragment.newInstance();
    }
}
