package cn.icatw.blog.excetion;


import cn.icatw.blog.common.ResultStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 业务异常
 *
 * @author 王顺
 * @date 2024/03/27
 */
@Getter
@AllArgsConstructor
public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code = ResultStatusEnum.SYSTEM_EXCEPTION.getCode();

    /**
     * 错误信息
     */
    private final String message;

    public BizException(String message) {
        this.message = message;
    }

    public BizException(ResultStatusEnum statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getMessage();
    }


}
