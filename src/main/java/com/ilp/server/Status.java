package com.ilp.server;

public enum Status {

    PENDING("pending"),
    RUNNING("running"),
    FINISHED("finished"),
    ERROR("error");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


    @Override
    public String toString() {
        return status;
    }
}
