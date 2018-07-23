package com.george.balasca.articleregistry.repository;

public class NetworkState {

    private final Status NetworkStatus;
    private final String Message;
    public static final NetworkState LOADED;
    public static final NetworkState LOADING;
    public static final NetworkState FAILED;

    public NetworkState(Status status, String message) {
        this.NetworkStatus = status;
        this.Message = message;
    }

    static {
        LOADED = new NetworkState(Status.SUCCESS,"Success :)");
        LOADING = new NetworkState(Status.RUNNING,"Loading..");
        FAILED = new NetworkState(Status.FAILED,"Failed :(");
    }

    public Status getNetworkStatus() {
        return NetworkStatus;
    }

    public String getMessage() {
        return Message;
    }

}
