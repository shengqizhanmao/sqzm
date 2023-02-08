package com.lin.common.pojo.Vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author lin
 */
public class SUserVo {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    private String sId;
    @ApiModelProperty("账号")
    private String username;
    @ApiModelProperty("名称")
    private String nickname;
    @ApiModelProperty("真实姓名")
    private String realname;
    @ApiModelProperty("职位")
    private String duties;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("性别,1为男,2为女")
    private String gender;
    @ApiModelProperty("创建时间")
    private Date createDate;
    @ApiModelProperty("状态,1为正常,-1为封禁")
    private String enableFlag;
    @ApiModelProperty("最后登录时间")
    private Date loginDate;

    @Override
    public String toString() {
        return "SUserVo{" +
                "sId='" + sId + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", realname='" + realname + '\'' +
                ", duties='" + duties + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", createDate=" + createDate +
                ", enableFlag='" + enableFlag + '\'' +
                ", loginDate=" + loginDate +
                '}';
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }
}
