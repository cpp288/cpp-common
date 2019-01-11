package com.cpp.common.connection.model;

import lombok.Builder;
import lombok.Data;

/**
 * http response
 *
 * @author chenjian
 * @date 2019-01-11 16:06
 */
@Data
@Builder
public class HttpResponseResult<T> {

    private int statusCode;

    private T content;
}
