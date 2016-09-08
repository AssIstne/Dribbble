package com.assistne.dribbble.downloadanim;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.assistne.dribbble.R;

/**
 * Created by assistne on 16/9/8.
 */
public class DownloadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        final DownloadProgress progress = (DownloadProgress) findViewById(R.id.download_progress);
        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.startDownload();
                progress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
                        animator.setDuration(1200);
                        animator.setInterpolator(new AccelerateInterpolator());
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                progress.setProgress((Integer) animation.getAnimatedValue());
                            }
                        });
                        animator.start();
                    }
                }, 4500);
            }
        });
    }
}
