/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: InnnerServicesFeignClient
 * Author:   Derek Xu
 * Date:     2022/3/28 10:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.auth.api.client;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.EmailFeignInfoReq;
import cn.com.xuct.calendar.common.module.feign.SmsCodeFeignInfoReq;
import cn.com.xuct.calendar.common.web.web.FeignConfiguration;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/28
 * @since 1.0.0
 */
@FeignClient(name = "dav-basic-services", contextId = "basic-services", configuration = FeignConfiguration.class)
public interface BasicServicesFeignClient {

    @PostMapping("/api/basic/v1/sms")
    @Headers("Content-Type: application/json")
    R<String> smsCode(SmsCodeFeignInfoReq smsCodeFeignInfoReq);

    @PostMapping("/api/basic/v1/email")
    R<String> emailCode(EmailFeignInfoReq emailFeignInfoReq);
}