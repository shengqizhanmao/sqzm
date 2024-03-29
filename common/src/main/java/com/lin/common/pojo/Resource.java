package com.lin.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

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


    @ApiModelProperty("是否叶子节点")
    private String isLeaf;

    @ApiModelProperty("资源类型")
    private String resourceType;

    @ApiModelProperty("排序")
    private Integer sortNo;

    @ApiModelProperty("描述")
    private String description;


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

    @NotNull
    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", resourceName=" + resourceName +
                ", requestPath=" + requestPath +
                ", lavel=" + lavel +
                ", isLeaf=" + isLeaf +
                ", resourceType=" + resourceType +
                ", sortNo=" + sortNo +
                ", description=" + description +
                ", isSystemRoot=" + isSystemRoot +
                ", enableFlag=" + enableFlag +
                "}";
    }
}
