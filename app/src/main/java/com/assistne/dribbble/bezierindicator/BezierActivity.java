package com.assistne.dribbble.bezierindicator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.assistne.dribbble.R;

/**
 * Created by assistne on 16/9/1.
 */
public class BezierActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_indicator);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
