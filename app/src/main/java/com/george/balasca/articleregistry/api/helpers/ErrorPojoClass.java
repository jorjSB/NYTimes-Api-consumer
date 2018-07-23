package com.george.balasca.articleregistry.api.helpers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorPojoClass {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("errors")
    @Expose
    private String errors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
