package cn.witque.tools.controller;

import cn.witque.tools.service.V2rayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RefreshScope
public class PageController {

    @Autowired
    private V2rayService v2rayService;

    @CrossOrigin
    @PostMapping("/showV2rayLog")
    public Object getV2rayConfig(@RequestBody Map<String,String> map){
        return v2rayService.getV2rayLog(map.get("nowPage"),map.get("pageSize"));
    }


    @CrossOrigin
    @PostMapping("/delV2rayLog")
    public Object delV2rayLog(@RequestBody Map<String,String> map){
        return v2rayService.delV2rayLog(map.get("id"));
    }



}
