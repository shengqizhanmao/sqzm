package com.lin.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-06
 */
@TableName("u_category")
@ApiModel(value = "Category对象", description = "分类")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String avatar;

    private String categoryName;

    private String description;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    @Override
    public String toString() {
        return "Category{" +
        "id=" + id +
        ", avatar=" + avatar +
        ", categoryName=" + categoryName +
        ", description=" + description +
        "}";
    }
}
