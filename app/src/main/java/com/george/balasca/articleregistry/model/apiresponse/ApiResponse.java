
package com.george.balasca.articleregistry.model.apiresponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("copyright")
    @Expose                             // Not used yet: @Expose(deserialize = false)
    private String copyright;

    @SerializedName("response")
    @Expose
    private ResponseBody responseBody;

    // ************************************************************************************************************************

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public ResponseBody getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(ResponseBody responseBody) {
        this.responseBody = responseBody;
    }

}
