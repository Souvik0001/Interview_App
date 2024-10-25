package com.AzureOpenAI.Hackathon.exceptions;

public class RuntimeConflictException extends RuntimeException {

    public RuntimeConflictException() {
    }

    public RuntimeConflictException(String message) {
        super(message);
    }
}
