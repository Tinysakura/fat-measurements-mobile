package chenfeihao.com.fat_measurements_mobile.http.constant;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/17
 */
public enum ResponseCodeEnum {
    /**
     * 正常响应
     */
    OK(0, "正常响应"),
    /**
     * 服务器内部错误
     */
    ERROR(-1, "服务器内部错误"),
    /**
     * 用户未登录
     */
    NO_LOGIN(-2, "用户未登录"),
    /**
     * 登录失败无此用户
     */
    NO_USER(-3, "登录失败无此用户"),
    /**
     * 登录失败密码错误
     */
    PWD_ERROR(-4, "登录失败密码错误")
    ;

    private Integer code;
    private String value;

    ResponseCodeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
