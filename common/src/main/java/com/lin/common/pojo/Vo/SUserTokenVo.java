package com.lin.common.pojo.Vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lin
 */
public class SUserTokenVo {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("登录名称")
    private String userName;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("是否有效")
    private String enableFlag;

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "UserTokenVo{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex='" + sex + '\'' +
                ", enableFlag='" + enableFlag + '\'' +
                '}';
    }
}
