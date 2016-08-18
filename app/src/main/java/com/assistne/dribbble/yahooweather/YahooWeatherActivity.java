package com.assistne.dribbble.yahooweather;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.assistne.dribbble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by assistne on 16/8/18.
 */
public class YahooWeatherActivity extends Activity {
    private static final String TAG = "#YahooWeatherActivity";
    @BindView(R.id.yahoo_img_bg)
    ImageView mBackgroundImg;
    @BindView(R.id.yahoo_img_blurred_bg)
    ImageView mBlurredImg;
    @BindView(R.id.yahoo_btn_blur)
    Button mBlurBtn;

    @BindView(R.id.yahoo_span_container)
    YHWeatherContainer mContainer;

    private BlurAlgorithmUtil mBlurUtil;
    private Bitmap mBlurredBitmap;
    private float mLastFraction;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yahoo_weather);
        ButterKnife.bind(this);


        final Bitmap targetBitmap = ((BitmapDrawable)mBackgroundImg.getDrawable()).getBitmap();
        mBlurUtil = new AndroidBlurUtil(this, targetBitmap);
//        mBlurUtil = new StackBlurUtil(this);
        mBlurredBitmap = targetBitmap.copy(targetBitmap.getConfig(), true);
        final ValueAnimator anim = ValueAnimator.ofFloat(1, 40f, 1);
        anim.setDuration(5000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float radius = (float) animation.getAnimatedValue();
                mBlurUtil.blur(mBlurredBitmap, radius);
                mBlurredImg.setImageBitmap(mBlurredBitmap);
            }
        });
        mBlurBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim.start();
            }
        });
        mContainer.setListener(new YHWeatherContainer.DragListener() {
            @Override
            public void onDrag(float fraction) {
                if (Math.abs(mLastFraction - fraction) >= 0.1) {
                    new MyTask().execute(fraction);
                    mLastFraction = fraction;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBlurUtil.destroy();
    }

    private class MyTask extends AsyncTask<Float, Integer, Bitmap> {

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected Bitmap doInBackground(Float... params) {
            float fraction = params[0];
            Log.d(TAG, "onDrag: " + fraction);
            float radius = fraction * 25;
            mBlurUtil.blur(mBlurredBitmap, radius);
            return mBlurredBitmap;
        }

        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(Bitmap result) {
            mBlurredImg.setImageBitmap(result);
        }
    }
}
