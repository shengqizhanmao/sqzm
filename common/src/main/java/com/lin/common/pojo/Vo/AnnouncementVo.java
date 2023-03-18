package com.lin.common.pojo.Vo;

import com.lin.common.pojo.Modulars;

public class AnnouncementVo {
    private String id;

    private String palteId;

    private Modulars modulars;

    private String content;

    @Override
    public String toString() {
        return "AnnouncementVo{" +
                "id='" + id + '\'' +
                ", palteId='" + palteId + '\'' +
                ", modulars=" + modulars +
                ", content='" + content + '\'' +
                '}';
    }

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

    public Modulars getModulars() {
        return modulars;
    }

    public void setModulars(Modulars modulars) {
        this.modulars = modulars;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
