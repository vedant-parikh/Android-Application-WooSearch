package com.vedantparikh.woosearch;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter {
    private String pane1 = "Movie";
    private String pane2 = "Best Meal";
    private String pane3 = "Dictionary";
    CharSequence header[] = {pane1, pane2, pane3};
    int count = 3;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }
    /* Swipe with Tabs - Referenced from developer.android.com and stackoverflow.com*/
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Movie_fragment firstpane = new Movie_fragment();
                return firstpane;
            case 1:
                RecipeFragment Secondpane = new RecipeFragment();
                return Secondpane;
            case 2:
                DictionaryFragment Thirdpane = new DictionaryFragment();
                return Thirdpane;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    public CharSequence getPageTitle(int position) {
        return header[position];
    }
    /* Code Referenced Over */
}
