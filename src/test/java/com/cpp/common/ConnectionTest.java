package com.cpp.common;

import com.cpp.common.connection.ApacheHttpClient;
import com.cpp.common.connection.ClientConfiguration;
import com.cpp.common.connection.ServerHttpClient;
import com.cpp.common.connection.model.HttpMethod;
import com.cpp.common.connection.model.HttpRequestMessage;
import com.cpp.common.connection.model.HttpResponseResult;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * apache client test
 *
 * @author chenjian
 * @date 2019-01-11 19:21
 */
public class ConnectionTest {

    @Test
    public void clientTest() {
        ServerHttpClient httpClient = new ApacheHttpClient(new ClientConfiguration());
        try {
            String url = "http://116.62.244.37/yqx/v1/sms/single_send";
            HttpEntity httpEntity = new StringEntity("{\"account\":\"\",\"sign\":\"\"}", ContentType.APPLICATION_JSON);
            HttpRequestMessage httpRequest =
                    new HttpRequestMessage(url, HttpMethod.POST, null, httpEntity);
            HttpResponseResult httpResponse = httpClient.sendRequest(httpRequest);
            System.out.println(EntityUtils.toString(httpResponse.getHttpEntity(), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }
    }
}
