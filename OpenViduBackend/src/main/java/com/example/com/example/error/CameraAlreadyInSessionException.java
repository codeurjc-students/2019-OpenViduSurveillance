package com.example.com.example.error;

public class CameraAlreadyInSessionException extends Exception {
    public CameraAlreadyInSessionException(String message) {
        super(message);
    }
}
