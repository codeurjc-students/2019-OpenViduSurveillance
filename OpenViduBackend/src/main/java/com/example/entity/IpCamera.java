package com.example.entity;

public class IpCamera {
    public String rtspUri;
    public String data;
    public Boolean adaptativeBitrate;
    public Boolean onlyPlayWhenSubscribers;

    public IpCamera(String rtspUri) {
        this.rtspUri = rtspUri;
    }

    public IpCamera(String rtspUri, String data, Boolean adaptativeBitrate, Boolean onlyPlayWhenSubscribers) {
        this.rtspUri = rtspUri;
        this.data = data;
        this.adaptativeBitrate = adaptativeBitrate;
        this.onlyPlayWhenSubscribers = onlyPlayWhenSubscribers;
    }
}

