package chenfeihao.com.fat_measurements_mobile.constant.enums;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/18
 */
public enum CommonCookieKeyEnum {
    /**
     * 用户登录id
     */
    LOGIN_ID("login_id"),;

    private String value;

    CommonCookieKeyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}