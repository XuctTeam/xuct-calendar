/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ShortChainDomainConfiguration
 * Author:   Derek Xu
 * Date:     2022/5/5 17:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.basic.services.config;

import cn.com.xuct.calendar.basic.services.config.data.ShortChainDomainData;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/5/5
 * @since 1.0.0
 */
@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "domain")
public class ShortChainDomainConfiguration {

    private List<ShortChainDomainData> list = Lists.newArrayList();

    public String getNotFound(String domain) {
        return list.stream().filter(item ->
                item.getDomain().replace("http://", "").replace("https://", "")
                        .equals(domain)).findFirst().orElse(new ShortChainDomainData()).getNotFound();
    }

    public String getDomain(String type) {
        return list.stream().filter(item -> item.getType().equals(type)).findFirst().orElse(new ShortChainDomainData()).getDomain();
    }

}