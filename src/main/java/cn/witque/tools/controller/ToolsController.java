package cn.witque.tools.controller;

import cn.witque.tools.service.V2rayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


/**
 * @author QUE
 */

@RestController
@RefreshScope
public class ToolsController {

    @Autowired
    private V2rayService v2rayService;


    /**
     * 下载获取V2ray配置文件
     * @param response
     * @param port
     */
    @CrossOrigin
    @GetMapping("/getV2rayConf")
    public void export(HttpServletResponse response, String port){
        v2rayService.getV2rayConfig(response,port);
    }




}
