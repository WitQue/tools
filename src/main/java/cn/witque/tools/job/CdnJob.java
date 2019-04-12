package cn.witque.tools.job;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
public class CdnJob {

    @Value("${local-path}")
    private String LOCAL_PATH;
    @Value("${bucket-name}")
    private String BUCKET_NAME;
    @Value("${access-key}")
    private String accessKey ;
    @Value("${secret-key}")
    private String secretKey;

    private final String cron = "0/10 * * * * ?";

    private static COSClient COS_CLIENT = null;


    /**
     * 初始化客户端
     */
    public @PostConstruct void init() {
        COSCredentials cred = new BasicCOSCredentials(accessKey,secretKey);
        ClientConfig clientConfig = new ClientConfig(new Region("ap-shanghai"));
        COS_CLIENT = new COSClient(cred, clientConfig);
        log.info("初始化客户端");
    }

    /**
     * CND任务
     */
    @Scheduled(cron = cron)
    public void cndJob(){
        List<String> file = getNotInCloudFileNameList();
        if (file != null) {
            upload(file);
        }
    }


    /**
     * 上传不存在的文件
     * @param list
     */
    public void upload(List<String> list) {
        if (list.size() > 0) {
            for (String upName : list) {
                File localFile = new File(LOCAL_PATH + getPrefix() + upName);

                String key = getPrefix() + upName;
                PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, key, localFile);
                PutObjectResult putObjectResult = COS_CLIENT.putObject(putObjectRequest);
                log.info(key + "上传成功");
            }
        } else {
            log.info("检测结果：未检测到服务器不存在的文件!");
        }
    }


    /**
     * 获取前缀 年-月-日 文件夹方式
     * @return
     */
    private String getPrefix() {
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(1));
        String month = String.valueOf(calendar.get(2) + 1);
        if (month.length() < 2) {
            month = "0" + month;
        }
        String prefix = year + "/" + month + "/";
        return prefix;
    }


    /**
     * 取得对象云存储里的文件
     * @return
     */
    public List<String> getCloudFileNameList() {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        listObjectsRequest.setBucketName(BUCKET_NAME);
        String prefix = getPrefix();
        listObjectsRequest.setPrefix(prefix);
        listObjectsRequest.setMaxKeys(Integer.valueOf(999));
        ObjectListing objectListing = COS_CLIENT.listObjects(listObjectsRequest);
        List<COSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        List<String> bucketList = new ArrayList();
        for (COSObjectSummary cosObjectSummary : objectSummaries) {
            String key = cosObjectSummary.getKey().replace(prefix, "");
            if (!key.equals("")) {
                bucketList.add(key);
            }
        }
        return bucketList;
    }

    /**
     * 检测不存在的文件并删除本地文件
     * @return
     */
    public List<String> getNotInCloudFileNameList() {
        File file = new File(LOCAL_PATH + getPrefix());

        String[] files = file.list();
        if ((file == null) || (files == null)) {
            log.info("未检测到服务器不存在的文件(跳出)");
            return null;
        }
        List<String> bucketList = getCloudFileNameList();
        List<String> localList = Arrays.asList(files);
        List<String> notHaveList = new ArrayList();
        for (String local : localList) {
            boolean flag = bucketList.contains(local);
            if (!flag) {
                notHaveList.add(local);
            } else {
                File delFile = new File(LOCAL_PATH + getPrefix() + local);
                delFile.delete();
                log.info("已删除：" + LOCAL_PATH + getPrefix() + local);
            }
        }
        return notHaveList;
    }


}