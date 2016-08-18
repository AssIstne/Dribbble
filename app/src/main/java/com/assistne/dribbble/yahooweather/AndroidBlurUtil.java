package com.assistne.dribbble.yahooweather;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

/**
 * Created by assistne on 16/8/18.
 */
public class AndroidBlurUtil implements BlurAlgorithmUtil {

    private RenderScript mRenderScript;
    private ScriptIntrinsicBlur mBlurScript;
    private Bitmap mOriginBitmap;

    public AndroidBlurUtil(Context context, Bitmap originBitmap) {
        mRenderScript = RenderScript.create(context);
        mBlurScript = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript));
        mOriginBitmap = originBitmap;
    }

    @Override
    public void blur(Bitmap outputBitmap, float radius) {
        radius = Math.max(1, Math.min(25, radius));
        Allocation mInputAllocation = Allocation.createFromBitmap(mRenderScript, mOriginBitmap);
        Allocation mOutputAllocation = Allocation.createTyped(mRenderScript, mInputAllocation.getType());
        mBlurScript.setRadius(radius);
        mBlurScript.setInput(mInputAllocation);
        mBlurScript.forEach(mOutputAllocation);
        mOutputAllocation.copyTo(outputBitmap);
        mOutputAllocation.destroy();
        mInputAllocation.destroy();
    }

    @Override
    public void destroy() {
        mBlurScript.destroy();
        mRenderScript.destroy();
    }


}
