package com.assistne.dribbble.emailreceipt;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
    @BindView(R.id.email_receipt_txt_title)
    TextView mTitleTxt;
    @BindView(R.id.email_receipt_item_0)
    ItemView mItem0;
    @BindView(R.id.email_receipt_item_1)
    ItemView mItem1;
    @BindView(R.id.email_receipt_item_2)
    ItemView mItem2;
    @BindView(R.id.email_receipt_span_main)
    ViewGroup mMainSpan;
    @BindView(R.id.email_receipt_span_code)
    ViewGroup mCodeSpan;

    private boolean mHasShow = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_receipt);
        ButterKnife.bind(this);
        mIcon.setOnClickListener(this);
        mCodeSpan.setOnClickListener(this);
        mItem0.setContent("1", "t-Shirt Lacoste", "$48.00", true);
        mItem1.setContent("2", "Snickers Nike", "$125.00", true);
        mItem2.setContent("3", "All Stars", "$95.00", false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_receipt_img_paypal:
                if (!mHasShow) {
                    startShowAnimation();
                } else {
                    startHideAnimation();
                }
                break;
            case R.id.email_receipt_span_code:
                showHeadSpan();
        }
    }

    private void startShowAnimation() {
        mHasShow = true;
        mItem0.show();
    }

    private void startHideAnimation() {
        mHasShow = false;
        mItem0.hide();
    }

    private void showHeadSpan() {
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
        txtSet.playSequentially(nameSet, msgSet);
        AnimatorSet lineSet = new AnimatorSet();
        lineSet.playTogether(
                txtSet,
                ObjectAnimator.ofFloat(mBlueLine, "alpha", 1),
                ObjectAnimator.ofFloat(mBlueLine, "translationX", 0));
        AnimatorSet allSet = new AnimatorSet();
        allSet.playSequentially(spanSet, lineSet);
        allSet.start();
        mMainSpan.setPivotY(0);
        Log.d(TAG, "showHeadSpan: " + mMainSpan.getPivotX() + "  " + mMainSpan.getPivotY() + "  " + mMainSpan.getHeight());
        mMainSpan.setRotationX(-45);
    }
}
