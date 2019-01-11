package com.cpp.common.connection;

import com.cpp.common.connection.model.HttpRequestMessage;
import com.cpp.common.connection.model.HttpResponseResult;
import com.cpp.common.util.PreconditionUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * http client abstract class
 *
 * @author chenjian
 * @date 2019-01-11 14:24
 */
@Slf4j
public abstract class ServerHttpClient {

    /**
     * client configuration
     */
    protected ClientConfiguration config;

    public HttpResponseResult sendRequest(HttpRequestMessage httpRequest) throws IOException {
        PreconditionUtils.checkArgument(null != httpRequest, "httpRequest should not be null");

        long startTime = System.currentTimeMillis();
        HttpResponseResult httpResponse = this.sendRequestCore(httpRequest);
        long duration = System.currentTimeMillis() - startTime;
        String requestInfo = String.format("Request cost %d ms, url [%s]%s, statusCode %d.",
                duration, httpRequest.getHttpMethod().name(), httpRequest.getUrl(), httpResponse.getStatusCode());
        // 记录请求时长
        if (duration > this.config.getSlowRequestsThreshold()) {
            log.warn(requestInfo);
        } else {
            log.debug(requestInfo);
        }
        return httpResponse;
    }

    /**
     * 具体执行请求方法，由子类实现
     *
     * @param httpRequest
     * @return
     */
    protected abstract HttpResponseResult sendRequestCore(HttpRequestMessage httpRequest) throws IOException;

    public abstract void close();
}
