package test.movebox.fullscan;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
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

    private static final long DURATION_ENTER_TAB_TRANSLATION = 3500 / 5;
    private static final long DURATION_ENTER_RING_SCALE = 5000 / 5;
    private static final long DURATION_ENTER_DESC_SCALE = 3500 / 5;
//    private static final long DURATION_ENTER_TAB_TRANSLATION = 3500;
//    private static final long DURATION_ENTER_RING_SCALE = 5000;
//    private static final long DURATION_ENTER_DESC_SCALE = 3500;

    private static final int PRIMARY_BLACK = DisplayUtil.getColor(R.color.black_primary);
    private static final int PRIMARY_RED = DisplayUtil.getColor(R.color.full_scan_red);
    private static final int PRIMARY_YELLOW = DisplayUtil.getColor(R.color.full_scan_yellow);
    private static final int PRIMARY_BLUE = DisplayUtil.getColor(R.color.full_scan_blue);

    private static final int SCREEN_WIDTH = DisplayUtil.getScreenWidth();
    private static final int SCREEN_HEIGHT = DisplayUtil.getScreenHeight();

    private static final float TOOLBAR_HEIGHT = DisplayUtil.getDimension(R.dimen.height_toolbar);
    private static final float APPBAR_HEIGHT = TOOLBAR_HEIGHT + DisplayUtil.getDimension(R.dimen.height_tab_layout_with_icon);

    private static final float FULL_SCAN_TEXT_SIZE = DisplayUtil.getDimension(R.dimen.full_scan_text);
    private static final float SCAN_ALL_ITEMS_TEXT_SIZE = DisplayUtil.getDimension(R.dimen.scan_all_items_text);

    private static String TEXT_BOOST = DisplayUtil.getString(R.string.main_tab_boost);
    private static String TEXT_CLEAN = DisplayUtil.getString(R.string.main_tab_clean);
    private static String TEXT_SECURITY = DisplayUtil.getString(R.string.main_tab_security);
    private static String TEXT_FULL_SCAN = DisplayUtil.getString(R.string.full_scan);
    private static String TEXT_SCAN_ALL_ITEMS = DisplayUtil.getString(R.string.scan_all_the_items);

    //NORMAL
    private static final float NORMAL_RING_WIDTH = DisplayUtil.dpToPx(1.5f);
    private static final float NORMAL_RED_RING_RADIUS = DisplayUtil.dpToPx(8f);
    private static final float NORMAL_YELLOW_RING_RADIUS = DisplayUtil.dpToPx(5.5f);
    private static final float NORMAL_BLUE_RING_RADIUS = DisplayUtil.dpToPx(3f);

    //ENTER
    private static final float ENTER_RING_CENTER_X = SCREEN_WIDTH - DisplayUtil.dpToPx(64f);
    private static final float ENTER_RING_CENTER_Y = TOOLBAR_HEIGHT * 0.5f;
    private static final float ENTER_RING_RADIUS = SCREEN_WIDTH * 0.42f;
    private static final float ENTER_RING_WIDTH_MAX = DisplayUtil.dpToPx(10f);
    private static final float ENTER_RING_WIDTH_MIN = DisplayUtil.dpToPx(5f);

    //SCAN
    private static final float SCAN_RING_CENTER_X = SCREEN_WIDTH * 0.5f;
    private static final float SCAN_RING_CENTER_Y = SCREEN_HEIGHT * 0.4f;
    private static final float SCAN_TAB_CIRCLE_CENTER_Y = SCREEN_HEIGHT * 0.79f;


    private Paint ringPaint;
    private Paint boostTabPaint;
    private Paint cleanTabPaint;
    private Paint securityTabPaint;
    private Paint tabStripPaint;
    private Paint descPaint;

    private Bitmap boostIcon;
    private Bitmap cleanIcon;
    private Bitmap securityIcon;

    private Point ringPoint;
    private Point boostIconPoint;
    private Point cleanIconPoint;
    private Point securityIconPoint;
    private Point boostTextPoint;
    private Point cleanTextPoint;
    private Point securityTextPoint;
    private Point fullScanTextPoint;
    private Point scanAllTextPoint;

    private RectF tabStripRectF;

    private float red_ring_radius;
    private float yellow_ring_radius;
    private float blue_ring_radius;
    private float fullScanTextSize;
    private float scanAllTextSize;

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

        descPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        descPaint.setStyle(Paint.Style.FILL);
        descPaint.setTextAlign(Paint.Align.CENTER);
        descPaint.setColor(PRIMARY_BLACK);
        descPaint.setAlpha(0);
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
                canvas.drawCircle(ringPoint.x, ringPoint.y, red_ring_radius, ringPaint);
                ringPaint.setColor(PRIMARY_YELLOW);
                canvas.drawCircle(ringPoint.x, ringPoint.y, yellow_ring_radius, ringPaint);
                ringPaint.setColor(PRIMARY_BLUE);
                canvas.drawCircle(ringPoint.x, ringPoint.y, blue_ring_radius, ringPaint);

                //Tab
                canvas.drawBitmap(boostIcon, boostIconPoint.x, boostIconPoint.y, boostTabPaint);
                canvas.drawText(TEXT_BOOST, boostTextPoint.x, boostTextPoint.y, boostTabPaint);
                canvas.drawBitmap(cleanIcon, cleanIconPoint.x, cleanIconPoint.y, cleanTabPaint);
                canvas.drawText(TEXT_CLEAN, cleanTextPoint.x, cleanTextPoint.y, cleanTabPaint);
                canvas.drawBitmap(securityIcon, securityIconPoint.x, securityIconPoint.y, securityTabPaint);
                canvas.drawText(TEXT_SECURITY, securityTextPoint.x, securityTextPoint.y, securityTabPaint);
                canvas.drawRect(tabStripRectF, tabStripPaint);

                //环内描述
                descPaint.setAlpha((int) ((fullScanTextSize / FULL_SCAN_TEXT_SIZE) * 0xDE));
                descPaint.setTextSize(fullScanTextSize);
                canvas.drawText(TEXT_FULL_SCAN, fullScanTextPoint.x, fullScanTextPoint.y, descPaint);

                descPaint.setAlpha((int) ((scanAllTextSize / SCAN_ALL_ITEMS_TEXT_SIZE) * 0x89));
                descPaint.setTextSize(scanAllTextSize);
                canvas.drawText(TEXT_SCAN_ALL_ITEMS, scanAllTextPoint.x, scanAllTextPoint.y, descPaint);

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

        //重置FullScanView的大小
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.rightMargin = 0;
        params.height = SCREEN_HEIGHT;
        params.width = SCREEN_WIDTH;
        setLayoutParams(params);

        //重绘TabLayout
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
                    cleanIconPoint = new Point(icon.getLeft() + Math.round(SCREEN_WIDTH / 3f),
                        Math.round(tabView.getTop() + ((RelativeLayout) icon.getParent()).getTop() + TOOLBAR_HEIGHT));
                    cleanTextPoint = new Point(title.getLeft() + Math.round(SCREEN_WIDTH / 3f),
                        Math.round(tabView.getTop() + title.getTop() - title.getPaint().getFontMetrics().top + TOOLBAR_HEIGHT));
                    cleanIcon = DisplayUtil.drawable2Bitmap(icon.getDrawable());
                    cleanTabPaint.setTextSize(title.getTextSize());
                    cleanTabPaint.setColor(title.getCurrentTextColor());
                    break;
                case 2:
                    securityIconPoint = new Point(icon.getLeft() + Math.round(SCREEN_WIDTH * 2f / 3f),
                        Math.round(tabView.getTop() + ((RelativeLayout) icon.getParent()).getTop() + TOOLBAR_HEIGHT));
                    securityTextPoint = new Point(title.getLeft() + Math.round(SCREEN_WIDTH * 2f / 3f),
                        Math.round(tabView.getTop() + title.getTop() - title.getPaint().getFontMetrics().top + TOOLBAR_HEIGHT));
                    securityIcon = DisplayUtil.drawable2Bitmap(icon.getDrawable());
                    securityTabPaint.setTextSize(title.getTextSize());
                    securityTabPaint.setColor(title.getCurrentTextColor());
                    break;
            }
        }

        this.selectTab = selectTab;
        TabLayout.Tab tab = tabLayout.getTabAt(selectTab);
        View tabView = tab.getCustomView();
        tabStripRectF = new RectF(selectTab * Math.round(SCREEN_WIDTH / 3f), TOOLBAR_HEIGHT + tabView.getBottom(),
            selectTab * Math.round(SCREEN_WIDTH / 3f) + Math.round(SCREEN_WIDTH / 3f), TOOLBAR_HEIGHT + tabLayout.getHeight());

        invalidate();

        final float appbarScaleY = SCREEN_HEIGHT - APPBAR_HEIGHT;
        final float tabStripRectFStartTop = tabStripRectF.top;
        final float tabStripRectFStartBottom = tabStripRectF.bottom;

        final float tabTranslation = SCAN_TAB_CIRCLE_CENTER_Y - boostIconPoint.y;
        final float tabIconTranslationStart = boostIconPoint.y;
        final float tabTextTranslationStart = boostTextPoint.y;

        final int boostTabColorStart = boostTabPaint.getColor();
        final int cleanTabColorStart = cleanTabPaint.getColor();
        final int securityTabColorStart = securityTabPaint.getColor();

        final float ringScale = ENTER_RING_RADIUS / NORMAL_RED_RING_RADIUS;
        ringPoint = new Point((int) ENTER_RING_CENTER_X, (int) ENTER_RING_CENTER_Y);

        //Tab下移动画
        ValueAnimator tabTranslationAnim = ValueAnimator.ofFloat(0, 1);
        tabTranslationAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();


                RelativeLayout.LayoutParams appBarParams = (RelativeLayout.LayoutParams) appBarLayout.getLayoutParams();
                appBarParams.height = (int) (APPBAR_HEIGHT + appbarScaleY * fraction);
                setLayoutParams(appBarParams);

                tabStripRectF.top = tabStripRectFStartTop + appbarScaleY * fraction;
                tabStripRectF.bottom = tabStripRectFStartBottom + appbarScaleY * fraction;
                tabStripPaint.setAlpha((int) ((1 - fraction * 2) < 0 ? 0 : (1 - fraction * 2) * 0xFF));

                boostIconPoint.set(boostIconPoint.x, (int) (tabIconTranslationStart + tabTranslation * fraction));
                boostTextPoint.set(boostTextPoint.x, (int) (tabTextTranslationStart + tabTranslation * fraction));
                cleanIconPoint.set(cleanIconPoint.x, (int) (tabIconTranslationStart + tabTranslation * fraction));
                cleanTextPoint.set(cleanTextPoint.x, (int) (tabTextTranslationStart + tabTranslation * fraction));
                securityIconPoint.set(securityIconPoint.x, (int) (tabIconTranslationStart + tabTranslation * fraction));
                securityTextPoint.set(securityTextPoint.x, (int) (tabTextTranslationStart + tabTranslation * fraction));

                boostTabPaint.setColorFilter(new PorterDuffColorFilter((DisplayUtil.colorEvaluate(fraction, boostTabColorStart, PRIMARY_BLACK)), PorterDuff.Mode.SRC_IN));
                cleanTabPaint.setColorFilter(new PorterDuffColorFilter((DisplayUtil.colorEvaluate(fraction, cleanTabColorStart, PRIMARY_BLACK)), PorterDuff.Mode.SRC_IN));
                securityTabPaint.setColorFilter(new PorterDuffColorFilter((DisplayUtil.colorEvaluate(fraction, securityTabColorStart, PRIMARY_BLACK)), PorterDuff.Mode.SRC_IN));
            }
        });
        tabTranslationAnim.setDuration(DURATION_ENTER_TAB_TRANSLATION).setInterpolator(new FastOutSlowInInterpolator());

        //圆环扩散动画
        ValueAnimator circleScaleAnim = ValueAnimator.ofFloat(NORMAL_RING_WIDTH, ENTER_RING_WIDTH_MAX, ENTER_RING_WIDTH_MIN);
        circleScaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();

                ringPoint.x = (int) (ENTER_RING_CENTER_X + (SCAN_RING_CENTER_X - ENTER_RING_CENTER_X) * fraction);
                ringPoint.y = (int) (ENTER_RING_CENTER_Y + (SCAN_RING_CENTER_Y - ENTER_RING_CENTER_Y) * fraction);

                ringPaint.setStrokeWidth((Float) animation.getAnimatedValue());
                red_ring_radius = NORMAL_RED_RING_RADIUS + ((NORMAL_RED_RING_RADIUS * ringScale) - NORMAL_RED_RING_RADIUS) * fraction;
                yellow_ring_radius = NORMAL_YELLOW_RING_RADIUS + ((NORMAL_YELLOW_RING_RADIUS * ringScale) - NORMAL_YELLOW_RING_RADIUS) * fraction
                    + (NORMAL_RED_RING_RADIUS - NORMAL_YELLOW_RING_RADIUS) * ringScale * fraction * fraction;
                blue_ring_radius = NORMAL_BLUE_RING_RADIUS + ((NORMAL_BLUE_RING_RADIUS * ringScale) - NORMAL_BLUE_RING_RADIUS) * fraction
                    + (NORMAL_RED_RING_RADIUS - NORMAL_BLUE_RING_RADIUS) * ringScale * fraction * fraction;
                invalidate();
            }
        });
        circleScaleAnim.setDuration(DURATION_ENTER_RING_SCALE).setInterpolator(new FastOutSlowInInterpolator());

        //环中央文字及Tab小球动画
        fullScanTextPoint = new Point();
        scanAllTextPoint = new Point();

        ValueAnimator textScaleAnim = ValueAnimator.ofFloat(0, 1);
        textScaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();

                fullScanTextSize = FULL_SCAN_TEXT_SIZE * fraction;
                scanAllTextSize = SCAN_ALL_ITEMS_TEXT_SIZE * fraction;

                fullScanTextPoint.x = ringPoint.x;
                scanAllTextPoint.x = ringPoint.x;
                fullScanTextPoint.y = ringPoint.y;
                scanAllTextPoint.y = (int) (ringPoint.y + scanAllTextSize * 1.5);
            }
        });
        textScaleAnim.setDuration(DURATION_ENTER_DESC_SCALE).setStartDelay(DURATION_ENTER_RING_SCALE - DURATION_ENTER_DESC_SCALE);
        textScaleAnim.setInterpolator(new FastOutSlowInInterpolator());

        AnimatorSet startAnimSet = new AnimatorSet();
        startAnimSet.playTogether(tabTranslationAnim, circleScaleAnim, textScaleAnim);
        startAnimSet.start();
    }
}
