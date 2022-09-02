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
package cn.com.xuct.calendar.common.security.client;

import cn.com.xuct.calendar.common.core.constant.ServiceNameConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.MemberFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.MemberModifyPasswordFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.MemberRegisterFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxUserInfoFeignInfo;
import cn.com.xuct.calendar.common.web.web.FeignConfiguration;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/15
 * @since 1.0.0
 */

@FeignClient(name = ServiceNameConstants.UMS_SERVICE, contextId = "members" , configuration = FeignConfiguration.class)
public interface UmsMemberFeignClient {

    @GetMapping("/api/feign/v1/member/get/phone")
    R<MemberFeignInfo> loadMemberByMobile(@RequestParam("phone") String phone);

    @GetMapping("/api/feign/v1/member/get/openId")
    R<MemberFeignInfo> loadMemberByOpenId(@RequestParam("openId") String openId);

    @GetMapping("/api/feign/v1/member/get/username")
    R<MemberFeignInfo> loadMemberByUserName(@RequestParam("username") String username);

    @GetMapping("/api/feign/v1/member/get/email")
    R<MemberFeignInfo> loadMemberByEmail(@RequestParam("email") String email);

    @Headers({"Content-Type: application/json"})
    @PostMapping("/api/feign/v1/member/get/code")
    R<MemberFeignInfo> loadMemberByWechatCode(WxUserInfoFeignInfo wxUserInfoFeignInfo);


    @PostMapping("/api/feign/v1/member/register")
    @Headers("Content-Type: application/json")
    R<String> registerMember(MemberRegisterFeignInfo registerFeignInfo);

    @PostMapping("/api/feign/v1/member/modify/password")
    @Headers("Content-Type: application/json")
    R<String> modifyPassword(MemberModifyPasswordFeignInfo passwordDto);

}