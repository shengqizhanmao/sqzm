package com.lin.common.pojo.Vo2;

public class FriendsUsersAndUserVo {
    private String id;
    private String formUserId;
    private String toUserId;
    private String status;
    private String avatar;
    private String nickname;
    private String formNickname;
    private String formAvatar;

    public String getFormNickname() {
        return formNickname;
    }

    public void setFormNickname(String formNickname) {
        this.formNickname = formNickname;
    }

    public String getFormAvatar() {
        return formAvatar;
    }

    public void setFormAvatar(String formAvatar) {
        this.formAvatar = formAvatar;
    }

    @Override
    public String toString() {
        return "FriendsUsersAndUserVo{" +
                "id='" + id + '\'' +
                ", formUserId='" + formUserId + '\'' +
                ", formNickname='" + formNickname + '\'' +
                ", formAvatar='" + formAvatar + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", status='" + status + '\'' +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }

    public String getFormUserId() {
        return formUserId;
    }

    public void setFormUserId(String formUserId) {
        this.formUserId = formUserId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
