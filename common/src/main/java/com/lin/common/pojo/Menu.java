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
 * @since 2023-01-05
 */
@TableName("u_menu")
@ApiModel(value = "Menu对象", description = "用户目录")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String name;

    private String icon;

    private String path;

    private String effect;

    private String modularsId;


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

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getModularsId() {
        return modularsId;
    }

    public void setModularsId(String modularsId) {
        this.modularsId = modularsId;
    }

    @NotNull
    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name=" + name +
                ", icon=" + icon +
                ", path=" + path +
                ", effect=" + effect +
                ", modularsId=" + modularsId +
                "}";
    }
}
