package com.assistne.dribbble.stepper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by assistne on 17/3/28.
 */

public class StepperView extends View {
    public StepperView(Context context) {
        this(context, null);
    }

    public StepperView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepperView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
    }
}
