package com.linyi.pig.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 统一响应结果类
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 时间戳
     */
    private long timestamp;


    /**
     * 构造方法
     * @param code 响应状态码
     * @param message 响应信息
     * @param data 响应数据
     */
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = new Date().getTime();
    }


    /**
     * 成功的结果
     *
     * @param data 返回的数据
     * @param <T> 泛型参数，表示返回数据的类型
     * @return 包含成功状态和数据的Result对象
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "Success", data);
    }

    /**
     * 成功的结果
     *
     * @param message 描述成功的消息
     * @param data     返回的数据
     * @param <T>      泛型参数，表示返回数据的类型
     * @return 一个包含成功消息和数据的结果对象
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    /**
     * 失败的结果
     * @param <T> 泛型参数，表示返回数据的类型
     * @return 包含失败状态的Result对象
     */
    public static <T> Result<T> error() {
        return new Result<>(500, "Error", null);
    }

    /**
     * 失败的结果
     * @param message 描述失败的信息
     * @param <T> 泛型参数，表示返回数据的类型
     * @return 包含失败状态和信息的Result对象
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    /**
     * 失败的结果
     * @param code 响应状态码
     * @param message 描述失败的消息
     * @param <T> 泛型参数，表示返回数据的类型
     * @return 包含失败状态和信息的Result对象
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }


}