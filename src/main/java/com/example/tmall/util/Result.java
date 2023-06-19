package com.example.tmall.util;

/**
 * 作者：cjy
 * 类名：Result
 * 全路径类名：com.example.tmall.util.Result
 * 父类或接口：
 * 描述：包含状态码、消息和可选数据的响应的结果类。
 */
public class Result {
    public static int SUCCESS_CODE = 0;
    public static int FAIL_CODE = 1;

    int code;           // 结果的状态码（0表示成功，1表示失败）
    String message;     // 描述结果的消息
    Object data;        // 结果的可选数据

    /**
     * 构造具有指定状态码、消息和数据的结果对象。
     *
     * @param code    结果的状态码
     * @param message 结果的消息
     * @param data    结果的可选数据
     */
    private Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 创建一个默认状态码且没有数据的成功结果。
     *
     * @return 成功结果
     */
    public static Result success() {
        return new Result(SUCCESS_CODE, null, null);
    }

    /**
     * 创建一个带有指定数据的成功结果。
     *
     * @param data 成功结果的数据
     * @return 带有数据的成功结果
     */
    public static Result success(Object data) {
        return new Result(SUCCESS_CODE, "", data);
    }

    /**
     * 创建一个带有指定消息且没有数据的失败结果。
     *
     * @param message 失败结果的消息
     * @return 带有消息的失败结果
     */
    public static Result fail(String message) {
        return new Result(FAIL_CODE, message, null);
    }

    /**
     * 获取结果的状态码。
     *
     * @return 状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 设置结果的状态码。
     *
     * @param code 要设置的状态码
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 获取与结果关联的消息。
     *
     * @return 结果消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置与结果关联的消息。
     *
     * @param message 要设置的消息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取结果的数据。
     *
     * @return 结果数据
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置结果的数据。
     *
     * @param data 要设置的数据
     */
    public void setData(Object data) {
        this.data = data;
    }
}
