package fit.cvut.org.cz.tournamentmanager.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import fit.cvut.org.cz.tmlibrary.R;
import fit.cvut.org.cz.tmlibrary.presentation.adapters.DefaultViewPagerAdapter;
import fit.cvut.org.cz.tmlibrary.presentation.fragments.DummyFragment;

/**
 * Created by Vaclav on 20. 3. 2016.
 */
public class AbstractTabActivity extends AppCompatActivity {

    private ViewPager pager;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract_tab);

        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(getAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private PagerAdapter getAdapter(FragmentManager manager) {
        return new DefaultViewPagerAdapter(manager, new Fragment[] {new DummyFragment(), new DummyFragment()}, new String[] {"dummy1", "dummy2"});
    }
}
