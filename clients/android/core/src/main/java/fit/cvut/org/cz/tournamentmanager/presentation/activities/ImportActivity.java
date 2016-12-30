package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.CompetitionImportInfo;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tmlibrary.presentation.communication.ExtraConstants;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.ImportFragment;

/**
 * Created by kevin on 28.10.2016.
 */
public class ImportActivity extends AbstractToolbarActivity {
    private Fragment fragment = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        CompetitionImportInfo competition = intent.getParcelableExtra(ExtraConstants.EXTRA_COMPETITION);
        setTitle(getResources().getString(R.string.import_summary)+" – "+competition.getName());

        if (getSupportFragmentManager().findFragmentById(R.id.container) == null) {
            fragment = getImportFragment();
            fragment.setArguments(intent.getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
    }

    public Fragment getImportFragment() {
        return ImportFragment.newInstance();
    }

    @Override
    protected View injectView(ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.activity_import, parent, false);
    }
}
