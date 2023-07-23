package com.lin.common;

public class WebSocketGetMeg {
    private String formUserId;
    private String toUserId;
    private String msg;

    private String type;

    @Override
    public String toString() {
        return "WebSocketGetMeg{" +
                "formUserId='" + formUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", msg='" + msg + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormUserId() {
        return formUserId;
    }

    public void setFormUserId(String formUserId) {
        this.formUserId = formUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
