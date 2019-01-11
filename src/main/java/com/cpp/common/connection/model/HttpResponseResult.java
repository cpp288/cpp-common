package com.cpp.common.connection.model;

import lombok.Builder;
import lombok.Data;
import org.apache.http.HttpEntity;

import java.util.Map;

/**
 * http response
 *
 * @author chenjian
 * @date 2019-01-11 16:06
 */
@Data
@Builder
public class HttpResponseResult {

    /**
     * status code
     */
    private int statusCode;
    /**
     * request headers
     *
     */
    private Map<String, String> headers;
    /**
     * apache response content
     */
    private HttpEntity httpEntity;
}
