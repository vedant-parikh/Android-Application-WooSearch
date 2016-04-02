package com.vedantparikh.woosearch;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity {
    PagerAdapter codePagerAdapter;
    ViewPager mobileViewPager;
    PagerTitleStrip PageHeading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PageHeading = (PagerTitleStrip) findViewById(R.id.pageStrip);
        codePagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mobileViewPager = (ViewPager) findViewById(R.id.pager);
        mobileViewPager.setAdapter(codePagerAdapter);
    }


}