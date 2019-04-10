package cn.witque.tools.util;

import com.google.common.base.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static final String DEFAULT_ENCODING = "UTF-8";

    private static HttpClientBuilder httpClientBuilder = null;

    private static RequestConfig requestConfig = null;

    private static final Header DEFAULT_HTTP_HEADER = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");

    /**
     * 加入
     */
    private static  final Header USER_AGENT=new BasicHeader(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");

    // 设置连接超时时间
    private static Integer CONNECTION_TIMEOUT = 120 * 1000; // 设置请求超时
    private static Integer SOCKET_TIMEOUT = 120 * 1000; // 设置等待数据超时时间

    static {
        requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpClientBuilder = HttpClients.custom();
    }

    /**
     * get方法
     *
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, Object> params) {
        return execute(params, url, "get");
    }

    public static String doPost(String url, Object params) {
        return execute(params, url, "post");
    }

    /**
     * 执行
     *
     * @param params
     * @param url
     * @param methodType
     * @return
     */
    private static String execute(Object params, String url, String methodType) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        HttpUriRequest request = null;
        String message = null;
        try {
            httpClient = createHttpClient();
            request = createRequestMethod(params, url, methodType);
            response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                entity = response.getEntity();
                message = EntityUtils.toString(entity, DEFAULT_ENCODING);
            } else {
                logger.error("=========调用失败=========" + response);
            }
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                EntityUtils.consume(response.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
            close(httpClient, response, entity);

        }
        return message;
    }

    /**
     * 组装request请求
     *
     * @param params
     * @param url
     * @param method
     * @return
     */
    private static HttpUriRequest createRequestMethod(Object params, String url, String method) {
        HttpUriRequest reqMethod = null;
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        if (params instanceof Map) {
            ((Map<String, String>) params).forEach((key, value) -> {
                NameValuePair pair = new BasicNameValuePair(key, value);
                paramList.add(pair);
            });
        }

        if ("post".equals(method)) {
            if (params instanceof String) {
                StringEntity entity = new StringEntity(params.toString(), Charsets.UTF_8);
                reqMethod = RequestBuilder.post().setUri(url).setEntity(entity).addHeader(DEFAULT_HTTP_HEADER).addHeader(USER_AGENT)
                        .addParameters(paramList.toArray(new BasicNameValuePair[paramList.size()])).setConfig
                                (requestConfig).build();
            } else {
                reqMethod = RequestBuilder.post().setUri(url)
                        .addParameters(paramList.toArray(new BasicNameValuePair[paramList.size()])).setConfig
                                (requestConfig)
                        .build();
            }
        } else if ("get".equals(method)) {
            reqMethod = RequestBuilder.get().setUri(url).addParameters(paramList.toArray(new
                    BasicNameValuePair[paramList.size()])).setConfig(requestConfig).build();
        }

        return reqMethod;
    }

    /**
     * 生成client
     *
     * @return
     */
    public static CloseableHttpClient createHttpClient() {
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
        return httpClientBuilder.build();
    }

    /**
     * 释放资源
     *
     * @param client
     * @param response
     * @param entity
     */
    public static void close(CloseableHttpClient client, CloseableHttpResponse response, HttpEntity entity) {
        if (null != entity) {
            try {
                EntityUtils.consume(entity);
            } catch (IOException e) {
                logger.error(e+"");
            }
        }
        if (null != response) {
            try {
                response.close();
            } catch (Exception e) {
                logger.error(e+"");
            }
        }
        if (null != client) {
            try {
                client.close();
            } catch (Exception e) {
                logger.error(e+"");
            }
        }
    }

}
