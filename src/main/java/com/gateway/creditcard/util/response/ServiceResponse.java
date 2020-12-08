package com.gateway.creditcard.util.response;

public class ServiceResponse<T> {

    private T response;
    private ServiceError serviceError;

    public ServiceResponse() {
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public ServiceError getServiceError() {
        return serviceError;
    }

    public void setServiceError(ServiceError serviceError) {
        this.serviceError = serviceError;
    }

    public enum ServiceError {
        ALREADY_EXISTS("entity_already_exists"),
        VALIDATION_ERROR("validation_error"),
        DATABASE_ERROR("generic_database_error");

        private String messageKeyValue;

        ServiceError(String messageKeyValue) {
            this.messageKeyValue = messageKeyValue;
        }
    }
}

