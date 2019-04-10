package cn.witque.tools.service.impl;

import cn.witque.tools.service.V2rayService;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Service
public class V2rayServiceImpl implements V2rayService {

    @Value("${v2ray-demo-url} ")
    private String url;


    /**
     * 下载获取V2ray配置文件
     * @param response
     * @param port
     */
    public void getV2rayConfig(HttpServletResponse response,String port){
        String id = UUID.randomUUID().toString();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse resp = null;
        try {
        resp = client.execute(request);
        String demo = "";
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            demo = EntityUtils.toString(resp.getEntity());
            demo = demo.replace("demoPort",""+port).replace("demoUuid", id);
            byte[] bytes = demo.getBytes();
            response.addHeader("Content-Type", "application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=config.json");
            response.getOutputStream().write(bytes);
        }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
}

