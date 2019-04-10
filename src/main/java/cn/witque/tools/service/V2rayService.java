package cn.witque.tools.service;

import javax.servlet.http.HttpServletResponse;

public interface V2rayService {

    void getV2rayConfig(HttpServletResponse response, String port);


}
