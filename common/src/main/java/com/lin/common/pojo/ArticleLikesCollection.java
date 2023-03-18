package com.lin.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author linShengWei
 * @since 2023-02-27
 */
@TableName("u_article_likes_collection")
@ApiModel(value = "ArticleLikesCollection对象", description = "")
public class ArticleLikesCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String articleId;

    @ApiModelProperty("0为无点赞,1为点赞")
    private String likes;

    @ApiModelProperty("0为无收藏,1为收藏")
    private String collection;

    private String userId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ArticleLikesCollection{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", likes=" + likes +
                ", collection=" + collection +
                ", userId=" + userId +
                "}";
    }
}
