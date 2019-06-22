package com.mobiquityinc.exception;

public class APIException extends IllegalArgumentException {

    public APIException(String s) {
        super(s);
    }

    public APIException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
