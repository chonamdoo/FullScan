package test.movebox.fullscan;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

/**
 * Author JackSparrow
 * Create Date 01/11/2016.
 */

public class DisplayUtil {
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static float dpToPx(float dp) {
        float density = HSApplication.getContext().getResources().getDisplayMetrics().density;
        return dp * density;
    }

    public static int dpToPx(int dp) {
        float density = HSApplication.getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static DisplayMetrics getDisplayMetrics() {
        Resources resources = HSApplication.getContext().getResources();
        return resources.getDisplayMetrics();
    }

    public static float getScreenDensity() {
        Resources resources = HSApplication.getContext().getResources();
        return resources.getDisplayMetrics().density;
    }

    public static int getScreenWidth() {
        Resources resources = HSApplication.getContext().getResources();
        return resources.getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        Resources resources = HSApplication.getContext().getResources();
        return resources.getDisplayMetrics().heightPixels;
    }

    public static int getColor(int id) {
        return HSApplication.getContext().getResources().getColor(id);
    }

    public static float getDimension(int id) {
        return HSApplication.getContext().getResources().getDimension(id);
    }

    public static String getString(int id) {
        return HSApplication.getContext().getResources().getString(id);
    }
}
