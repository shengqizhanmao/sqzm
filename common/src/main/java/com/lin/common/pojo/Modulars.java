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
 * @since 2023-01-09
 */
@TableName("u_modulars")
@ApiModel(value = "Modulars对象", description = "模块,分区")
public class Modulars implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String name;

    private String icon;

    private String path;

    private String palteId;

    private String sort;

    @NotNull
    @Override
    public String toString() {
        return "Modulars{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", path='" + path + '\'' +
                ", palteId='" + palteId + '\'' +
                ", sort='" + sort + '\'' +
                '}';
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPalteId() {
        return palteId;
    }

    public void setPalteId(String palteId) {
        this.palteId = palteId;
    }

}
