/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: SmsCodeController
 * Author:   Derek Xu
 * Date:     2022/3/28 11:46
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.app;

import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.enums.IdentityTypeEnum;
import cn.com.xuct.calendar.common.module.params.SmsSendParam;
import cn.com.xuct.calendar.common.security.annotation.Inner;
import cn.com.xuct.calendar.common.security.utils.SecurityUtils;
import cn.com.xuct.calendar.ums.api.entity.Member;
import cn.com.xuct.calendar.ums.api.entity.MemberAuth;
import cn.com.xuct.calendar.ums.api.feign.BasicServicesFeignClient;
import cn.com.xuct.calendar.ums.boot.service.IMemberAuthService;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/28
 * @since 1.0.0
 */
@Slf4j
@Tag(name = "【移动端】短信接口")
@RestController
@RequestMapping("/api/app/v1/sms")
@RequiredArgsConstructor
public class SmsAppController {

    private final RedisTemplate<String, Object> redisTemplate;

    private final BasicServicesFeignClient basicServicesFeignClient;

    private final IMemberAuthService memberAuthService;

    @Operation(summary = "【非登录】登录短信")
    @PostMapping("/anno/login")
    public R<String> loginSmsCode(@Validated @RequestBody SmsSendParam param) {
        String code = this.sendBindCode(param.getPhone(), param.getType());
        //basicServicesFeignClient.smsCode(SmsCodeFeignInfo.builder().phones(Lists.newArrayList(param.getPhone())).code(code).template("login").build());
        return R.status(true);
    }
    @Operation(summary = "【非登录】注册短信")
    @PostMapping("/anno/register")
    public R<String> registerSmsCode(@Validated @RequestBody SmsSendParam param) {
        String code = this.sendBindCode(param.getPhone(), param.getType());
        //basicServicesFeignClient.smsCode(SmsCodeFeignInfo.builder().phones(Lists.newArrayList(param.getPhone())).code(code).template("login").build());
        return R.status(true);
    }
    @Operation(summary = "【非登录】密码找回")
    @PostMapping("/anno/forget")
    public R<String> forgetPasswordCode(@Validated @RequestBody SmsSendParam param) {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("identity_type", IdentityTypeEnum.phone), Column.of("user_name", param.getPhone())));
        if (memberAuth == null) return R.fail("用户未注册");
        String code = this.sendBindCode(param.getPhone(), param.getType());
        return R.status(true);
    }

    @Operation(summary = "【登录】发送短信")
    @PostMapping("")
    public R<String> sendSmsCode(@Validated @RequestBody SmsSendParam param) {
        if (!(param.getType() == 3 || param.getType() == 4)) return R.fail("类型错误");
        String code = this.sendBindCode(param.getPhone(), param.getType());
        //basicServicesFeignClient.smsCode(SmsCodeFeignInfo.builder().phones(Lists.newArrayList(param.getPhone())).code(code).template("bind").build());
        return R.status(true);
    }

    private String sendBindCode(String phone, Integer type) {
        String userId = "";
        if (type == 3 || type == 4) {
            userId = String.valueOf(SecurityUtils.getUserId());
        }
        String key = null;
        switch (type) {
            case 0:
                key = RedisConstants.MEMBER_PHONE_LOGIN_CODE_KEY;
                break;
            case 1:
                key = RedisConstants.MEMBER_PHONE_REGISTER_CODE_KEY;
                break;
            case 2:
                key = RedisConstants.MEMBER_FORGET_PASSWORD_PHONE_CODE_KEY;
                break;
            case 3:
                key = RedisConstants.MEMBER_BIND_PHONE_CODE_KEY;
                break;
            case 4:
                key = RedisConstants.MEMBER_UNBIND_PHONE_CODE_KEY;
                break;
        }
        String code = RandomUtil.randomNumbers(6);
        redisTemplate.opsForValue().set(key.concat(userId).concat(":").concat(phone), code, 60 * 10, TimeUnit.SECONDS);
        return code;
    }
}