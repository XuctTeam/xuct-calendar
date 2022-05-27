/**
 * Copyright (C), 2021-2022, 楚恬商行
 * FileName: FdfsConfiguration
 * Author:   Derek Xu
 * Date:     2022/5/27 15:05
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.fdfs.config;

import cn.com.xuct.calendar.common.fdfs.client.FdfsExt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.csource.fastdfs.ClientGlobal;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Administrator
 * @create 2022/5/27
 * @since 1.0.0
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(FdfsProperties.class)
public class FdfsAutoConfiguration implements InitializingBean, DisposableBean {

    private final FdfsProperties fdfsProperties;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("fdfs start init.....");
        ClientGlobal.initByProperties(fdfsProperties);
        FdfsExt.initExt();
    }

    @Override
    public void destroy() throws Exception {
        //System.out.println("<<<<<<<<<<<要销毁的事 begin>>>>>>>>>>>>>>>");
    }

}