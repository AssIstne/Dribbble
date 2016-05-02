package com.assistne.dribbble.framestday004;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;

import com.assistne.dribbble.R;

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
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setPageMargin(-250);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: " + position);
                FragmentPagerAdapter adapter = (FragmentPagerAdapter) mViewPager.getAdapter();
                for (int i = 0; i < adapter.getCount(); i ++) {
                    CardFragment fragment = (CardFragment) adapter.getItem(i);
                    if (i == position) {
                        fragment.show();
                    } else {
                        fragment.hide();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private class MyPageAdapter extends FragmentPagerAdapter {
        SparseArray<Fragment> mFragments;
        public MyPageAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new SparseArray<>();
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            if (mFragments.get(position) == null) {
                Fragment fragment;
                switch (position) {
                    case 1:
                        fragment = CardFragment.newInstance(R.drawable.fst_day4_ny, R.drawable.fst_day4_ny_star,
                                R.string.ny_comment, R.drawable.fst_day4_avatar2);
                        break;
                    case 2:
                        fragment = CardFragment.newInstance(R.drawable.fst_day4_bb, R.drawable.fst_day4_bb_star,
                                R.string.bb_comment, R.drawable.fst_day4_avatar1);
                        break;
                    default:
                        fragment = CardFragment.newInstance(R.drawable.fst_day4_wt, R.drawable.fst_day4_wt_star,
                                R.string.wt_comment, R.drawable.fst_day4_avatar3);
                        break;
                }
                mFragments.put(position, fragment);
            }
            return mFragments.get(position);
        }
    }
}
