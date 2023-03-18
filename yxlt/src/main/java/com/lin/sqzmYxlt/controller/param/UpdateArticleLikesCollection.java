package com.lin.sqzmYxlt.controller.param;

public class UpdateArticleLikesCollection {
    private String articleId;
    private String likes;
    private String collection;

    @Override
    public String toString() {
        return "UpdateArticleLikesCollection{" +
                "articleId='" + articleId + '\'' +
                ", likes='" + likes + '\'' +
                ", collection='" + collection + '\'' +
                '}';
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId() {
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
}
