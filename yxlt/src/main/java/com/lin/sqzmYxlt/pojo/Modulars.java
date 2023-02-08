package com.lin.sqzmYxlt.pojo;

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
 * @since 2023-02-06
 */
@TableName("u_modulars")
@ApiModel(value = "Modulars对象", description = "")
public class Modulars implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String name;

    private String icon;

    private String path;

    private String palteId;


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

    @Override
    public String toString() {
        return "Modulars{" +
        "id=" + id +
        ", name=" + name +
        ", icon=" + icon +
        ", path=" + path +
        ", palteId=" + palteId +
        "}";
    }
}
