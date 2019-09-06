package com.springboot.controller;

/**
 * Created by Administrator on 2017/6/27.
 */
public class ResponseData {
    private String resultStatus;
    private String resultDescription;
    private Object resultData;

    public ResponseData() {
    }

    public ResponseData(String resultStatus, String resultDescription, Object resultData) {
        this.resultStatus = resultStatus;
        this.resultDescription = resultDescription;
        this.resultData = resultData;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public Object getResultData() {
        return resultData;
    }

    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }
}
