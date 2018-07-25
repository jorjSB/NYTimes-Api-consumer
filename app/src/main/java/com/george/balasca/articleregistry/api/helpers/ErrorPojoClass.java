package com.george.balasca.articleregistry.api.helpers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ErrorPojoClass {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("errors")
    @Expose
    private ArrayList<String> errors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }
}
