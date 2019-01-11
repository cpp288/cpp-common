package com.cpp.common.connection.model;

import com.cpp.common.util.PreconditionUtils;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * apache http-client http request
 *
 * @author chenjian
 * @date 2019-01-11 15:18
 */
@Getter
@ToString
public class HttpRequestMessage<T> {

    /**
     * request url
     */
    private String url;
    /**
     * request http method
     */
    private HttpMethod httpMethod;
    /**
     * request headers
     *
     * @see HttpHeaders
     */
    private Map<String, String> headers;
    /**
     * request http content
     */
    private T content;

    public HttpRequestMessage(String url, HttpMethod httpMethod) {
        PreconditionUtils.checkArgument(StringUtils.isNotEmpty(url), "url should not be empty.");
        PreconditionUtils.checkArgument(null != httpMethod, "httpMethod should not be null.");

        this.url = url;
        this.httpMethod = httpMethod;
    }

    public HttpRequestMessage(String url, HttpMethod httpMethod,
                              Map<String, String> headers, T content) {
        this(url, httpMethod);
        this.headers = headers;
        this.content = content;
    }

}
