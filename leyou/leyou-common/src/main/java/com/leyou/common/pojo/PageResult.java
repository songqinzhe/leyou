package com.leyou.common.pojo;

import java.util.List;

public class PageResult<T> {
    private Long total;
    private Integer totalPage;
    private List<T> items;

    public PageResult() {
    }

    public PageResult(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageResult(Long total, Integer toyalPage, List<T> items) {
        this.total = total;
        this.totalPage = toyalPage;
        this.items = items;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getToyalPage() {
        return totalPage;
    }

    public void setToyalPage(Integer toyalPage) {
        this.totalPage = toyalPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
