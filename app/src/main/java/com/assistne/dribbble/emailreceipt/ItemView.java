package com.assistne.dribbble.emailreceipt;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.assistne.dribbble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by assistne on 16/6/1.
 */
public class ItemView extends RelativeLayout {
    private static final String TAG = "#ItemView";
    @BindView(R.id.item_name)
    TextView mNameTxt;
    @BindView(R.id.item_number)
    TextView mNumTxt;
    @BindView(R.id.item_price)
    TextView mPriceTxt;
    @BindView(R.id.item_line)
    View mLine;

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.email_receipt_item_view, this, true);
        ButterKnife.bind(this);
        mLine.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setContent(String num, String name, String price, boolean hasLine) {
        mNumTxt.setText(num);
        mNameTxt.setText(name);
        mPriceTxt.setText(price);
        mLine.setVisibility(hasLine ? VISIBLE : GONE);
    }

    public void show() {
        Log.d(TAG, "show: ");
        setAllAlpha(0);
        prepareForShow();
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSet numSet = new AnimatorSet();
        ObjectAnimator numAlphaAni = ObjectAnimator.ofFloat(mNumTxt, "alpha", 1).setDuration(500);
        ObjectAnimator numTraAni = ObjectAnimator.ofFloat(mNumTxt, "translationX", 0).setDuration(300);
        AnimatorSet nameSet = new AnimatorSet();
        ObjectAnimator nameAlphaAni = ObjectAnimator.ofFloat(mNameTxt, "alpha", 1).setDuration(500);
        ObjectAnimator nameTraAni = ObjectAnimator.ofFloat(mNameTxt, "translationX", 0).setDuration(300);
        nameSet.playTogether(nameAlphaAni, nameTraAni);
        AnimatorSet priceSet = new AnimatorSet();
        ObjectAnimator priceAlphaAni = ObjectAnimator.ofFloat(mPriceTxt, "alpha", 1).setDuration(500);
        ObjectAnimator priceTraAni = ObjectAnimator.ofFloat(mPriceTxt, "translationX", 0).setDuration(300);
        priceSet.playTogether(priceAlphaAni, priceTraAni);
        AnimatorSet lineSet = null;
        if (mLine.getVisibility() == VISIBLE) {
            lineSet = new AnimatorSet();
            ObjectAnimator lineAlphaAni = ObjectAnimator.ofFloat(mLine, "alpha", 1).setDuration(700);
            ObjectAnimator lineTraXAni = ObjectAnimator.ofFloat(mLine, "translationX", 0).setDuration(500);
            ObjectAnimator lineTraYAni = ObjectAnimator.ofFloat(mLine, "translationY", 0).setDuration(500);
            lineSet.playTogether(lineAlphaAni, lineTraXAni, lineTraYAni);

        }
        numSet.playTogether(numAlphaAni, numTraAni, lineSet);
        animatorSet.playSequentially(numSet, nameSet, priceSet);
        animatorSet.start();
    }

    public void hide() {
        Log.d(TAG, "hide: ");
        float offset = -40;
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator numAlphaAni = ObjectAnimator.ofFloat(mNumTxt, "alpha", 0);
        ObjectAnimator numTraAni = ObjectAnimator.ofFloat(mNumTxt, "translationX", offset);
        ObjectAnimator nameAlphaAni = ObjectAnimator.ofFloat(mNameTxt, "alpha", 0);
        ObjectAnimator nameTraAni = ObjectAnimator.ofFloat(mNameTxt, "translationX", offset);
        ObjectAnimator priceAlphaAni = ObjectAnimator.ofFloat(mPriceTxt, "alpha", 0);
        ObjectAnimator priceTraAni = ObjectAnimator.ofFloat(mPriceTxt, "translationX", offset);
        ObjectAnimator lineAlphaAni = null;
        ObjectAnimator lineTraYAni = null;
        if (mLine.getVisibility() == VISIBLE) {
            lineAlphaAni = ObjectAnimator.ofFloat(mLine, "alpha", 0);
            lineTraYAni = ObjectAnimator.ofFloat(mLine, "translationY", -offset/2);
        }
        animatorSet.setDuration(400);
        animatorSet.playTogether(numAlphaAni, numTraAni,
                nameAlphaAni, nameTraAni,
                priceAlphaAni, priceTraAni,
                lineAlphaAni, lineTraYAni);
        animatorSet.start();
    }

    private void setAllAlpha(float alpha) {
        mNumTxt.setAlpha(alpha);
        mNameTxt.setAlpha(alpha);
        mPriceTxt.setAlpha(alpha);
        if (mLine.getVisibility() == VISIBLE) {
            mLine.setAlpha(alpha);
        }
    }

    private void prepareForShow() {
        float offset = -60;
        mNumTxt.setTranslationX(offset);
        mNameTxt.setTranslationX(offset);
        mPriceTxt.setTranslationX(offset);
        if (mLine.getVisibility() == VISIBLE) {
            mLine.setTranslationX(offset * 2);
            mLine.setTranslationY(-offset);
        }
    }
}
