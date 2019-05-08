package cn.witque.tools.service;

import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpServletResponse;

public interface V2rayService {

    String getV2rayConfig(HttpServletResponse response, String port);

    PageInfo getV2rayLog(String nowPage, String pageSize);

    void truncateV2rayLog();

    int getV2rayLogCount();

    int delV2rayLog(String id);
}
