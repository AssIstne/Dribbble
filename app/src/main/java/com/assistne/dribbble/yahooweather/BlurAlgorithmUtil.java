package com.assistne.dribbble.yahooweather;

import android.graphics.Bitmap;

/**
 * Created by assistne on 16/8/18.
 */
public interface BlurAlgorithmUtil {
    void blur(Bitmap blurredBitmap, float radius);
    void destroy();
}
