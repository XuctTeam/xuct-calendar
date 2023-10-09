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

    /**
     * 发送短信验证码
     *
     * @param smsCodeFeignInfo 短信验证码信息
     * @return String
     */
    @PostMapping("/api/basic/v1/sms")
    R<String> smsCode(@RequestBody SmsCodeFeignInfo smsCodeFeignInfo);

    /**
     * 发送邮件
     *
     * @param emailFeignInfo 邮件信息
     * @return String
     */
    @PostMapping("/api/basic/v1/email")
    R<String> sendEmail(@RequestBody EmailFeignInfo emailFeignInfo);

    /**
     * 获取微信小程序的session信息
     *
     * @param code 微信小程序的code
     * @return WxMaJscode2SessionResult
     */
    @GetMapping("/api/basic/v1/wx/ma/getSessionInfo")
    R<WxMaJscode2SessionResult> getSessionInfo(@RequestParam("code") String code);

    /**
     * 获取微信小程序的用户信息
     *
     * @param wxUserInfoFeignInfo 微信用户信息
     * @return WxMaUserInfo
     */
    @PostMapping("/api/basic/v1/wx/ma/getUserInfo")
    R<WxMaUserInfo> getUserInfo(@RequestBody WxUserInfoFeignInfo wxUserInfoFeignInfo);

    /**
     * 获取微信小程序的手机号信息
     *
     * @param wxUserPhoneFeignInfo 微信用户手机号信息
     * @return WxMaPhoneNumberInfo
     */
    @PostMapping("/api/basic/v1/wx/ma/getPhoneNoInfo")
    R<WxMaPhoneNumberInfo> getPhoneNoInfo(@RequestBody WxUserPhoneFeignInfo wxUserPhoneFeignInfo);

    /**
     * 发送订阅消息
     *
     * @param wxSubscribeMessageFeignInfos 订阅消息信息
     * @return String
     */
    @PostMapping("/api/basic/v1/wx/ma/sendSubscribeMsg")
    R<String> sendSubscribeMsg(@RequestBody List<WxSubscribeMessageFeignInfo> wxSubscribeMessageFeignInfos);
}