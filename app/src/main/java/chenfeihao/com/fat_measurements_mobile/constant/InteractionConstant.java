package chenfeihao.com.fat_measurements_mobile.constant;

import android.content.Intent;

/**
 * UI交互相关枚举
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/18
 */
public class InteractionConstant {

    public enum PasswordRelatedEnum {
        PASSWORD_PLAINTEXT(0, "明文显示"),
        PASSWORD_CIPHERTEXT(1, "密文显示")
        ;

        private Integer code;
        private String message;

        PasswordRelatedEnum(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
