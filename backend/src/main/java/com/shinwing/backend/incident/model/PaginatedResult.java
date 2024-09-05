package com.shinwing.backend.incident.model;

import java.util.List;

public class PaginatedResult<T> {
    private List<T> items;
    private Long totalCount;

    public PaginatedResult(List<T> items, Long totalCount) {
        this.items = items;
        this.totalCount = totalCount;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}