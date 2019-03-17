package chenfeihao.com.fat_measurements_mobile.http.common;

import chenfeihao.com.fat_measurements_mobile.http.constant.ResponseCodeEnum;
import lombok.Data;

/**
 * @Author: chenfeihao@corp.netease.com
 * @Date: 2019/3/17
 */
@Data
public class ResponseView<R> {
    private int code = ResponseCodeEnum.OK.getCode();
    private String message;
    private R result;
}
