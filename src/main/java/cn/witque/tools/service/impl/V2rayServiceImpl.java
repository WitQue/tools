package cn.witque.tools.service.impl;

import cn.witque.tools.service.V2rayService;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
    public void getV2rayConfig(HttpServletResponse response, String port) {
        String id = UUID.randomUUID().toString();
        String demo = null;
        try {
            ClassPathResource resource = new ClassPathResource("./static/demo.json");
            File file = resource.getFile();
            demo = FileUtils.readFileToString(file);
            demo = demo.replace("65000", "" + port).replace("demoUuid", id);
            byte[] bytes = demo.getBytes();
            response.addHeader("Content-Type", "application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=config.json");
            response.getOutputStream().write(bytes);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}

