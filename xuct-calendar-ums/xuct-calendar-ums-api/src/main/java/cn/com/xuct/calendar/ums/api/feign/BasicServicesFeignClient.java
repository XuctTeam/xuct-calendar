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
package cn.com.xuct.calendar.ums.api.feign;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.req.*;
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
 * @create 2022/3/28
 * @since 1.0.0
 */
@FeignClient(name = "dav-basic-services", contextId = "basic-services")
public interface BasicServicesFeignClient {

    @PostMapping("/api/basic/v1/sms")
    R<String> smsCode(@RequestBody SmsCodeFeignInfo smsCodeFeignInfo);

    @PostMapping("/api/basic/v1/email")
    R<String> sendEmail(@RequestBody EmailFeignInfo emailFeignInfo);

    @GetMapping("/api/basic/v1/wx/miniapp/getSessionInfo")
    R<WxMaJscode2SessionResult> getSessionInfo(@RequestParam("code") String code);

    @PostMapping("/api/basic/v1/wx/miniapp/getUserInfo")
    R<WxMaUserInfo> getUserInfo(@RequestBody WxUserInfoFeignInfo wxUserInfoFeignInfo);

    @PostMapping("/api/basic/v1/wx/miniapp/getPhoneNoInfo")
    R<WxMaPhoneNumberInfo> getPhoneNoInfo(@RequestBody WxUserPhoneFeignInfo wxUserPhoneFeignInfo);

    @PostMapping("/api/basic/v1/wx/miniapp/sendSubscribeMsg")
    R<String> sendSubscribeMsg(@RequestBody List<WxSubscribeMessageFeignInfo> wxSubscribeMessageFeignInfos);
}