package com.lin.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-19
 */
@TableName("u_friends_user")
@ApiModel(value = "FriendsUser对象", description = "")
public class FriendsUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String formUserId;

    private String toUserId;

    @ApiModelProperty("1:好友,-1:非好友,0:申请好友中")
    private String status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FriendsUser{" +
        "id=" + id +
        ", formUserId=" + formUserId +
        ", toUserId=" + toUserId +
        ", status=" + status +
        "}";
    }
}
