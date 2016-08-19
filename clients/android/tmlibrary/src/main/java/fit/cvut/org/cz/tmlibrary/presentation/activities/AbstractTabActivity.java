package fit.cvut.org.cz.tmlibrary.presentation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import fit.cvut.org.cz.tmlibrary.R;

/**
 * Created by Vaclav on 20. 3. 2016.
 */
public abstract class AbstractTabActivity extends AbstractToolbarActivity {

    public static final String ARG_TABMODE = "arg_tabmode";

    protected ViewPager pager;

    @Override
    protected View injectView(ViewGroup parent) {
        return getLayoutInflater().inflate(R.layout.activity_abstract_tab, parent, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(getAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        int tabMode = getIntent().getIntExtra(ARG_TABMODE, -1);

        if (tabMode == TabLayout.MODE_SCROLLABLE ) {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }


        tabLayout.setupWithViewPager(pager);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    /**
     *
     * @param manager Support fragment manager needed to create adapter
     * @return Adapter with fragments for ViewPager
     */
    protected abstract PagerAdapter getAdapter(FragmentManager manager);
}
