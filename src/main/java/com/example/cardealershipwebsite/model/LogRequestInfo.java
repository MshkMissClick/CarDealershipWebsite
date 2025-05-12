package com.example.cardealershipwebsite.model;

import java.io.File;

/**Info.*/
public class LogRequestInfo {

    /**Status.*/
    public enum Status {
        PENDING,
        DONE,
        ERROR
    }

    private String requestId;
    private String date;
    private Status status;
    private File file;
    private String errorMessage;

    /**Constructor empty.*/
    public LogRequestInfo() {}

    /**Constructor.*/
    public LogRequestInfo(String requestId, String date, Status status) {
        this.requestId = requestId;
        this.date = date;
        this.status = status;
    }

    /**Constructor.*/
    public LogRequestInfo(String requestId, String date, Status status, File file, String errorMessage) {
        this.requestId = requestId;
        this.date = date;
        this.status = status;
        this.file = file;
        this.errorMessage = errorMessage;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}