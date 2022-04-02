/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: WxUserFeignClient
 * Author:   Derek Xu
 * Date:     2021/11/15 11:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.api.feign;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.MemberFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.ComponentNotifyFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.MemberMessageFeignInfo;
import cn.com.xuct.calendar.common.web.web.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/15
 * @since 1.0.0
 */

@FeignClient(name = "dav-ums", contextId = "component", configuration = FeignConfiguration.class)
public interface UmsComponentFeignClient {


    /**
     * 发送提醒消息
     *
     * @param componentNotifyFeignInfo
     * @return
     */
    @PostMapping("/api/feign/v1/component/alarm")
    R<String> notifyAlarm(@RequestBody ComponentNotifyFeignInfo componentNotifyFeignInfo);


    /**
     * 发送删除消息
     *
     * @param componentNotifyFeignInfo
     * @return
     */
    @PostMapping("/api/feign/v1/component/delete")
    R<String> deleteComponent(@RequestBody ComponentNotifyFeignInfo componentNotifyFeignInfo);


}