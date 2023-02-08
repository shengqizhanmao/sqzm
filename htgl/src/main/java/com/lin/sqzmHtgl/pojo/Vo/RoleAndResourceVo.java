package com.lin.sqzmHtgl.pojo.Vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lin.sqzmHtgl.pojo.Resource;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author lin
 */
public class RoleAndResourceVo {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色标识")
    private String label;

    @ApiModelProperty("角色描述")
    private String description;

    @ApiModelProperty("排序")
    private Integer sortNo;

    @ApiModelProperty("是否有效")
    private String enableFlag;

    @ApiModelProperty("角色拥有的资源权限")
    private List<Resource> listResourceName;

    @Override
    public String toString() {
        return "RoleAndResourceVo{" +
                "id='" + id + '\'' +
                ", roleName='" + roleName + '\'' +
                ", label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", sortNo=" + sortNo +
                ", enableFlag='" + enableFlag + '\'' +
                ", listResourceName=" + listResourceName +
                '}';
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public List<Resource> getListResourceName() {
        return listResourceName;
    }

    public void setListResourceName(List<Resource> listResourceName) {
        this.listResourceName = listResourceName;
    }
}
