package fit.cvut.org.cz.squash.presentation.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import fit.cvut.org.cz.squash.business.ManagersFactory;
import fit.cvut.org.cz.squash.business.entities.SetRowItem;
import fit.cvut.org.cz.squash.presentation.fragments.MatchPlayersFragment;
import fit.cvut.org.cz.squash.presentation.fragments.SetsFragment;
import fit.cvut.org.cz.squash.presentation.services.MatchService;
import fit.cvut.org.cz.tmlibrary.business.entities.Player;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;

/**
 * Created by Vaclav on 24. 4. 2016.
 */
public class MatchDetailActivity extends AbstractTabActivity {

    public static final String ARG_PLAYED = "arg_played";
    public static final String ARG_ID = "arg_id";

    public static Intent newStartIntent(Context context, long id, boolean played){
        Intent i = new Intent(context, MatchDetailActivity.class);
        i.putExtra(ARG_ID, id);
        i.putExtra(ARG_PLAYED, played);

        return i;
    }

    private DefaultViewPagerAdapter adapter = null;

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        adapter = new DefaultViewPagerAdapter(manager, new Fragment[]{new SetsFragment()}, new String[]{"Sets"});
        return adapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_finish, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == fit.cvut.org.cz.tmlibrary.R.id.action_finish){

            SetsFragment fr = (SetsFragment) getSupportFragmentManager().findFragmentByTag(adapter.getTag(0));

            if (fr != null && !fr.isWorking()){
                Intent intent = MatchService.newStartIntent(MatchService.ACTION_UPDATE_MATCH_DETAIL, this);
                ArrayList<SetRowItem> list = fr.getSets();
                intent.putExtra(MatchService.EXTRA_MATCHES, list);
                intent.putExtra(MatchService.EXTRA_ID, getIntent().getLongExtra(ARG_ID, -1));
                startService(intent);
            }

            if (adapter.getCount() > 1){

                MatchPlayersFragment mfr = (MatchPlayersFragment) getSupportFragmentManager().findFragmentByTag(adapter.getTag(1));
                if (mfr != null && !mfr.isWorking()){
                    ArrayList<Player> players = mfr.getPlayers();
                }
            }
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

