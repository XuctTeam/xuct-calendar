/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: EmailController
 * Author:   Derek Xu
 * Date:     2022/3/28 12:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.app;

import cn.com.xuct.calendar.common.core.constant.GlobalConstants;
import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.enums.IdentityTypeEnum;
import cn.com.xuct.calendar.common.module.feign.req.EmailFeignInfo;
import cn.com.xuct.calendar.common.module.params.EmailCodeParam;
import cn.com.xuct.calendar.common.security.utils.SecurityUtils;
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

import java.util.HashMap;
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
@Tag(name = "【移动端】邮件接口")
@RestController
@RequestMapping("/api/app/v1/email")
@RequiredArgsConstructor
public class EmailAppController {

    private final BasicServicesFeignClient basicServicesFeignClient;

    private final RedisTemplate<String, Object> redisTemplate;

    private final IMemberAuthService memberAuthService;

    @Operation(summary = "【非登录】密码找回")
    @PostMapping("/anno/forget")
    public R<String> forgetPasswordCode(@Validated @RequestBody EmailCodeParam param) {
        String publicKey = this.validatePublicKey(param.getRandomStr() , param.getKey());
        if(publicKey == null){
            return R.fail("验证码错误");
        }
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("identity_type", IdentityTypeEnum.email), Column.of("user_name", param.getEmail())));
        if (memberAuth == null) {
            return R.fail("用户未注册");
        }
        String code = this.bindEmail(param.getEmail(), param.getType());
        return basicServicesFeignClient.sendEmail(EmailFeignInfo.builder().subject("【楚日历】找回密码邮件")
                .template("code")
                .tos(Lists.newArrayList(param.getEmail()))
                .params(new HashMap<>() {{
                    put("title", "找回密码");
                    put("code", code);
                    put("userName", memberAuth.getUsername());
                    put("duration", "10");
                }})
                .build());
    }

    @Operation(summary = "【非登录】注册邮件")
    @PostMapping("/anno/register")
    public R<String> register(@Validated @RequestBody EmailCodeParam param) {
        String code = this.bindEmail(param.getEmail(), param.getType());
        return basicServicesFeignClient.sendEmail(EmailFeignInfo.builder().subject("【楚日历】注册邮件")
                .template("code")
                .tos(Lists.newArrayList(param.getEmail()))
                .params(new HashMap<>() {{
                    put("title", "邮件注册");
                    put("code", code);
                    put("userName", param.getEmail());
                    put("duration", "10");
                }})
                .build());
    }

    @Operation(summary = "【登录】发送邮件")
    @PostMapping("")
    public R<String> sendEmail(@Validated @RequestBody EmailCodeParam param) {
        if (param.getType() == 1 || param.getType() == 2) {
            String code = this.bindEmail(param.getEmail(), param.getType());
            return basicServicesFeignClient.sendEmail(EmailFeignInfo.builder().subject("【楚日历】绑定认证邮件")
                    .template("code")
                    .tos(Lists.newArrayList(param.getEmail()))
                    .params(new HashMap<>() {{
                        put("title", param.getType() == 1 ? "邮箱绑定" : "邮箱解绑");
                        put("code", code);
                        put("userName", SecurityUtils.getUserName());
                        put("duration", "10");
                    }})
                    .build());
        }
        return R.fail("发送错误");
    }

    private String validatePublicKey(String randomStr, String key){
        String publicRedisKey = RedisConstants.DEFAULT_PUBLIC_CODE_KEY.concat(GlobalConstants.COLON).concat(randomStr);
        Object publicKey = redisTemplate.opsForValue().get(publicRedisKey) ;
        if(publicKey == null || !String.valueOf(publicKey).equals(key)){
            return null;
        }
        return publicRedisKey;
    }

    private String bindEmail(final String email, final Integer type) {
        String userId = "";
        if (type == 3 || type == 4) {
            userId = String.valueOf(SecurityUtils.getUserId());
        }
        String key = "";
        switch (type) {
            case 1:
                key = RedisConstants.MEMBER_EMAIL_REGISTER_CODE_KEY;
                break;
            case 2:
                key = RedisConstants.MEMBER_FORGET_PASSWORD_EMAIL_CODE_KEY;
                break;
            case 3:
                key = RedisConstants.MEMBER_BIND_EMAIL_CODE_KEY;
                break;
            case 4:
                key = RedisConstants.MEMBER_UNBIND_EMAIL_CODE_KEY;
                break;
        }
        String code = RandomUtil.randomNumbers(6);
        redisTemplate.opsForValue().set(key.concat(userId).concat(":").concat(email), code, 60 * 10, TimeUnit.SECONDS);
        return code;
    }
}