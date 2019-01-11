package com.cpp.common.connection;

import com.cpp.common.connection.model.HttpProxy;
import lombok.Data;

/**
 * client configuration
 *
 * @author chenjian
 * @date 2019-01-11 15:30
 */
@Data
public class ClientConfiguration {

    public static final int DEFAULT_MAX_RETRIES = 3;
    public static final int DEFAULT_MAX_CONNECTIONS = 1024;

    public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = -1;
    public static final int DEFAULT_CONNECTION_TIMEOUT = 50 * 1000;
    public static final int DEFAULT_SOCKET_TIMEOUT = 50 * 1000;
    /**
     * 默认慢请求阈值
     */
    public static final long DEFAULT_SLOW_REQUESTS_THRESHOLD = 5 * 60 * 1000;

    /**
     * 参数配置
     */
    private int maxRetry = DEFAULT_MAX_RETRIES;
    private int maxConnections = DEFAULT_MAX_CONNECTIONS;
    private int connectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT;
    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    private int socketTimeout = DEFAULT_SOCKET_TIMEOUT;

    protected long slowRequestsThreshold = DEFAULT_SLOW_REQUESTS_THRESHOLD;

    /**
     * 代理配置
     */
    private HttpProxy httpProxy;
}
