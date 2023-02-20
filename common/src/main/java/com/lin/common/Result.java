package com.lin.common;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author lin
 */
public class Result implements Serializable {

    // 200表示正常，非200表示异常
    private int code;
    private String msg;
    private Object data;
    private boolean success;

    @NotNull
    public static Result succ(Object data) {
        return succ(ResultCode.SUCCESS, "操作成功", data);
    }

    @NotNull
    public static Result succ(String msg, Object data) {
        return succ(ResultCode.SUCCESS, msg, data);
    }

    @NotNull
    public static Result succ(String msg) {
        return succ(ResultCode.SUCCESS, msg, null);
    }

    @NotNull
    public static Result succ(int code, String msg, Object data) {
        return succ(true, code, msg, data);
    }

    @NotNull
    public static Result succ(boolean success, int code, String msg, Object data) {
        Result r = new Result();
        r.setSuccess(success);
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result fail(String msg) {
        return fail(ResultCode.FAIL, msg, null);
    }

    @NotNull
    public static Result fail(int code, String msg) {
        return fail(code, msg, null);
    }

    @NotNull
    public static Result fail(String msg, Object data) {
        return fail(ResultCode.FAIL, msg, data);
    }

    @NotNull
    public static Result fail(int code, String msg, Object data) {
        return fail(false, code, msg, data);
    }

    public static Result fail(boolean success, int code, String msg, Object data) {
        Result r = new Result();
        r.setSuccess(success);
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    //get,set,toString
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @NotNull
    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ",code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
