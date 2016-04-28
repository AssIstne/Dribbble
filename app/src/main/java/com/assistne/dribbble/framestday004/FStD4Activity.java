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
import android.view.ViewGroup;

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
        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getItem: " + position);
            switch (position) {
                case 0:
                    return CardFragment.newInstance(R.drawable.fst_day4_wt, R.drawable.fst_day4_wt_star,
                            R.string.wt_comment, R.drawable.fst_day4_avatar3);
                case 1:
                    return CardFragment.newInstance(R.drawable.fst_day4_ny, R.drawable.fst_day4_ny_star,
                            R.string.ny_comment, R.drawable.fst_day4_avatar2);
                case 2:
                    return CardFragment.newInstance(R.drawable.fst_day4_bb, R.drawable.fst_day4_bb_star,
                            R.string.bb_comment, R.drawable.fst_day4_avatar1);
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem: " + position);
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.d(TAG, "destroyItem: " + position + "  " + object);
            super.destroyItem(container, position, object);
        }
    }
}
