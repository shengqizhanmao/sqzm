package com.lin.sqzmHtgl.pojo.Vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author lin
 */
public class UserVo {
    @ApiModelProperty("用户id")
    private String id;
    @ApiModelProperty("账号")
    private String username;
    @ApiModelProperty("用户名称")
    private String nickname;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("性别,1为男,2为女,3为不明")
    private String gender;
    @ApiModelProperty("创建时间")
    private Date createDate;
    @ApiModelProperty("状态,1为正常用户,2为封禁状态")
    private String enableFlag;

    @Override
    public String toString() {
        return "UserVo{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", createDate=" + createDate +
                ", enableFlag='" + enableFlag + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
