package cn.witque.tools.service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface V2rayService {

    String getV2rayConfig(HttpServletResponse response, String port);

    List<Map<String,Object>> getV2rayLog(int nowPage,int pageSize);

    void truncateV2rayLog();

    int getV2rayLogCount();
}
