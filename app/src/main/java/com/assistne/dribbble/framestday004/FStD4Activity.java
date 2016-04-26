package com.assistne.dribbble.framestday004;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.assistne.dribbble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by assistne on 16/4/26.
 */
public class FStD4Activity extends AppCompatActivity {
    private static final String TAG = "#FStD4Activity";
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fst_d4);
        mViewPager = (ViewPager) findViewById(R.id.fst_d4_viewpager);
        mViewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
        Log.d(TAG, "onCreate: " + mViewPager.getPageMargin());
        mViewPager.setPageMargin(-400);
    }

    private class MyPageAdapter extends FragmentPagerAdapter {

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            return new CardFragment();
        }
    }
}
