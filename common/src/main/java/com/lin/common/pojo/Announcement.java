package com.lin.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-25
 */
@TableName("u_announcement")
@ApiModel(value = "Announcement对象", description = "")
public class Announcement implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String palteId;

    private String modularsId;

    private String content;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPalteId() {
        return palteId;
    }

    public void setPalteId(String palteId) {
        this.palteId = palteId;
    }

    public String getModularsId() {
        return modularsId;
    }

    public void setModularsId(String modularsId) {
        this.modularsId = modularsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "id=" + id +
                ", palteId=" + palteId +
                ", modularsId=" + modularsId +
                ", content=" + content +
                "}";
    }
}
