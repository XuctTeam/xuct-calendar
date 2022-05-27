/**
 * Copyright (C), 2021-2022, 楚恬商行
 * FileName: FdfsClient
 * Author:   Derek Xu
 * Date:     2022/5/27 16:16
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.fdfs.client;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.csource.fastdfs.*;

import java.io.IOException;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Administrator
 * @create 2022/5/27
 * @since 1.0.0
 */
@Slf4j
public class FdfsClient {

    @Getter
    private StorageClient storageClient;

    private TrackerServer trackerServer;


     public FdfsClient() throws IOException {
         log.info("network_timeout=" + ClientGlobal.g_network_timeout + "ms");
         log.info("charset=" + ClientGlobal.g_charset);
         TrackerClient tracker = new TrackerClient();
         trackerServer = tracker.getTrackerServer();
         StorageServer storageServer = null;
         storageClient = new StorageClient(trackerServer, storageServer);
     }
}