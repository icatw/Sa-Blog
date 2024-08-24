package cn.icatw.blog.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一返回对象R
 *
 * @author icatw
 * @since 2024-03-21 10:17:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code;
    private String message;
    private Boolean flag;
    private T data;

    public static <T> Result<T> ok() {
        return new Result<>(ResultStatusEnum.SUCCESS.getCode(), "操作成功", true, null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(ResultStatusEnum.SUCCESS.getCode(), "操作成功", true, data);
    }

    public static <T> Result<T> ok(T data, String msg) {
        return new Result<>(ResultStatusEnum.SUCCESS.getCode(), msg, true, data);
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        return new Result<>(code, msg, false, null);
    }

    public static <T> Result<T> fail() {
        return new Result<>(ResultStatusEnum.SYSTEM_EXCEPTION.getCode(), ResultStatusEnum.SYSTEM_EXCEPTION.getMessage(), false, null);
    }

}
