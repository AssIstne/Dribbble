package com.assistne.dribbble.emailreceipt;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
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

    public AnimatorSet getShowAni() {
        setAllAlpha(0);
        prepareForShow();
        long duration = 200;
        long delay = 150;
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSet numSet = new AnimatorSet();
        ObjectAnimator numAlphaAni = ObjectAnimator.ofFloat(mNumTxt, "alpha", 1).setDuration(duration);
        ObjectAnimator numTraAni = ObjectAnimator.ofFloat(mNumTxt, "translationX", 0).setDuration(duration);
        AnimatorSet nameSet = new AnimatorSet();
        ObjectAnimator nameAlphaAni = ObjectAnimator.ofFloat(mNameTxt, "alpha", 1).setDuration(duration);
        ObjectAnimator nameTraAni = ObjectAnimator.ofFloat(mNameTxt, "translationX", 0).setDuration(duration);
        nameSet.playTogether(nameAlphaAni, nameTraAni);
        AnimatorSet priceSet = new AnimatorSet();
        ObjectAnimator priceAlphaAni = ObjectAnimator.ofFloat(mPriceTxt, "alpha", 1).setDuration(duration);
        ObjectAnimator priceTraAni = ObjectAnimator.ofFloat(mPriceTxt, "translationX", 0).setDuration(duration);
        priceSet.playTogether(priceAlphaAni, priceTraAni);
        if (mLine.getVisibility() == VISIBLE) {
            AnimatorSet lineSet = new AnimatorSet();
            ObjectAnimator lineAlphaAni = ObjectAnimator.ofFloat(mLine, "alpha", 1).setDuration(duration);
            ObjectAnimator lineTraXAni = ObjectAnimator.ofFloat(mLine, "translationX", 0).setDuration(duration);
            ObjectAnimator lineTraYAni = ObjectAnimator.ofFloat(mLine, "translationY", 0).setDuration(duration);
            lineSet.playTogether(lineAlphaAni, lineTraXAni, lineTraYAni);
            numSet.playTogether(numAlphaAni, numTraAni, lineSet);
        } else {
            numSet.playTogether(numAlphaAni, numTraAni);
        }
        animatorSet.play(numSet);
        animatorSet.play(nameSet).after(delay);
        animatorSet.play(priceSet).after(delay);
        return animatorSet;
    }

    public AnimatorSet getHideAni() {
        float offset = -40;
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator numAlphaAni = ObjectAnimator.ofFloat(mNumTxt, "alpha", 0);
        ObjectAnimator numTraAni = ObjectAnimator.ofFloat(mNumTxt, "translationX", offset);
        ObjectAnimator nameAlphaAni = ObjectAnimator.ofFloat(mNameTxt, "alpha", 0);
        ObjectAnimator nameTraAni = ObjectAnimator.ofFloat(mNameTxt, "translationX", offset);
        ObjectAnimator priceAlphaAni = ObjectAnimator.ofFloat(mPriceTxt, "alpha", 0);
        ObjectAnimator priceTraAni = ObjectAnimator.ofFloat(mPriceTxt, "translationX", offset);
        if (mLine.getVisibility() == VISIBLE) {
            ObjectAnimator lineAlphaAni = ObjectAnimator.ofFloat(mLine, "alpha", 0);
            ObjectAnimator lineTraYAni = ObjectAnimator.ofFloat(mLine, "translationY", -offset/2);
            animatorSet.playTogether(numAlphaAni, numTraAni,
                    nameAlphaAni, nameTraAni,
                    priceAlphaAni, priceTraAni,
                    lineAlphaAni, lineTraYAni);
        } else {
            animatorSet.playTogether(numAlphaAni, numTraAni,
                    nameAlphaAni, nameTraAni,
                    priceAlphaAni, priceTraAni);
        }
        animatorSet.setDuration(400);

        return animatorSet;
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
