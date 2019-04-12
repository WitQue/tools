package cn.witque.tools.service.impl;

import cn.witque.tools.mapper.V2rayMapper;
import cn.witque.tools.service.V2rayService;
import cn.witque.tools.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class V2rayServiceImpl implements V2rayService {

    @Value("${v2ray-demo-url} ")
    private String url;

    @Autowired
    private V2rayMapper v2rayMapper;

    /**
     * 下载获取V2ray配置文件
     * @param response
     * @param port
     */
    public String getV2rayConfig(HttpServletResponse response, String port) {
        String id = UUID.randomUUID().toString();
        try {
            String demo = HttpClientUtil.doGet(url.replace(" ",""),null);
            demo = demo.replace("65000", "" + port).replace("demoUuid", id);
            byte[] bytes = demo.getBytes();
            response.addHeader("Content-Type", "application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=config.json");
            response.getOutputStream().write(bytes);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        v2rayMapper.addUseV2rayConfigLog(id,port);
        return id;
    }


   public List<Map<String,Object>> getV2rayLog(int nowPage,int pageSize){
       List<Map<String, Object>> list = v2rayMapper.getV2rayLog(nowPage,pageSize);
       return list;
    }

    @Override
    public void truncateV2rayLog() {
        v2rayMapper.init();
        log.info("初始化v2ray表");
    }


    public int getV2rayLogCount(){
        return v2rayMapper.getV2rayLogCount();
    }

}

