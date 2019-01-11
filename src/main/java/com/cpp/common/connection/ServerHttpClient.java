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
public abstract class ServerHttpClient<R, Q> {

    /**
     * client configuration
     */
    protected ClientConfiguration config;

    public HttpResponseResult<Q> sendRequest(HttpRequestMessage<R> httpRequest) throws IOException {
        PreconditionUtils.checkArgument(null != httpRequest, "httpRequest should not be null");

        long startTime = System.currentTimeMillis();
        HttpResponseResult<Q> httpResponse = this.sendRequestCore(httpRequest);
        long duration = System.currentTimeMillis() - startTime;
        // 记录请求时长
        if (duration > this.config.getSlowRequestsThreshold()) {
            log.warn(String.format("Request cost %d seconds, url %s, method %s, statusCode %d.",
                    duration / 1000, httpRequest.getUrl(), httpRequest.getHttpMethod(), httpResponse.getStatusCode()));
        }
        return httpResponse;
    }

    /**
     * 具体执行请求方法，由子类实现
     *
     * @param httpRequest
     * @return
     */
    protected abstract HttpResponseResult<Q> sendRequestCore(HttpRequestMessage<R> httpRequest) throws IOException;

    public abstract void close();
}
