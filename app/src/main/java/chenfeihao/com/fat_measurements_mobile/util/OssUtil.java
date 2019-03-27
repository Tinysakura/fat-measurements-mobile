package chenfeihao.com.fat_measurements_mobile.util;

import chenfeihao.com.fat_measurements_mobile.custom.OssConstant;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/27
 */
public class OssUtil {
    private static String urlFormat = "http://%s.%s/%s";

    public static String generateOssUrl(String ossKey) {
        return String.format(urlFormat, OssConstant.OSS_BUCKET_B_ULTRANSONIC, OssConstant.OSS_END_POINT, ossKey);
    }
}
