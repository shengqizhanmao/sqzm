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
@TableName("u_comment")
@ApiModel(value = "Comment对象", description = "")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("点赞")
    private String likesCounts;

    @ApiModelProperty("评论时间")
    private Date createDate;

    @ApiModelProperty("评论文章id")
    private String articleId;

    @ApiModelProperty("评论用户id")
    private String authorId;

    @ApiModelProperty("回复哪个评论的评论id")
    private String parentId;

    @ApiModelProperty("回复谁的,用户id")
    private String toUid;

    @ApiModelProperty("评论的第几层")
    private String level;

    @ApiModelProperty("评论区的第几层楼")
    private String layer;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLikesCounts() {
        return likesCounts;
    }

    public void setLikesCounts(String likesCounts) {
        this.likesCounts = likesCounts;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    @Override
    public String toString() {
        return "Comment{" +
        "id=" + id +
        ", content=" + content +
        ", likesCounts=" + likesCounts +
        ", createDate=" + createDate +
        ", articleId=" + articleId +
        ", authorId=" + authorId +
        ", parentId=" + parentId +
        ", toUid=" + toUid +
        ", level=" + level +
        ", layer=" + layer +
        "}";
    }
}
