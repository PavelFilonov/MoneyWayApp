package com.example.moneywayapp.model;

public class UserOperationContext {
    private User user;
    private Operation operation;

    public UserOperationContext(User user, Operation operation) {
        this.user = user;
        this.operation = operation;
    }

    public UserOperationContext() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
