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
package cn.com.xuct.calendar.auth.api.client;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.dto.MemberInfoDto;
import cn.com.xuct.calendar.common.module.dto.MemberRegisterDto;
import cn.com.xuct.calendar.common.module.dto.WechatCodeDto;
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

@FeignClient(name = "dav-ums", contextId = "members", configuration = FeignConfiguration.class)
public interface MemberFeignClient {

    @GetMapping("/api/feign/v1/member/get/phone")
    R<MemberInfoDto> loadMemberByMobile(@RequestParam("phone") String phone);

    @Headers({"Content-Type: application/json"})
    @PostMapping("/api/feign/v1/member/get/code")
    R<MemberInfoDto> loadMemberByWechatCode(WechatCodeDto wechatCodeDto);

    @GetMapping("/api/feign/v1/member/get/openId")
    R<MemberInfoDto> loadMemberByOpenId(@RequestParam("openId") String openId);

    @GetMapping("/api/feign/v1/member/get/username")
    R<MemberInfoDto> loadMemberByUserName(@RequestParam("username") String username);

    @PostMapping("/api/feign/v1/member/register")
    @Headers("Content-Type: application/json")
    R<String> registerMember(MemberRegisterDto registerDto);

}