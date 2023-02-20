package com.lin.common;

public class WebSocketPushMeg {
    private String formUserId;
    private String toUserId;
    private String data;
    private String type;

    @Override
    public String toString() {
        return "WebSocketPushMeg{" +
                "formUserId='" + formUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", data='" + data + '\'' +
                ", type='" + type + '\'' +
                '}';
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
