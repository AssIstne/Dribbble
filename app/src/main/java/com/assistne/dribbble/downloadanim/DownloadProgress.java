package com.assistne.dribbble.downloadanim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * https://dribbble.com/shots/2939772--Daily-gif-Download
 * Created by assistne on 16/9/8.
 */
public class DownloadProgress extends View {
    public DownloadProgress(Context context) {
        this(context, null);
    }

    public DownloadProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setProgress(int progress) {

    }

    public void startDownload() {
        
    }
}
