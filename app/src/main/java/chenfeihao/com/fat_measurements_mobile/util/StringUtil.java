package chenfeihao.com.fat_measurements_mobile.util;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/17
 */
public class StringUtil {

    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }

        return str.equals("");
    }
}
