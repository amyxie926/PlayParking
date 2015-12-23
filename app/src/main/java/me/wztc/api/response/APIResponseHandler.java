package me.wztc.api.response;

public interface APIResponseHandler<RESPONSE extends APIResponse> {

    void handleError(String errorCode, String errorMessage);

    void handleResponse(RESPONSE response);
}
