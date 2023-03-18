package com.lin.sqzmYxlt.controller.param;

/**
 * @author lin
 */
public class CommentCreateParam {
    private String articleId;//评论文章id
    private String content;//评论内容
    private String parentId;//回复哪个评论的评论id
    private String toUserId;//回复谁的用户id

    @Override
    public String toString() {
        return "CommentCreateParam{" +
                "articleId='" + articleId + '\'' +
                ", content='" + content + '\'' +
                ", parentId='" + parentId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                '}';
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
}
