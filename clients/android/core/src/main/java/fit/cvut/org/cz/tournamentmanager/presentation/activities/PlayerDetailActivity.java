package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.PlayerCompetitionsListFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.PlayerDetailFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tournamentmanager.presentation.services.PlayerService;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class PlayerDetailActivity extends AbstractTabActivity {

    private static String HEADER_PLAYER_DETAIL = "Player info";
    private static String HEADER_COMPETITIONS_LIST = "Competitions";

    private long playerID;

    private Fragment[] fragments;
    private String[] titles;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        playerID = getIntent().getExtras().getLong(PlayerService.EXTRA_ID);
        Log.d("PDA", ""+playerID);

        titles = new String[]{ HEADER_PLAYER_DETAIL };//, HEADER_COMPETITIONS_LIST };
        Fragment f1 = PlayerDetailFragment.newInstance(playerID, PlayerDetailFragment.class);
        Fragment f2 = null;//PlayerCompetitionsListFragment.newInstance(playerID);
        fragments = new Fragment[]{ f1 };//, f2 };

        super.onCreate(savedInstanceState);
    }

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        PagerAdapter res = new DefaultViewPagerAdapter(manager, fragments, titles);
        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:{
                if (playerID == -1) break;
                Intent intent = new Intent(this, CreatePlayerActivity.class);
                intent.putExtra(PlayerService.EXTRA_ID, playerID);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
        return true;
    }
}
