package com.metronome.util;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;

public class ContextManager {
    private static Context context;

    public static void setContext(Context contextPram){
        context = contextPram;
    }

    public static Context getContext() {
        return context;
    }

    public static Point getScreenSize(){
        Point size = new Point();
        DisplayMetrics displayMetrics = context.getApplicationContext().getResources().getDisplayMetrics();
        size.x = displayMetrics.widthPixels;
        size.y = displayMetrics.heightPixels;
        return size;
    }
}
