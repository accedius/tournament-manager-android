package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.presentation.CrossPackageComunicationConstants;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractToolbarActivity;
import fit.cvut.org.cz.tournamentmanager.R;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.NewPlayerFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.services.PlayerService;

public class CreatePlayerActivity extends AbstractToolbarActivity {

    @Override
    protected View injectView(ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.activity_create_player, parent, false);
    }

    @Override
    protected FloatingActionButton getFloatingActionButton(ViewGroup root) {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long id = getIntent().getLongExtra(PlayerService.EXTRA_ID, -1);

        if( getSupportFragmentManager().findFragmentById(R.id.container) == null )
            getSupportFragmentManager().beginTransaction().add(R.id.container, NewPlayerFragment.newInstance(id, NewPlayerFragment.class)).commit();
    }

}
