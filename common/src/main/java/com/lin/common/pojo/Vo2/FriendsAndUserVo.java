package com.lin.common.pojo.Vo2;

import java.util.Date;

public class FriendsAndUserVo {
    private String id;
    private String toUserId;
    private String nickname;
    private String username;
    private String avatar;
    private String msg;

    private Date createDate;

    @Override
    public String toString() {
        return "FriendsAndUserVo{" +
                "id='" + id + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", msg='" + msg + '\'' +
                ", createDate=" + createDate +
                '}';
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
