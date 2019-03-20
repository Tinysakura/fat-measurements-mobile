package chenfeihao.com.fat_measurements_mobile.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/20
 */
public class DensityUtil {
    //******************************************************************************************
    //                                                                                    单位转换
    //******************************************************************************************
    /**像素密度*/
    public static float getDisplayMetrics(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }
    /**  dp 转成为 px     */
    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }
    /**  px 转成为 dp     */
    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / getDisplayMetrics(context) + 0.5f);
    }
    /** sp转px */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }
    /** px转sp */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
    //******************************************************************************************
    //                                                                                屏幕宽高
    //******************************************************************************************
    /**  获取屏幕宽  */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }
    /** 获取屏幕高，包含状态栏，但不包含虚拟按键，如1920屏幕只有1794  */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }
    /**  获取屏幕宽  */
    public static int getScreenWidth2(Context context) {
        Point point = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
        return point.x;
    }
    /**  获取屏幕高，包含状态栏，但不包含某些手机最下面的【HOME键那一栏】，如1920屏幕只有1794  */
    public static int getScreenHeight2(Context context) {
        Point point = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(point);
        return point.y;
    }
    /**  获取屏幕原始尺寸高度，包括状态栏以及虚拟功能键高度  */
    public static int getAllScreenHeight(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            Method method = Class.forName("android.view.Display").getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            return displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    //******************************************************************************************
    //                                                                        状态栏、标题栏、虚拟按键
    //******************************************************************************************
    /** 状态栏高度，单位px，一般为25dp  */
    public static int getStatusBarHeight(Context context) {
        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }
    /** 状态栏高度，单位px，【注意】要在onWindowFocusChanged中获取才可以 */
    public static int getStatusBarHeight2(Activity activity) {
        Rect rect = new Rect();
        //DecorView是Window中的最顶层view，可以从DecorView获取到程序显示的区域，包括标题栏，但不包括状态栏。所以状态栏的高度即为显示区域的top坐标值
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }
    /**标题栏的高度，【注意】要在onWindowFocusChanged中获取才可以*/
    public static int getTitleBarHeight(Activity activity) {
        int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return contentTop - getStatusBarHeight(activity);
    }
    /**获取 虚拟按键的高度     */
    public static int getBottomBarHeight(Context context) {
        return getAllScreenHeight(context) - getScreenHeight(context);
    }
}
