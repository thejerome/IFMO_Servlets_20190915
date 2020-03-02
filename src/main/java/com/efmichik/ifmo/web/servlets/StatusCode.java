package com.efmichik.ifmo.web.servlets;

public enum StatusCode {
    UPDATED(200),
    CREATED(201),
    CALCULATED(200),
    DELETED(204),
    INCORRECT(400),
    EXCEEDED(403),
    UNCALCULATED(409);

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
