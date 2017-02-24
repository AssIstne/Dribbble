package com.assistne.dribbble.dashboardnavigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.assistne.dribbble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DNavigationActivity extends AppCompatActivity {
    private static final String TAG = "#DNavigationActivity";
    @BindView(R.id.chart)
    PieChartView mPieChartView;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dnavigation);
        ButterKnife.bind(this);

        mPieChartView.setData(new float[]{5, 4.5f, 2, 4, 7});
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return ContentFragment.newInstance(PieChartView.COLOR_ARR[position]);
            }

            @Override
            public int getCount() {
                return 5;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPieChartView.rotateChart(positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                mPieChartView.setCurrentIndex(position);
            }
        });
    }
}
