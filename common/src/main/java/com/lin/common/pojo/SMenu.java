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
@TableName("s_menu")
@ApiModel(value = "SMenu对象", description = "系统目录")
public class SMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "s_id", type = IdType.ASSIGN_UUID)
    private String sId;

    private String path;

    private String name;

    private String icon;

    private String effect;


    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    @NotNull
    @Override
    public String toString() {
        return "SMenu{" +
        "sId=" + sId +
        ", path=" + path +
        ", name=" + name +
        ", icon=" + icon +
        ", effect=" + effect +
        "}";
    }
}
