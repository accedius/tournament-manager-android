package fit.cvut.org.cz.tmlibrary.presentation.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * Created by Vaclav on 20. 3. 2016.
 */
public class DefaultViewPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] fragments;
    private String[] titles;
    private String[] tags;


    public DefaultViewPagerAdapter(FragmentManager fm, Fragment[] fragments , String[] titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
        tags = new String[fragments.length];
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }
    public String getTag(int position) {
        return tags[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        tags[position] = fragment.getTag();
        return fragment;
    }
}
