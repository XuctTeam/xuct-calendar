/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: UuidFeignClient
 * Author:   Administrator
 * Date:     2022/1/21 9:31
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.api.feign;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.web.web.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/21
 * @since 1.0.0
 */
@FeignClient(name = "dav-uuid", contextId = "uuid", configuration = FeignConfiguration.class)
public interface UuidFeignClient {

    @GetMapping("/api/feign/uuid")
    R<Long> uuid(@RequestParam("key") String key);
}