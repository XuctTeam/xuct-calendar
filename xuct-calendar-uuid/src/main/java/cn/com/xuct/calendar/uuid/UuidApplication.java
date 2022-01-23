/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: UuidApplication
 * Author:   Administrator
 * Date:     2022/1/20 21:24
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uuid;

import cn.com.xuct.calendar.common.swagger.annotation.EnableAutoSwagger2;
import com.sankuai.inf.leaf.plugin.annotation.EnableLeafServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/20
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableLeafServer
@EnableAutoSwagger2
public class UuidApplication {

    public static void main(String[] args) {
        SpringApplication.run(UuidApplication.class, args);
    }
}