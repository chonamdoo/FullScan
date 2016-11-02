package test.movebox.fullscan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    private static final int PRIMARY_RED = DisplayUtil.getColor(R.color.primary_red);
    private static final int PRIMARY_YELLOW = DisplayUtil.getColor(R.color.primary_yellow);
    private static final int PRIMARY_BLUE = DisplayUtil.getColor(R.color.primary_blue);

    private static final float TOOLBAR_HEIGHT = DisplayUtil.getDimension(R.dimen.height_toolbar);

    private static String TEXT_BOOST = DisplayUtil.getString(R.string.main_tab_boost);
    private static String TEXT_CLEAN = DisplayUtil.getString(R.string.main_tab_clean);
    private static String TEXT_SECURITY = DisplayUtil.getString(R.string.main_tab_security);

    //NORMAL
    private static final float NORMAL_RING_WIDTH = DisplayUtil.dpToPx(1.5f);
    private static final float NORMAL_RED_RING_RADIUS = DisplayUtil.dpToPx(8f);
    private static final float NORMAL_YELLOW_RING_RADIUS = DisplayUtil.dpToPx(5.5f);
    private static final float NORMAL_BLUE_RING_RADIUS = DisplayUtil.dpToPx(3f);

    //ENTER
    private static final float ENTER_RING_CENTER_X = DisplayUtil.getScreenWidth() - DisplayUtil.dpToPx(64f);
    private static final float ENTER_RING_CENTER_Y = TOOLBAR_HEIGHT / 2;

    private Paint ringPaint;
    private Paint boostTabPaint;
    private Paint cleanTabPaint;
    private Paint securityTabPaint;
    private Paint tabStripPaint;

    private Bitmap boostIcon;
    private Bitmap cleanIcon;
    private Bitmap securityIcon;
    private Point boostIconPoint;
    private Point cleanIconPoint;
    private Point securityIconPoint;
    private Point boostTextPoint;
    private Point cleanTextPoint;
    private Point securityTextPoint;
    private RectF tabStripRectF;

    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private int selectTab;


    @State
    private int state = NORMAL;

    public FullScanView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(NORMAL_RING_WIDTH);

        boostTabPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boostTabPaint.setStyle(Paint.Style.FILL);
        cleanTabPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cleanTabPaint.setStyle(Paint.Style.FILL);
        securityTabPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        securityTabPaint.setStyle(Paint.Style.FILL);
        tabStripPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tabStripPaint.setStyle(Paint.Style.FILL);
        tabStripPaint.setColor(PRIMARY_BLUE);
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
                //环
                ringPaint.setColor(PRIMARY_RED);
                canvas.drawCircle(ENTER_RING_CENTER_X, ENTER_RING_CENTER_Y, NORMAL_RED_RING_RADIUS, ringPaint);
                ringPaint.setColor(PRIMARY_YELLOW);
                canvas.drawCircle(ENTER_RING_CENTER_X, ENTER_RING_CENTER_Y, NORMAL_YELLOW_RING_RADIUS, ringPaint);
                ringPaint.setColor(PRIMARY_BLUE);
                canvas.drawCircle(ENTER_RING_CENTER_X, ENTER_RING_CENTER_Y, NORMAL_BLUE_RING_RADIUS, ringPaint);

                //Tab
                canvas.drawBitmap(boostIcon, boostIconPoint.x, boostIconPoint.y, boostTabPaint);
                canvas.drawText(TEXT_BOOST, boostTextPoint.x, boostTextPoint.y, boostTabPaint);
                canvas.drawBitmap(cleanIcon, cleanIconPoint.x, cleanIconPoint.y, cleanTabPaint);
                canvas.drawText(TEXT_CLEAN, cleanTextPoint.x, cleanTextPoint.y, cleanTabPaint);
                canvas.drawBitmap(securityIcon, securityIconPoint.x, securityIconPoint.y, securityTabPaint);
                canvas.drawText(TEXT_SECURITY, securityTextPoint.x, securityTextPoint.y, securityTabPaint);
                canvas.drawRect(tabStripRectF, tabStripPaint);
                break;
            case SCAN:
                break;
            case OUT:
                break;
        }
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
        appBarLayout = (AppBarLayout) tabLayout.getParent();
    }

    public void startAnimation(int selectTab) {
        state = ENTER;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.rightMargin = 0;
        params.height = appBarLayout.getHeight();
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        setLayoutParams(params);

        this.selectTab = selectTab;

        for (int index = 0; index < tabLayout.getTabCount(); index++) {
            TabLayout.Tab tab = tabLayout.getTabAt(index);
            View tabView = tab.getCustomView();
            TextView title = (TextView) tabView.findViewById(R.id.title);
            ImageView icon = (ImageView) tabView.findViewById(R.id.tab_icon);
            switch (index) {
                case 0:
                    boostIconPoint = new Point(icon.getLeft(), Math.round(tabView.getTop() + ((RelativeLayout) icon.getParent()).getTop() + TOOLBAR_HEIGHT));
                    boostTextPoint = new Point(title.getLeft(), Math.round(tabView.getTop() + title.getTop() - title.getPaint().getFontMetrics().top + TOOLBAR_HEIGHT));
                    boostIcon = DisplayUtil.drawable2Bitmap(icon.getDrawable());
                    boostTabPaint.setTextSize(title.getTextSize());
                    boostTabPaint.setColor(title.getCurrentTextColor());
                    break;
                case 1:
                    cleanIconPoint = new Point(icon.getLeft() + Math.round(tabLayout.getWidth() / 3f),
                        Math.round(tabView.getTop() + ((RelativeLayout) icon.getParent()).getTop() + TOOLBAR_HEIGHT));
                    cleanTextPoint = new Point(title.getLeft() + Math.round(tabLayout.getWidth() / 3f),
                        Math.round(tabView.getTop() + title.getTop() - title.getPaint().getFontMetrics().top + TOOLBAR_HEIGHT));
                    cleanIcon = DisplayUtil.drawable2Bitmap(icon.getDrawable());
                    cleanTabPaint.setTextSize(title.getTextSize());
                    cleanTabPaint.setColor(title.getCurrentTextColor());
                    break;
                case 2:
                    securityIconPoint = new Point(icon.getLeft() + Math.round(tabLayout.getWidth() * 2f / 3f),
                        Math.round(tabView.getTop() + ((RelativeLayout) icon.getParent()).getTop() + TOOLBAR_HEIGHT));
                    securityTextPoint = new Point(title.getLeft() + Math.round(tabLayout.getWidth() * 2f / 3f),
                        Math.round(tabView.getTop() + title.getTop() - title.getPaint().getFontMetrics().top + TOOLBAR_HEIGHT));
                    securityIcon = DisplayUtil.drawable2Bitmap(icon.getDrawable());
                    securityTabPaint.setTextSize(title.getTextSize());
                    securityTabPaint.setColor(title.getCurrentTextColor());
                    break;
            }
        }

        TabLayout.Tab tab = tabLayout.getTabAt(selectTab);
        View tabView = tab.getCustomView();
        tabStripRectF = new RectF(selectTab * Math.round(tabLayout.getWidth() / 3f), TOOLBAR_HEIGHT + tabView.getBottom(),
            selectTab * Math.round(tabLayout.getWidth() / 3f) + Math.round(tabLayout.getWidth() / 3f), TOOLBAR_HEIGHT + tabLayout.getHeight());

        requestLayout();
    }
}
