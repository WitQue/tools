package cn.witque.tools.util;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

@Data
public class PageUtil<T> {

    private long totalCount;

    private int from;

    private int size;

    private T data;

    private int totalPage;


    public PageUtil(String nowPage, String pageSize,int totalCount){

        if(StringUtils.isEmpty(nowPage) || StringUtils.isEmpty(pageSize)){
            this.from = 0;
            this.size = 10;
        }else {
            this.size = Integer.parseInt(pageSize);
            this.totalPage = (totalCount + this.size - 1) / this.size;
            if (Integer.parseInt(nowPage) > totalPage){
             this.from = (this.totalPage - 1) * this.size;
            }else {
                this.from = (Integer.parseInt(nowPage) - 1) * this.size;
            }
            if (this.from < 0){
                this.from = 0;
            }
        }

    }

}
