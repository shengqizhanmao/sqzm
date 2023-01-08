package com.lin.sqzmHtgl.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author linShengWei
 * @since 2023-01-05
 */
@TableName("sh_resource")
@ApiModel(value = "Resource对象", description = "资源表")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("父资源")
    private String parentId;

    @ApiModelProperty("资源名称")
    private String resourceName;

    @ApiModelProperty("资源路径")
    private String requestPath;

    @ApiModelProperty("资源标签")
    private String lavel;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("是否叶子节点")
    private String isLeaf;

    @ApiModelProperty("资源类型")
    private String resourceType;

    @ApiModelProperty("排序")
    private Integer sortNo;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("系统code")
    private String systemCode;

    @ApiModelProperty("是否根节点")
    private String isSystemRoot;

    @ApiModelProperty("是否有效")
    private String enableFlag;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public String getLavel() {
        return lavel;
    }

    public void setLavel(String lavel) {
        this.lavel = lavel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Integer getSortNo() {
        return sortNo;
    }

    public void setSortNo(Integer sortNo) {
        this.sortNo = sortNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getIsSystemRoot() {
        return isSystemRoot;
    }

    public void setIsSystemRoot(String isSystemRoot) {
        this.isSystemRoot = isSystemRoot;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    @Override
    public String toString() {
        return "Resource{" +
        "id=" + id +
        ", parentId=" + parentId +
        ", resourceName=" + resourceName +
        ", requestPath=" + requestPath +
        ", lavel=" + lavel +
        ", icon=" + icon +
        ", isLeaf=" + isLeaf +
        ", resourceType=" + resourceType +
        ", sortNo=" + sortNo +
        ", description=" + description +
        ", systemCode=" + systemCode +
        ", isSystemRoot=" + isSystemRoot +
        ", enableFlag=" + enableFlag +
        "}";
    }
}
