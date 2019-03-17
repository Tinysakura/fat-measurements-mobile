package chenfeihao.com.fat_measurements_mobile.constant.enums;

/**
 * 新用户注册步骤枚举
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/17
 */
public enum RegisterStepEnum {
    ENTER_USER_NAME(1, "输入用户名"),
    ENTER_USER_PWD(2, "输入密码"),
    AFFIRM_USER_PWD(3, "确认密码"),
    REGISTER(4, "注册")
    ;

    private Integer code;
    private String step;

    RegisterStepEnum(Integer code, String step) {
        this.code = code;
        this.step = step;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}
