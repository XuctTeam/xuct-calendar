/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: RegisterEndpoint
 * Author:   Derek Xu
 * Date:     2022/3/26 19:38
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.endpoint;

import cn.com.xuct.calendar.common.core.exception.SvrException;
import cn.com.xuct.calendar.common.core.res.SvrResCode;
import cn.com.xuct.calendar.common.module.feign.req.EmailFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.SmsCodeFeignInfo;
import cn.com.xuct.calendar.common.module.params.MemberEmailParam;
import cn.com.xuct.calendar.common.module.params.MemberPhoneParam;
import cn.com.xuct.calendar.common.module.params.data.MemberEmailRegisterData;
import cn.com.xuct.calendar.common.module.params.data.MemberPhoneRegisterData;
import cn.com.xuct.calendar.common.module.params.data.MemberUserNameRegisterData;
import cn.com.xuct.calendar.uaa.api.client.BasicServicesFeignClient;
import cn.com.xuct.calendar.uaa.api.client.UmsMemberFeignClient;
import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.req.MemberRegisterFeignInfo;
import cn.com.xuct.calendar.common.module.params.MemberRegisterParam;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/26
 * @since 1.0.0
 */
@Slf4j
@Api(tags = "【通用】注册服务")
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@Controller
public class RegisterEndpoint {

    private final StringRedisTemplate stringRedisTemplate;

    private final UmsMemberFeignClient umsMemberFeignClient;

    private final BasicServicesFeignClient basicServicesFeignClient;

    @ApiOperation(value = "获取图形验证码")
    @GetMapping("/captcha")
    public R<Map<String, String>> captcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        String redisKey = this.getCaptchaKey(key);
        stringRedisTemplate.opsForValue().set(redisKey, verCode, 60 * 2, TimeUnit.SECONDS);
        return R.data(new HashMap<String, String>() {{
            put("key", key);
            put("image", specCaptcha.toBase64(""));
        }});
    }

    @ApiOperation(value = "获取短信验证码")
    @PostMapping("/sms")
    public R<String> sendSmsCode(@Validated @RequestBody MemberPhoneParam param) {
        String code = RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set(this.getSmsCodeKey(param.getPhone()), code, 60 * 2, TimeUnit.SECONDS);
        //return basicServicesFeignClient.smsCode(SmsCodeFeignInfo.builder().code(code).phones(Lists.newArrayList(param.getPhone())).template("register").build());
        return R.status(true);
    }


    @ApiOperation(value = "获取邮箱验证码")
    @PostMapping("/email/code")
    public R<String> sendEmailCode(@Validated @RequestBody MemberEmailParam param) {
        String code = RandomUtil.randomNumbers(4);
        stringRedisTemplate.opsForValue().set(this.getEmailCodeKey(param.getEmail()), code, 120, TimeUnit.SECONDS);
        return basicServicesFeignClient.emailCode(EmailFeignInfo.builder().template("register").tos(Lists.newArrayList(param.getEmail())).subject("注册验证").params(
                new HashMap<>() {{
                    put("userName", param.getEmail());
                    put("code", code);
                    put("date", DateUtil.now());
                }}).build());
    }


    @ApiOperation(value = "会员注册")
    @PostMapping("")
    public R<String> register(@Validated @RequestBody MemberRegisterParam param) {
        switch (param.getFormType()) {
            case 0:
                return this.registerByUserName(param.getUsername());
            case 1:
                return this.registerByPhone(param.getPhone());
            case 2:
                return this.registerByEmail(param.getEmail());
            default:
                throw new SvrException(SvrResCode.PARAM_ERROR);
        }
    }


    private R<String> registerByUserName(MemberUserNameRegisterData param) {
        if (!(StringUtils.hasLength(param.getKey()) && StringUtils.hasLength(param.getCaptcha())))
            return R.fail("参数错误");
        String verCode = stringRedisTemplate.opsForValue().get(this.getCaptchaKey(param.getKey()));
        if (!verCode.toLowerCase().equals(param.getCaptcha().toLowerCase())) return R.fail("验证失败");
        return umsMemberFeignClient.registerMember(MemberRegisterFeignInfo.builder().formType(0).username(param.getUsername()).password(param.getPassword()).build());
    }

    private R<String> registerByPhone(MemberPhoneRegisterData param) {
        if (!(StringUtils.hasLength(param.getPhone()) && StringUtils.hasLength(param.getSmsCode())))
            return R.fail("参数错误");
        String verCode = stringRedisTemplate.opsForValue().get(this.getSmsCodeKey(param.getPhone()));
        if (!StringUtils.hasLength(verCode) || !verCode.equals(param.getSmsCode())) return R.fail("验证码错误");
        return umsMemberFeignClient.registerMember(MemberRegisterFeignInfo.builder().formType(1).username(param.getPhone()).password(param.getPassword()).build());
    }

    private R<String> registerByEmail(MemberEmailRegisterData param) {
        if (!StringUtils.hasLength(param.getEmail()) && StringUtils.hasLength(param.getCode()) && StringUtils.hasLength(param.getPassword()))
            return R.fail("参数错误");
        String verCode = stringRedisTemplate.opsForValue().get(this.getEmailCodeKey(param.getEmail()));
        if (!StringUtils.hasLength(verCode) || !verCode.equals(param.getCode())) return R.fail("验证码错误");
        return umsMemberFeignClient.registerMember(MemberRegisterFeignInfo.builder().formType(2).username(param.getEmail()).password(param.getPassword()).build());
    }

    private String getCaptchaKey(String key) {
        return RedisConstants.MEMBER_CAPTCHA_REGISTER_CODE_KEY.concat(key);
    }

    private String getSmsCodeKey(String phone) {
        return RedisConstants.MEMBER_PHONE_REGISTER_CODE_KEY.concat(phone);
    }

    private String getEmailCodeKey(String email) {
        return RedisConstants.MEMBER_EMAIL_REGISTER_CODE_KEY.concat(email);
    }
}