package com.lin.sqzmHtgl.controller.param;

import java.util.List;

public class UpdateArticle {
    private String id;//id
    private String title;//标题
    private String summary;//简介
    private String frontCover;//封面
    private String palteId;//板块id
    private String modularsId;//模块id
    private String categoryId;//分类id
    private List<String> tagsList;//List标签id
    private String content;//内容
    private String contentHtml;//内容Html

    @Override
    public String toString() {
        return "AddArticle{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", frontCover='" + frontCover + '\'' +
                ", palteId='" + palteId + '\'' +
                ", modularsId='" + modularsId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", tagsList=" + tagsList +
                ", content='" + content + '\'' +
                ", contentHtml='" + contentHtml + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getFrontCover() {
        return frontCover;
    }

    public void setFrontCover(String frontCover) {
        this.frontCover = frontCover;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<String> tagsList) {
        this.tagsList = tagsList;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentHtml() {
        return contentHtml;
    }

    public void setContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }
}
