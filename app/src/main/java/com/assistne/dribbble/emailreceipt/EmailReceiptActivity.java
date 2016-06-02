package com.assistne.dribbble.emailreceipt;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.assistne.dribbble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by assistne on 16/5/31.
 */
public class EmailReceiptActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "#EmailReceipt";
    @BindView(R.id.email_receipt_span_header)
    ViewGroup mHeaderSpan;
    @BindView(R.id.email_receipt_img_paypal)
    ImageView mIcon;
    @BindView(R.id.email_receipt_img_line)
    View mBlueLine;
    @BindView(R.id.email_receipt_txt_name)
    TextView mNameTxt;
    @BindView(R.id.email_receipt_txt_msg)
    TextView mMsgTxt;

    @BindView(R.id.email_receipt_span_main)
    ViewGroup mMainSpan;
    @BindView(R.id.email_receipt_txt_title)
    TextView mTitleTxt;
    @BindView(R.id.email_receipt_item_0)
    ItemView mItem0;
    @BindView(R.id.email_receipt_item_1)
    ItemView mItem1;
    @BindView(R.id.email_receipt_item_2)
    ItemView mItem2;
    @BindView(R.id.email_receipt_img_line_yellow)
    View mYellowLine;
    @BindView(R.id.email_receipt_txt_total)
    TextView mTotalTxt;
    @BindView(R.id.email_receipt_txt_total_price)
    TextView mPriceTxt;

    @BindView(R.id.email_receipt_span_code)
    ViewGroup mCodeSpan;
    @BindView(R.id.email_receipt_img_dot_line_white)
    View mWhiteDotLine;

    private int mCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_receipt);
        ButterKnife.bind(this);
        mWhiteDotLine.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mIcon.setOnClickListener(this);
        mCodeSpan.setOnClickListener(this);
        mItem0.setContent("1", "t-Shirt Lacoste", "$48.00", true);
        mItem1.setContent("2", "Snickers Nike", "$125.00", true);
        mItem2.setContent("3", "All Stars", "$95.00", false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_receipt_span_code:
                mCount = 0;
                getHideAnimation().start();
                break;
        }
    }

    private Animator getShowAnimation() {
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(
                getShowHeadSpanAni(),
                getShowMainSpanAni());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCount > 10) {
                    return;
                }
                mCount += 1;
                Animator animator = getHideAnimation();
                animator.setStartDelay(700);
                animator.start();
            }
        });
        return set;
    }

    private Animator getHideAnimation() {
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(
                getHideMainSpanAni(),
                getHideHeadSpanAni());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCount > 10) {
                    return;
                }
                mCount += 1;
                Animator animator = getShowAnimation();
                animator.setStartDelay(700);
                animator.start();
            }
        });
        return set;
    }

    private Animator getShowHeadSpanAni() {
        mHeaderSpan.setAlpha(0);
        mHeaderSpan.setScaleX(0.3f);
        mHeaderSpan.setScaleY(0.3f);
        mBlueLine.setAlpha(0);
        mBlueLine.setTranslationX(100);
        mNameTxt.setAlpha(0);
        mNameTxt.setTranslationY(100);
        mMsgTxt.setAlpha(0);
        mMsgTxt.setTranslationY(100);
        AnimatorSet spanSet = new AnimatorSet();
        spanSet.playTogether(
                ObjectAnimator.ofFloat(mHeaderSpan, "scaleX", 1),
                ObjectAnimator.ofFloat(mHeaderSpan, "scaleY", 1),
                ObjectAnimator.ofFloat(mHeaderSpan, "alpha", 1));
        spanSet.setDuration(500);
        AnimatorSet nameSet = new AnimatorSet();
        nameSet.playTogether(
                ObjectAnimator.ofFloat(mNameTxt, "alpha", 1),
                ObjectAnimator.ofFloat(mNameTxt, "translationY", 0));
        AnimatorSet msgSet = new AnimatorSet();
        msgSet.playTogether(
                ObjectAnimator.ofFloat(mMsgTxt, "alpha", 1),
                ObjectAnimator.ofFloat(mMsgTxt, "translationY", 0));
        AnimatorSet txtSet = new AnimatorSet();
        txtSet.play(nameSet);
        txtSet.play(msgSet).after(100);
        AnimatorSet lineSet = new AnimatorSet();
        lineSet.playTogether(
                txtSet,
                ObjectAnimator.ofFloat(mBlueLine, "alpha", 1),
                ObjectAnimator.ofFloat(mBlueLine, "translationX", 0));
        AnimatorSet allSet = new AnimatorSet();
        allSet.playSequentially(spanSet, lineSet);
        return allSet;
    }

    private Animator getShowMainSpanAni() {
        mMainSpan.setPivotY(0);
        mCodeSpan.setPivotY(0);
        mWhiteDotLine.setPivotY(0);
        mMainSpan.setRotationX(-90);
        mWhiteDotLine.setRotationX(-90);
        mTitleTxt.setAlpha(0);
        mTitleTxt.setTranslationY(100);
        AnimatorSet titleSet = new AnimatorSet();
        titleSet.playTogether(
                ObjectAnimator.ofFloat(mTitleTxt, "alpha", 1),
                ObjectAnimator.ofFloat(mTitleTxt, "translationY", 0)
        );
        AnimatorSet allSet = new AnimatorSet();
        allSet.playSequentially(
                ObjectAnimator.ofFloat(mMainSpan, "rotationX", 0),
                ObjectAnimator.ofFloat(mWhiteDotLine, "rotationX", 0),
                getShowCodeSpanAni(),
                titleSet,
                getShowItemAni(),
                getShowTotalAni()
        );
        return allSet;
    }

    private Animator getShowItemAni() {
        long delay = 300;
        AnimatorSet itemSet = new AnimatorSet();
        Animator item0Ani = mItem0.getShowAni();
        Animator item1Ani = mItem1.getShowAni();
        Animator item2Ani = mItem2.getShowAni();
        itemSet.play(item0Ani);
        itemSet.play(item1Ani).after(delay);
        itemSet.play(item2Ani).after(delay * 2);
        return itemSet;
    }

    private Animator getShowTotalAni() {
        mTotalTxt.setAlpha(0);
        mTotalTxt.setTranslationY(200);
        mTotalTxt.setTranslationX(0);
        mPriceTxt.setAlpha(0);
        mPriceTxt.setTranslationY(200);
        mPriceTxt.setTranslationX(0);
        mYellowLine.setAlpha(0);
        mYellowLine.setTranslationY(200);
        mYellowLine.setTranslationX(0);
        AnimatorSet allSet = new AnimatorSet();
        allSet.playTogether(
                ObjectAnimator.ofFloat(mTotalTxt, "alpha", 1),
                ObjectAnimator.ofFloat(mTotalTxt, "translationY", 0),
                ObjectAnimator.ofFloat(mPriceTxt, "alpha", 1),
                ObjectAnimator.ofFloat(mPriceTxt, "translationY", 0),
                ObjectAnimator.ofFloat(mYellowLine, "alpha", 1),
                ObjectAnimator.ofFloat(mYellowLine, "translationY", 0)
        );
        return allSet;
    }

    private Animator getShowCodeSpanAni() {
        mCodeSpan.setRotationX(-90);
        return ObjectAnimator.ofFloat(mCodeSpan, "rotationX", 0);
    }

    private Animator getHideHeadSpanAni() {
        mHeaderSpan.setAlpha(1);
        AnimatorSet spanSet = new AnimatorSet();
        spanSet.playTogether(
                ObjectAnimator.ofFloat(mHeaderSpan, "scaleX", 1, 1.3f, 1f, 0.3f),
                ObjectAnimator.ofFloat(mHeaderSpan, "scaleY", 1, 1.3f, 1f, 0.3f),
                ObjectAnimator.ofFloat(mHeaderSpan, "alpha", 0));
        spanSet.setDuration(700);
        return spanSet;
    }

    private Animator getHideMainSpanAni() {
        long delay = 150;
        mMainSpan.setPivotY(0);
        mCodeSpan.setPivotY(0);
        mWhiteDotLine.setPivotY(0);
        mMainSpan.setRotationX(0);
        mTitleTxt.setAlpha(1);
        mTitleTxt.setTranslationY(0);
        AnimatorSet titleSet = new AnimatorSet();
        titleSet.playTogether(
                ObjectAnimator.ofFloat(mTitleTxt, "alpha", 0),
                ObjectAnimator.ofFloat(mTitleTxt, "translationY", 100)
        );
        AnimatorSet allSet = new AnimatorSet();
        Animator itemSet = getHideItemAni();
        allSet.play(getHideTotalAni());
        allSet.play(itemSet).after(delay);
        allSet.play(getHideCodeSpanAni()).after(itemSet);
        allSet.play(titleSet).after(itemSet);
        allSet.play(ObjectAnimator.ofFloat(mMainSpan, "rotationX", -90)).after(titleSet);
        allSet.play(ObjectAnimator.ofFloat(mWhiteDotLine, "rotationX", -90)).after(itemSet);
        return allSet;
    }

    private Animator getHideItemAni() {
        long delay = 300;
        AnimatorSet itemSet = new AnimatorSet();
        Animator item0Ani = mItem0.getHideAni();
        Animator item1Ani = mItem1.getHideAni();
        Animator item2Ani = mItem2.getHideAni();
        itemSet.play(item2Ani);
        itemSet.play(item1Ani).after(delay);
        itemSet.play(item0Ani).after(delay * 2);
        return itemSet;
    }

    private Animator getHideTotalAni() {
        long offset = -200;
        mTotalTxt.setAlpha(1);
        mTotalTxt.setTranslationX(0);
        mPriceTxt.setAlpha(1);
        mPriceTxt.setTranslationX(0);
        mYellowLine.setAlpha(1);
        mYellowLine.setTranslationX(0);
        AnimatorSet allSet = new AnimatorSet();
        allSet.playTogether(
                ObjectAnimator.ofFloat(mTotalTxt, "alpha", 0),
                ObjectAnimator.ofFloat(mTotalTxt, "translationX", offset),
                ObjectAnimator.ofFloat(mPriceTxt, "alpha", 0),
                ObjectAnimator.ofFloat(mPriceTxt, "translationX", offset),
                ObjectAnimator.ofFloat(mYellowLine, "alpha", 0),
                ObjectAnimator.ofFloat(mYellowLine, "translationX", offset)
        );
        return allSet;
    }

    private Animator getHideCodeSpanAni() {
        mCodeSpan.setRotationX(0);
        return ObjectAnimator.ofFloat(mCodeSpan, "rotationX", -90);
    }
}
