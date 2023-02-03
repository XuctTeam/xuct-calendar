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
package cn.com.xuct.calendar.cms.api.feign;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.req.EmailFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.ShortChainFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.SmsCodeFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxQrCodeInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/28
 * @since 1.0.0
 */
@FeignClient(name = "dav-basic-services", contextId = "basic-services")
public interface BasicServicesFeignClient {

    /**
     * 发送短信
     * @param smsCodeFeignInfo
     * @return
     */
    @PostMapping("/api/basic/v1/sms")
    R<String> smsCode(@RequestBody SmsCodeFeignInfo smsCodeFeignInfo);

    /**
     * 发送邮件
     * @param emailFeignInfo
     * @return
     */
    @PostMapping("/api/basic/v1/email")
    R<String> emailCode(@RequestBody EmailFeignInfo emailFeignInfo);

    /**
     * 通过长链接获取对应的短链接
     * @param shortChainFeignInfo
     * @return
     */
    @PostMapping("/api/basic/v1/short/chain")
    R<String> shortChain(@RequestBody ShortChainFeignInfo shortChainFeignInfo);

    /**
     * 获取微信分享二维码
     * @param wxQrCodeInfo
     * @return
     */
    @PostMapping("/api/basic/v1/wx/ma/qrcode")
    R<String> getMaQrCode(@RequestBody WxQrCodeInfo wxQrCodeInfo);
}