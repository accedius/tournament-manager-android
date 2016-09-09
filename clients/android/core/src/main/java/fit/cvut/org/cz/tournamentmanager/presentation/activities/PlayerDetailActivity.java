package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.PlayerDetailFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.fragments.PlayerSportFragment;
import fit.cvut.org.cz.tournamentmanager.presentation.services.PlayerService;

/**
 * Created by atgot_000 on 29. 3. 2016.
 */
public class PlayerDetailActivity extends AbstractTabActivity {

    private long playerID;

    private Fragment[] fragments;
    private String[] titles;
    private ArrayList<ApplicationInfo> sport_packages;

    public PlayerDetailActivity() {
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        playerID = getIntent().getExtras().getLong(PlayerService.EXTRA_ID);
        Log.d("PDA", ""+playerID);
        sport_packages = getIntent().getExtras().getParcelableArrayList(PlayerService.EXTRA_PACKAGES);
        titles = new String[1+sport_packages.size()];
        titles[0] = getResources().getString(R.string.player_info);

        int i=1;
        for(ApplicationInfo info : sport_packages) {
            titles[i] = info.metaData.getString("sport_name");
            i++;
        }

        Fragment f1 = PlayerDetailFragment.newInstance(playerID, PlayerDetailFragment.class);
        fragments = new Fragment[1+sport_packages.size()];
        fragments[0] = f1;

        i=1;
        for(ApplicationInfo info : sport_packages) {
            PlayerSportFragment psf = new PlayerSportFragment();
            String package_name = info.metaData.getString("package_name");
            Bundle b = new Bundle();
            b.putLong("player_id", playerID);
            b.putString("package_name", package_name);
            b.putString("sport_name", info.metaData.getString("sport_name"));
            b.putString("activity_create_competition", info.metaData.getString("activity_create_competition"));
            b.putString("activity_detail_competition", info.metaData.getString("activity_detail_competition"));
            b.putString("stats_service", info.metaData.getString("service_stats"));
            psf.setArguments(b);
            fragments[i] = psf;
            i++;
        }

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
