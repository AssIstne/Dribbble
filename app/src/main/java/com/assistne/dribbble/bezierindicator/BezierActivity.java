package com.assistne.dribbble.bezierindicator;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.assistne.dribbble.R;

/**
 * Created by assistne on 16/9/1.
 */
@SuppressWarnings("ConstantConditions")
public class BezierActivity extends AppCompatActivity {
    private static final String TAG = "#BezierActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_indicator);
        final BezierIndicatorView indicatorView = (BezierIndicatorView) findViewById(R.id.bezier_indicator);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return BezierFragment.newInstance(R.drawable.yellow_radius_bg);
                } else if (position == 1) {
                    return BezierFragment.newInstance(R.drawable.blue_radius_bg);
                } else {
                    return BezierFragment.newInstance(R.drawable.red_radius_bg);
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPageMargin(-700);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled: " +position+ "  " + positionOffset);
                if (positionOffset != 0) {
                    indicatorView.move(position, positionOffset);
                }
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2, true);
            }
        });
    }
}
