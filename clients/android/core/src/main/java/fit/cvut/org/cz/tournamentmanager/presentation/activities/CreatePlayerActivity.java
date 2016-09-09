package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.business.entities.Player;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == fit.cvut.org.cz.tmlibrary.R.id.action_finish) {
            Player player = ((NewPlayerFragment)(getSupportFragmentManager().findFragmentById(R.id.container))).getPlayer();
            if (player.getName().isEmpty() || player.getEmail().isEmpty()) {
                Snackbar.make(findViewById(android.R.id.content), getString(fit.cvut.org.cz.tmlibrary.R.string.name_email_empty_error), Snackbar.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
            }

            Intent intent;
            if (player.getId() == -1) {
                intent = PlayerService.newStartIntent(PlayerService.ACTION_CREATE, this);
            } else {
                intent = PlayerService.newStartIntent(PlayerService.ACTION_UPDATE, this);
            }
            intent.putExtra(PlayerService.EXTRA_PLAYER, player);
            startService(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
