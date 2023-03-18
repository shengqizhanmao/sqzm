package com.lin.sqzmYxlt.controller.param;

public class GetArticle {
    private String sort; //0为最新,1为热度,2为评论最多,3为收藏数最多
    private String palteId;
    private String modularsId;
    private Long pages;
    private Long pagesSize;

    @Override
    public String toString() {
        return "GetArticle{" +
                "sort='" + sort + '\'' +
                ", palteId='" + palteId + '\'' +
                ", modularsId='" + modularsId + '\'' +
                ", pages=" + pages +
                ", pagesSize=" + pagesSize +
                '}';
    }

    public Long getPages() {
        return pages;
    }

    public void setPages(Long pages) {
        this.pages = pages;
    }

    public Long getPagesSize() {
        return pagesSize;
    }

    public void setPagesSize(Long pagesSize) {
        this.pagesSize = pagesSize;
    }

    public String getModularsId() {
        return modularsId;
    }

    public void setModularsId(String modularsId) {
        this.modularsId = modularsId;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPalteId() {
        return palteId;
    }

    public void setPalteId(String palteId) {
        this.palteId = palteId;
    }
}
