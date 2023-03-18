package com.lin.common.pojo.Vo;

import com.lin.common.pojo.Category;
import com.lin.common.pojo.Tags;

import java.util.Date;
import java.util.List;

public class ArticleNoContentVo {
    private String id;//帖子id
    private String userId;//作者id
    private String nickname;//作者姓名
    private String avatar;//作者头像
    private String title;//标题
    private String summary;//简介
    private String frontCover;//封面
    private Long commentCounts;//评论数量
    private Long viewCounts;//浏览数量
    private Long likesCounts;//点赞人数
    private Long collectionCounts;//收藏人数
    private Date createDate;//创建时间
    private String status;//状态，1为审核通过，0为审核中，-1为审核失败
    private List<Tags> tagsList;//标签
    private Category category;//分类

    @Override
    public String toString() {
        return "ArticleNoContentVo{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", frontCover='" + frontCover + '\'' +
                ", commentCounts=" + commentCounts +
                ", viewCounts=" + viewCounts +
                ", likesCounts=" + likesCounts +
                ", collectionCounts=" + collectionCounts +
                ", createDate=" + createDate +
                ", status='" + status + '\'' +
                ", tagsList=" + tagsList +
                ", category=" + category +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public List<Tags> getTagsList() {
        return tagsList;
    }

    public void setTagsList(List<Tags> tagsList) {
        this.tagsList = tagsList;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
