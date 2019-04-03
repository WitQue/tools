package cn.witque.tools.controller;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
@RefreshScope
public class ToolsController {

    @Value("${v2ray-demo-url} ")
    private String url;

    @GetMapping("/getV2rayConf")
    public void export(HttpServletResponse response, String port) throws IOException {
        String id = UUID.randomUUID().toString();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse resp = client.execute(request);
        String demo = "";
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            demo = EntityUtils.toString(resp.getEntity());
            demo = demo.replace("demoPort",""+port).replace("demoUuid", id);
            byte[] bytes = demo.getBytes();
            response.addHeader("Content-Type", "application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=config.json");
            response.getOutputStream().write(bytes);
        }
    }
}
