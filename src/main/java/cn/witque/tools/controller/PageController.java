package cn.witque.tools.controller;

import cn.witque.tools.service.V2rayService;
import cn.witque.tools.util.PageUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

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
        PageUtil pageUtil = new PageUtil(map.get("nowPage"),map.get("pageSize"),v2rayService.getV2rayLogCount());
        pageUtil.setTotalCount(v2rayService.getV2rayLogCount());
        pageUtil.setData(v2rayService.getV2rayLog(pageUtil.getFrom(),pageUtil.getSize()));
        if (pageUtil.getFrom() == 0){
            pageUtil.setFrom(1);
        }else {
            pageUtil.setFrom(pageUtil.getFrom() / pageUtil.getSize());
        }
        return pageUtil;
    }


    @CrossOrigin
    @PostMapping("/delV2rayLog")
    public Object delV2rayLog(@RequestBody Map<String,String> map){
        return v2rayService.delV2rayLog(map.get("id"));
    }



}
