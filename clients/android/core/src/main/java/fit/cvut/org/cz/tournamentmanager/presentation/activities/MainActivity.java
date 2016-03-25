package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import fit.cvut.org.cz.tmlibrary.presentation.fragments.NewCompetitionFragment;
import fit.cvut.org.cz.tmlibrary.presentation.interfaces.IProgressInterface;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.CompetitionListFragment;

public class MainActivity extends AppCompatActivity implements IProgressInterface {

    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        bar = (ProgressBar) findViewById(R.id.progress_spinner);

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new CompetitionListFragment())
                    .commit();
        }

    }

    @Override
    public void showProgress() {
        bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        bar.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        return true;
    }
}
