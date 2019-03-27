package chenfeihao.com.fat_measurements_mobile.util;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/27
 */
public class ButtonUtil {
    private static long lastClickTime;

    /**
     * 判断短时间内是否快速点击
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}