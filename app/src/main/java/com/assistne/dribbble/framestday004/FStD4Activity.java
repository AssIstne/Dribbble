package com.assistne.dribbble.framestday004;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.TextView;

import com.assistne.dribbble.R;

/**
 * Created by assistne on 16/4/26.
 */
public class FStD4Activity extends AppCompatActivity {
    private static final String TAG = "#FStD4Activity";
    private ViewPager mViewPager;
    private AnimatorSet mShowEnvelope;
    private AnimatorSet mHideEnvelope;
    private AnimatorSet mShowMsg;
    private AnimatorSet mHideMsg;
    private ImageView mEnvelopeHead;
    private ImageView mEnvelopeBody;
    private TextView mEnvelopeMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fst_d4);
        mEnvelopeHead = (ImageView) findViewById(R.id.fst_d4_envelope_head);
        mEnvelopeBody = (ImageView) findViewById(R.id.fst_d4_envelope_body);
        mEnvelopeMsg = (TextView) findViewById(R.id.fst_d4_envelope_msg);
        final MyPageAdapter adapter = new MyPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.fst_d4_viewpager);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setPageMargin(-250);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
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
                                R.string.ny_comment, R.drawable.fst_day4_avatar2, false);
                        break;
                    case 2:
                        fragment = CardFragment.newInstance(R.drawable.fst_day4_bb, R.drawable.fst_day4_bb_star,
                                R.string.bb_comment, R.drawable.fst_day4_avatar1, false);
                        break;
                    default:
                        fragment = CardFragment.newInstance(R.drawable.fst_day4_wt, R.drawable.fst_day4_wt_star,
                                R.string.wt_comment, R.drawable.fst_day4_avatar3, true);
                        break;
                }
                mFragments.put(position, fragment);
            }
            return mFragments.get(position);
        }
    }

    public void showEnvelope() {
        if (mHideEnvelope != null) {
            mHideEnvelope.cancel();
        }
        if (mShowEnvelope == null) {
            mShowEnvelope = new AnimatorSet();

            Animator head = ObjectAnimator.ofFloat(mEnvelopeHead, "translationY", 0);
            Animator body = ObjectAnimator.ofFloat(mEnvelopeBody, "translationY", 0);
            mShowEnvelope.playTogether(head, body);
        }
        mShowEnvelope.start();
    }

    public void hideEnvelope() {
        if (mShowEnvelope != null) {
            mShowEnvelope.cancel();
        }
        if (mHideEnvelope == null) {
            mHideEnvelope = new AnimatorSet();
            float to = getResources().getDimensionPixelSize(R.dimen.fst_day4_envelope_translationY);
            Animator head = ObjectAnimator.ofFloat(mEnvelopeHead, "translationY", to);
            Animator body = ObjectAnimator.ofFloat(mEnvelopeBody, "translationY", to);
            mHideEnvelope.playTogether(head, body);
        }
        mHideEnvelope.start();
    }

    public void showMessage() {
        if (mShowMsg == null) {
            mShowMsg = new AnimatorSet();
            Animator trans = ObjectAnimator.ofFloat(mEnvelopeMsg, "translationY", 0);
            Animator alpha = ObjectAnimator.ofFloat(mEnvelopeMsg, "alpha", 1);
            alpha.setDuration(700);
            mShowMsg.playTogether(trans, alpha);
        }
        if (!mShowMsg.isRunning() && mEnvelopeMsg.getAlpha() == 0) {
            mShowMsg.start();
        }
    }

    public void hideMessage() {
        float to = getResources().getDimensionPixelSize(R.dimen.fst_day4_envelope_msg_translationY);
        if (mHideMsg == null) {
            mHideMsg = new AnimatorSet();
            Animator trans = ObjectAnimator.ofFloat(mEnvelopeMsg, "translationY", to);
            Animator alpha = ObjectAnimator.ofFloat(mEnvelopeMsg, "alpha", 0);
            alpha.setDuration(700);
            mHideMsg.playTogether(trans, alpha);
        }
        if (!mHideMsg.isRunning() && mEnvelopeMsg.getTranslationY() < to) {
            mHideMsg.start();
        }
    }
}
