package test.movebox.fullscan;

import android.app.Application;
import android.content.Context;

/**
 * Author JackSparrow
 * Create Date 01/11/2016.
 */

public class HSApplication extends Application {
    private static HSApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
}
