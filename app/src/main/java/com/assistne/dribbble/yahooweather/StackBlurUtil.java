package com.assistne.dribbble.yahooweather;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;

/**
 * Created by assistne on 16/8/18.
 */
public class StackBlurUtil implements BlurAlgorithmUtil {

    private RenderScript mRenderScript;
    private ScriptC_stackBlur mBlurScript;
    private Allocation mInputAllocation;

    public StackBlurUtil(Context context) {
        mRenderScript = RenderScript.create(context);
        mBlurScript = new ScriptC_stackBlur(mRenderScript);
    }

    @Override
    public void blur(Bitmap blurredBitmap, float radius) {
        int width = blurredBitmap.getWidth();
        int height = blurredBitmap.getHeight();
        mInputAllocation = Allocation.createFromBitmap(mRenderScript, blurredBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        mBlurScript.set_gIn(mInputAllocation);
        mBlurScript.set_width(width);
        mBlurScript.set_height(height);
        mBlurScript.set_radius((int) radius);

        int[] row_indices = new int[height];
        for (int i = 0; i < height; i++) {
            row_indices[i] = i;
        }

        Allocation rows = Allocation.createSized(mRenderScript, Element.U32(mRenderScript), height, Allocation.USAGE_SCRIPT);
        rows.copyFrom(row_indices);

        row_indices = new int[width];
        for (int i = 0; i < width; i++) {
            row_indices[i] = i;
        }

        Allocation columns = Allocation.createSized(mRenderScript, Element.U32(mRenderScript), width, Allocation.USAGE_SCRIPT);
        columns.copyFrom(row_indices);

        mBlurScript.forEach_blur_h(rows);
        mBlurScript.forEach_blur_v(columns);
        mInputAllocation.copyTo(blurredBitmap);
        rows.destroy();
        columns.destroy();
        mInputAllocation.destroy();
    }

    @Override
    public void destroy() {
        mRenderScript.destroy();
        mBlurScript.destroy();
    }
}
