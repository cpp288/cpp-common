package com.cpp.common.connection.model;

import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;

import java.util.Map;

/**
 * apache http-client http request
 *
 * @author chenjian
 * @date 2019-01-11 15:18
 */
@Getter
@ToString
public class HttpRequestMessage {

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
     * apache request http content
     */
    private HttpEntity httpEntity;

    public HttpRequestMessage(String url, HttpMethod httpMethod) {
        Validate.isTrue(StringUtils.isNotEmpty(url), "url should not be empty.");
        Validate.isTrue(null != httpMethod, "httpMethod should not be null.");

        this.url = url;
        this.httpMethod = httpMethod;
    }

    public HttpRequestMessage(String url, HttpMethod httpMethod,
                              Map<String, String> headers, HttpEntity httpEntity) {
        this(url, httpMethod);
        this.headers = headers;
        this.httpEntity = httpEntity;
    }

}
