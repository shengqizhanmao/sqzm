package com.lin.sqzmHtgl.common;

import java.io.Serializable;

/**
 * @author lin
 */
public class Result implements Serializable {

    // 200是正常，非200表示异常
    private int code;
    private String msg;
    private Object data;
    private boolean success;

    public static Result succ(Object data) {
        return succ(200, "操作成功", data);
    }

    public static Result succ(String msg, Object data) {
        return succ(200, msg, data);
    }

    public static Result succ(String msg) {
        return succ(200, msg, null);
    }

    public static Result succ(int code, String msg, Object data) {
        return succ(true, code, msg, data);
    }

    public static Result succ(boolean success, int code, String msg, Object data) {
        Result r = new Result();
        r.setSuccess(success);
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result fail(String msg) {
        return fail(400, msg, null);
    }

    public static Result fail(int code, String msg) {
        return fail(code, msg, null);
    }

    public static Result fail(String msg, Object data) {
        return fail(400, msg, data);
    }

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
