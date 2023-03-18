package com.lin.common.pojo.Vo;

import java.util.Date;
import java.util.List;

public class ArticleVo {

    private String id;//帖子id
    private String userId;//作者id
    private String nickname;//作者名
    private String title;//标题
    private String summary;//简介
    private String frontCover;//封面
    private String content;//内容
    private String contentHtml;//内容Html
    private String palteName;//板块名称
    private String modularsName;//模块名称
    private String categoryName;//分类名称
    private List<String> tagsNameList;//List标签名称
    private Long commentCounts;//评论数量
    private Long viewCounts;//浏览数量
    private Long likesCounts;//点赞人数
    private Long collectionCounts;//收藏人数
    private Date createDate;//创建时间
    private String status;//状态

    @Override
    public String toString() {
        return "ArticleVo{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", frontCover='" + frontCover + '\'' +
                ", content='" + content + '\'' +
                ", contentHtml='" + contentHtml + '\'' +
                ", palteName='" + palteName + '\'' +
                ", modularsName='" + modularsName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", tagsNameList=" + tagsNameList +
                ", commentCounts='" + commentCounts + '\'' +
                ", viewCounts='" + viewCounts + '\'' +
                ", likesCounts='" + likesCounts + '\'' +
                ", collectionCounts='" + collectionCounts + '\'' +
                ", createDate=" + createDate +
                ", status='" + status + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getPalteName() {
        return palteName;
    }

    public void setPalteName(String palteName) {
        this.palteName = palteName;
    }

    public String getModularsName() {
        return modularsName;
    }

    public void setModularsName(String modularsName) {
        this.modularsName = modularsName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getTagsNameList() {
        return tagsNameList;
    }

    public void setTagsNameList(List<String> tagsNameList) {
        this.tagsNameList = tagsNameList;
    }

    public Long getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(Long commentCounts) {
        this.commentCounts = commentCounts;
    }

    public Long getViewCounts() {
        return viewCounts;
    }

    public void setViewCounts(Long viewCounts) {
        this.viewCounts = viewCounts;
    }

    public Long getLikesCounts() {
        return likesCounts;
    }

    public void setLikesCounts(Long likesCounts) {
        this.likesCounts = likesCounts;
    }

    public Long getCollectionCounts() {
        return collectionCounts;
    }

    public void setCollectionCounts(Long collectionCounts) {
        this.collectionCounts = collectionCounts;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
