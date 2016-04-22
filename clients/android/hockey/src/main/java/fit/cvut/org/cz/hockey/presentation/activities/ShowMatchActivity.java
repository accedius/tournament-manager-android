package fit.cvut.org.cz.hockey.presentation.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

import fit.cvut.org.cz.hockey.presentation.fragments.HockeyMatchOverviewFragment;
import fit.cvut.org.cz.tmlibrary.presentation.activities.AbstractTabActivity;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;

/**
 * Created by atgot_000 on 22. 4. 2016.
 */
public class ShowMatchActivity extends AbstractTabActivity {

    private static String HEADER_OVERVIEW_MATCH = "Overview";

    private static final String MATCH_ID = "match_id";

    private long matchId;

    private Fragment[] fragments;
    private String[] titles;

    public static Intent newStartIntent( Context context, long matchId )
    {
        Intent intent = new Intent( context, ShowMatchActivity.class);
        Bundle b = new Bundle();

        b.putLong(MATCH_ID, matchId);
        intent.putExtras(b);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        matchId = getIntent().getExtras().getLong(MATCH_ID);

        titles = new String[]{ HEADER_OVERVIEW_MATCH };
        Fragment f1 = HockeyMatchOverviewFragment.newInstance( matchId );
        fragments = new Fragment[]{ f1 };

        super.onCreate(savedInstanceState);


    }

    @Override
    protected PagerAdapter getAdapter(FragmentManager manager) {
        PagerAdapter res = new DefaultViewPagerAdapter(manager, fragments, titles);


        return res;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(fit.cvut.org.cz.tmlibrary.R.menu.menu_finish, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == fit.cvut.org.cz.tmlibrary.R.id.action_finish){

        }

        return super.onOptionsItemSelected(item);
    }
}
