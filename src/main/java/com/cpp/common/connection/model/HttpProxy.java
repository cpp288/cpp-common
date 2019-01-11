package com.cpp.common.connection.model;

import com.cpp.common.util.PreconditionUtils;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;

/**
 * http proxy
 *
 * @author chenjian
 * @date 2019-01-11 15:39
 */
@Slf4j
@Getter
@ToString
public class HttpProxy {

    private String host;
    private int port;
    private String username;
    private String password;

    /**
     * 是否需要身份验证
     */
    private boolean authenticationNeeded = false;

    public HttpProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public HttpProxy(String host, int port, String username, String password) {
        this(host, port);

        PreconditionUtils.checkArgument(null != this.username, "username should not be null");
        PreconditionUtils.checkArgument(null != this.password, "password should not be null");

        this.authenticationNeeded = true;
        this.username = username;
        this.password = password;

        log.info(String.format("Http Proxy - host: %s, port: %d, username: %s, password: %s",
                this.host, this.port, this.username, this.password));
    }

    public HttpHost toHttpHost() {
        return new HttpHost(this.host, this.port);
    }

}
