package com.dev.job.dto;

public class ApiResponse <T> {
    int code;
    String message;
    T data;
}
