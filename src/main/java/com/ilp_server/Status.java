package com.ilp_server;

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

    public static Status fromString(String status) {
        for (Status s : Status.values()) {
            if (s.getStatus().equals(status)) {
                return s;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return status;
    }
}
