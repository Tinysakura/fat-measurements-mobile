package chenfeihao.com.fat_measurements_mobile.util;

/**
 * 自定义的可改变输出级别的日志类
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/18
 */
public final class LogUtil {

    //all、verbose、debug、info、warn、err Log print on-off
    private final static boolean all = true;
    private final static boolean i = true;
    private final static boolean d = true;
    private final static boolean e = true;
    private final static boolean v = true;
    private final static boolean w = true;
    //default print tag
    private final static String defaultTag = "TAG";

    public static void V(String msg) {
        if (all && v) {
            android.util.Log.v(defaultTag, msg);
        }
    }

    public static void V(String tag, String msg) {
        if (all && v) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void D(String msg) {
        if (all && d) {
            android.util.Log.d(defaultTag, msg);
        }
    }

    public static void D(String tag, String msg) {
        if (all && d) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void I(String msg) {
        if (all && i) {
            android.util.Log.i(defaultTag, msg);
        }
    }

    public static void I(String tag, String msg) {
        if (all && i) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void W(String msg) {
        if (all && w) {
            android.util.Log.w(defaultTag, msg);
        }
    }

    public static void W(String tag, String msg) {
        if (all && w) {
            android.util.Log.w(tag, msg);
        }
    }

    public static void E(String msg) {
        if (all && e) {
            android.util.Log.e(defaultTag, msg);
        }
    }

    public static void E(String tag, String msg) {
        if (all && e) {
            android.util.Log.e(tag, msg);
        }
    }

    //log长度大于4K 限制
    public static void longI(String tag, String msg) {
        if (all && i) {
            int lenLimit = 4000, endPos;
            if (null != msg && msg.length() < lenLimit) {
                I(msg);
                return;
            }

            for (int i = 0, len = msg.length(); i < len; i += lenLimit) {
                endPos = (i + lenLimit) < len ? (i + lenLimit) : len;
                I(msg.substring(i, endPos));
            }
        }
    }

}
