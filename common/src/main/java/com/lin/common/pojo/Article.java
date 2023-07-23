package com.lin.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-22
 */
@TableName("u_article")
@ApiModel(value = "Article对象", description = "")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("帖子封面")
    private String frontCover;

    @ApiModelProperty("简介")
    private String summary;

    @ApiModelProperty("评论数量")
    private Long commentCounts;

    @ApiModelProperty("浏览数量")
    private Long viewCounts;


    @ApiModelProperty("点赞人数")
    private Long likesCounts;

    @ApiModelProperty("收藏人数")
    private Long collectionCounts;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("作者id")
    private String userId;

    @ApiModelProperty("分类id")
    private String categoryId;

    @ApiModelProperty("内容id")
    private String bodyId;

    @ApiModelProperty("模块id")
    private String modularsId;

    @ApiModelProperty("板块id")
    private String plateId;

    @ApiModelProperty("帖子状态,-1审核拒绝,0审核中,1审核通过")
    private String status;


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

    public String getFrontCover() {
        return frontCover;
    }

    public void setFrontCover(String frontCover) {
        this.frontCover = frontCover;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getBodyId() {
        return bodyId;
    }

    public void setBodyId(String bodyId) {
        this.bodyId = bodyId;
    }

    public String getModularsId() {
        return modularsId;
    }

    public void setModularsId(String modularsId) {
        this.modularsId = modularsId;
    }

    public String getPlateId() {
        return plateId;
    }

    public void setPlateId(String plateId) {
        this.plateId = plateId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title=" + title +
                ", frontCover=" + frontCover +
                ", summary=" + summary +
                ", commentCounts=" + commentCounts +
                ", viewCounts=" + viewCounts +
                ", likesCounts=" + likesCounts +
                ", collectionCounts=" + collectionCounts +
                ", createDate=" + createDate +
                ", userId=" + userId +
                ", categoryId=" + categoryId +
                ", bodyId=" + bodyId +
                ", modularsId=" + modularsId +
                ", plateId=" + plateId +
                ", status=" + status +
                "}";
    }
}
