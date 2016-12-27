package fit.cvut.org.cz.tmlibrary.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.business.loaders.entities.CompetitionImportInfo;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.ImportFragment;

/**
 * Created by kevin on 28.10.2016.
 */
public class ImportActivity extends AbstractToolbarActivity {
    public static final String COMPETITION = "competition";
    public static final String TOURNAMENTS = "tournaments";
    public static final String PLAYERS = "players";
    public static final String CONFLICTS = "conflicts";

    private Fragment fragment = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        CompetitionImportInfo competition = intent.getParcelableExtra(COMPETITION);
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
