package com.beans;

/**
 * Created by lurunfa on 2017/4/24.
 *
 * @author lurunfa
 * @version 1.0
 */
public class Json {

    private Boolean isSuccess;
    private String message;
    private Object object;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }
}
