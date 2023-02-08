package com.lin.sqzmYxlt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
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
@TableName("u_article")
@ApiModel(value = "Article对象", description = "")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("简介")
    private String summary;

    @ApiModelProperty("评论数量")
    private String commentCounts;

    @ApiModelProperty("浏览数量")
    private String viewCounts;

    @ApiModelProperty("是否置顶")
    private String weight;

    @ApiModelProperty("点赞人数")
    private String likesCounts;

    @ApiModelProperty("收藏人数")
    private String collectionCounts;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("作者id")
    private String authorId;

    @ApiModelProperty("分类id")
    private String categoryId;

    @ApiModelProperty("内容id")
    private String bodyId;

    @ApiModelProperty("模块id")
    private String modularsId;

    @ApiModelProperty("板块id")
    private String plateId;


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

    public String getCommentCounts() {
        return commentCounts;
    }

    public void setCommentCounts(String commentCounts) {
        this.commentCounts = commentCounts;
    }

    public String getViewCounts() {
        return viewCounts;
    }

    public void setViewCounts(String viewCounts) {
        this.viewCounts = viewCounts;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getLikesCounts() {
        return likesCounts;
    }

    public void setLikesCounts(String likesCounts) {
        this.likesCounts = likesCounts;
    }

    public String getCollectionCounts() {
        return collectionCounts;
    }

    public void setCollectionCounts(String collectionCounts) {
        this.collectionCounts = collectionCounts;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
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

    @Override
    public String toString() {
        return "Article{" +
        "id=" + id +
        ", title=" + title +
        ", summary=" + summary +
        ", commentCounts=" + commentCounts +
        ", viewCounts=" + viewCounts +
        ", weight=" + weight +
        ", likesCounts=" + likesCounts +
        ", collectionCounts=" + collectionCounts +
        ", createDate=" + createDate +
        ", authorId=" + authorId +
        ", categoryId=" + categoryId +
        ", bodyId=" + bodyId +
        ", modularsId=" + modularsId +
        ", plateId=" + plateId +
        "}";
    }
}
