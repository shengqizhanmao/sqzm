package com.lin.common;

import java.util.Date;

public class WebSocketPushMeg {
    private String formUserId;
    private String toUserId;
    private String msg;
    private String type;
    private Date createdDate;

    @Override
    public String toString() {
        return "WebSocketPushMeg{" +
                "formUserId='" + formUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", msg='" + msg + '\'' +
                ", type='" + type + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
