package com.lin.common.pojo.Vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author lin
 */
public class CommentVo {
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("评论用户")
    private UserVo author;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("评论时间")
    private Date createDate;

    @ApiModelProperty("评论文章id")
    private String articleId;

    private String parentId;
    @ApiModelProperty("评论的第几层")
    private Long level;

    @ApiModelProperty("评论的第几层楼")
    private Long layer;

    @ApiModelProperty("对该用户进行评论")
    private UserVo toUser;

    private List<CommentVo> childrens;

    public List<CommentVo> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<CommentVo> childrens) {
        this.childrens = childrens;
    }

    @Override
    public String toString() {
        return "CommentVo{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", createDate=" + createDate +
                ", articleId='" + articleId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", level=" + level +
                ", layer=" + layer +
                ", toUser=" + toUser +
                ", childrens=" + childrens +
                '}';
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

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

    public UserVo getAuthor() {
        return author;
    }

    public void setAuthor(UserVo author) {
        this.author = author;
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

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public Long getLayer() {
        return layer;
    }

    public void setLayer(Long layer) {
        this.layer = layer;
    }

    public UserVo getToUser() {
        return toUser;
    }

    public void setToUser(UserVo toUser) {
        this.toUser = toUser;
    }
}
