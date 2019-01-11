package com.cpp.common.connection.model;

import lombok.ToString;

/**
 * http methods
 *
 * @author chenjian
 * @date 2019-01-11 14:38
 */
@ToString
public enum HttpMethod {

    /**
     * HTTP DELETE.
     */
    DELETE,

    /**
     * HTTP GET
     */
    GET,

    /**
     * HTTP HEAD
     */
    HEAD,

    /**
     * HTTP POST
     */
    POST,

    /**
     * HTTP PUT
     */
    PUT,

    /**
     * HTTP OPTION
     */
    OPTIONS,
    ;
}
