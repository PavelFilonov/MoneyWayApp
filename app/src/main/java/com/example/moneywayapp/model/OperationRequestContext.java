package com.example.moneywayapp.model;

import java.time.LocalDateTime;

public class OperationRequestContext {
    private Long categoryId;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;

    public OperationRequestContext(Long categoryId, LocalDateTime fromDate, LocalDateTime toDate) {
        this.categoryId = categoryId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public OperationRequestContext() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }
}
