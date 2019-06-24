package com.mobiquityinc.exception;

public class APIException extends IllegalArgumentException {

    public APIException(String reason) {
        super(reason);
    }

    public APIException(String reason, Throwable cause) {
        super(reason, cause);
    }
}
