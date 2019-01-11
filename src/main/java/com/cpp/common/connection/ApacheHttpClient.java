package com.cpp.common.connection;

import com.cpp.common.connection.model.HttpMethod;
import com.cpp.common.connection.model.HttpProxy;
import com.cpp.common.connection.model.HttpRequestMessage;
import com.cpp.common.connection.model.HttpResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * apache http client
 *
 * @author chenjian
 * @date 2019-01-11 15:29
 */
@Slf4j
public class ApacheHttpClient extends ServerHttpClient<HttpEntity, HttpEntity> {

    /**
     * 连接池管理类
     */
    private PoolingHttpClientConnectionManager cm;
    /**
     * http client
     */
    private CloseableHttpClient httpClient;
    /**
     * 请求设置
     */
    private RequestConfig requestConfig;

    public ApacheHttpClient(ClientConfiguration config) {
        this.config = config;
        initHttpClient();
        initRequestConfig();
    }

    private void initRequestConfig() {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectionRequestTimeout(this.config.getConnectionRequestTimeout())
                .setConnectTimeout(this.config.getConnectionTimeout())
                .setSocketTimeout(this.config.getSocketTimeout());
        if (null != this.config.getHttpProxy()) {
            HttpProxy httpProxy = this.config.getHttpProxy();
            requestConfigBuilder.setProxy(httpProxy.toHttpHost());
        }
        this.requestConfig = requestConfigBuilder.build();
    }

    private void initHttpClient() {
        ConnectionSocketFactory plainSf = PlainConnectionSocketFactory
                .getSocketFactory();
        LayeredConnectionSocketFactory sslSf = SSLConnectionSocketFactory
                .getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", plainSf)
                .register("https", sslSf).build();
        this.cm = new PoolingHttpClientConnectionManager(registry);
        this.cm.setMaxTotal(this.config.getMaxConnections());
        this.cm.setDefaultMaxPerRoute(this.config.getMaxConnections());
        this.cm.setDefaultSocketConfig(SocketConfig.custom()
                .setSoTimeout(this.config.getSocketTimeout())
                .setTcpNoDelay(true)
                .build());

        // 请求失败时,进行请求重试
        HttpRequestRetryHandler retryHandler = (e, i, httpContext) -> {
            if (i > this.config.getMaxRetry()) {
                // 重试超过次数,放弃请求
                log.error(String.format("retry has more than %d time, give up request", this.config.getMaxRetry()));
                return false;
            }
            if (e instanceof NoHttpResponseException) {
                // 服务器没有响应,可能是服务器断开了连接,应该重试
                log.error("receive no response from server, retry");
                return true;
            }
            if (e instanceof SSLHandshakeException) {
                // SSL握手异常
                log.error("SSL hand shake exception");
                return false;
            }
            if (e instanceof InterruptedIOException) {
                // 超时
                log.error("InterruptedIOException");
                return false;
            }
            if (e instanceof UnknownHostException) {
                // 服务器不可达
                log.error("server host unknown");
                return false;
            }
            if (e instanceof SSLException) {
                log.error("SSLException");
                return false;
            }

            HttpClientContext context = HttpClientContext.adapt(httpContext);
            org.apache.http.HttpRequest request = context.getRequest();
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                //如果请求不是关闭连接的请求
                return true;
            }
            return false;
        };

        this.httpClient = HttpClients.custom()
                .setConnectionManager(this.cm)
                .setRetryHandler(retryHandler)
                .build();
    }

    @Override
    protected HttpResponseResult<HttpEntity> sendRequestCore(HttpRequestMessage<HttpEntity> httpRequestMessage) throws IOException {
        HttpRequestBase httpRequest = this.createHttpRequest(httpRequestMessage);

        HttpClientContext httpContext = HttpClientContext.create();
        httpContext.setRequestConfig(this.requestConfig);

        CloseableHttpResponse httpResponse = httpClient.execute(httpRequest, httpContext);

        return processResponse(httpResponse);
    }

    private HttpResponseResult<HttpEntity> processResponse(CloseableHttpResponse httpResponse) {
        HttpResponseResult<HttpEntity> httpResponseResult;
        // 获取返回结果
        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            httpResponseResult = HttpResponseResult.<HttpEntity>builder()
                    .statusCode(httpResponse.getStatusLine().getStatusCode())
                    .content(httpResponse.getEntity())
                    .build();
        } else {
            httpResponseResult = HttpResponseResult.<HttpEntity>builder()
                    .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .build();
        }
        return httpResponseResult;
    }

    private HttpRequestBase createHttpRequest(HttpRequestMessage<HttpEntity> request) {
        String url = request.getUrl();

        HttpRequestBase httpRequest;
        HttpMethod method = request.getHttpMethod();
        if (method == HttpMethod.POST) {
            HttpPost postMethod = new HttpPost(url);

            if (request.getContent() != null) {
                postMethod.setEntity(request.getContent());
            }

            httpRequest = postMethod;
        } else if (method == HttpMethod.PUT) {
            HttpPut putMethod = new HttpPut(url);

            if (request.getContent() != null) {
                putMethod.setEntity(request.getContent());
            }

            httpRequest = putMethod;
        } else if (method == HttpMethod.GET) {
            httpRequest = new HttpGet(url);
        } else if (method == HttpMethod.DELETE) {
            httpRequest = new HttpDelete(url);
        } else if (method == HttpMethod.HEAD) {
            httpRequest = new HttpHead(url);
        } else if (method == HttpMethod.OPTIONS) {
            httpRequest = new HttpOptions(url);
        } else {
            throw new IllegalArgumentException(String.format("Unknown HTTP method name: %s", method.toString()));
        }

        Map<String, String> headers = request.getHeaders();
        if (headers != null && headers.size() > 0) {
            headers.forEach(httpRequest::addHeader);
        }

        return httpRequest;
    }

    @Override
    public void close() {
        try {
            if (null != httpClient) {
                httpClient.close();
            }
            if (null != cm) {
                cm.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
