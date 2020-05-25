package com.example.entity;

public class IpCamera {
    public String rtspUri;
    public String data;
    public Boolean adaptativeBitrate;
    public Boolean onlyPlayWhenSubscribers;

    public IpCamera(){ }

    public IpCamera(String rtspUri, String data) {
        this.rtspUri = rtspUri;
        this.data = data;
        this.adaptativeBitrate = true;
        this.onlyPlayWhenSubscribers = true;
    }

    public IpCamera(String rtspUri, String data, Boolean adaptativeBitrate, Boolean onlyPlayWhenSubscribers) {
        this.rtspUri = rtspUri;
        this.data = data;
        this.adaptativeBitrate = adaptativeBitrate;
        this.onlyPlayWhenSubscribers = onlyPlayWhenSubscribers;
    }
    public String toString(){
        return "uri :" + rtspUri + " - name :" + data;
    }
}

