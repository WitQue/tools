package cn.witque.tools.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface V2rayMapper {

    @Insert("INSERT INTO `v2ray`(`uuid`, `port`) VALUES (#{uuid}, #{port});")
    Integer addUseV2rayConfigLog(@Param("uuid") String uuid, @Param("port") String port);

    @Select("SELECT * FROM `v2ray` ORDER BY id limit #{nowPage},#{pageSize};")
    List<Map<String,Object>> getV2rayLog(@Param("nowPage") int nowPage,@Param("pageSize") int pageSize);

    @Select("SELECT  COUNT(1) FROM `v2ray`")
    Integer getV2rayLogCount();

    @Update("truncate table v2ray;")
    void init();

}
