package test.movebox.fullscan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.IntDef;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Author JackSparrow
 * Create Date 01/11/2016.
 */

public class FullScanView extends View {
    @IntDef({NORMAL, ENTER, SCAN, OUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    private static final int NORMAL = 100;
    private static final int ENTER = 101;
    private static final int SCAN = 102;
    private static final int OUT = 103;

    private static final int PRIMARY_WHITE = 0xFFF5F5F5;
    private static final int PRIMARY_RED = 0xFFF44336;
    private static final int PRIMARY_YELLOW = 0xFFFFBE00;
    private static final int PRIMARY_BLUE = 0xFF4285F4;

    private static final float NORMAL_RING_WIDTH = DisplayUtil.dpToPx(1.5f);
    private static final float NORMAL_RED_RING_RADIUS = DisplayUtil.dpToPx(8f);
    private static final float NORMAL_YELLOW_RING_RADIUS = DisplayUtil.dpToPx(5.5f);
    private static final float NORMAL_BLUE_RING_RADIUS = DisplayUtil.dpToPx(3f);

    private Paint ringPaint;

    private Bitmap boostIcon;
    private Bitmap cleanIcon;
    private Bitmap securityIcon;

    @State
    private int state = NORMAL;

    public FullScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(NORMAL_RING_WIDTH);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (state) {
            case NORMAL:
                ringPaint.setColor(PRIMARY_RED);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, NORMAL_RED_RING_RADIUS, ringPaint);
                ringPaint.setColor(PRIMARY_YELLOW);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, NORMAL_YELLOW_RING_RADIUS, ringPaint);
                ringPaint.setColor(PRIMARY_BLUE);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, NORMAL_BLUE_RING_RADIUS, ringPaint);
                break;
            case ENTER:
                break;
            case SCAN:
                break;
            case OUT:
                break;
        }
    }

    public void startAnimation(int selectTab) {
        state = ENTER;

        boostIcon = DisplayUtil.drawable2Bitmap(VectorDrawableCompat.create(getResources(), R.drawable.ic_main_boost, null));
        cleanIcon = DisplayUtil.drawable2Bitmap(VectorDrawableCompat.create(getResources(), R.drawable.ic_main_clean, null));
        securityIcon = DisplayUtil.drawable2Bitmap(VectorDrawableCompat.create(getResources(), R.drawable.ic_main_security, null));
    }
}
