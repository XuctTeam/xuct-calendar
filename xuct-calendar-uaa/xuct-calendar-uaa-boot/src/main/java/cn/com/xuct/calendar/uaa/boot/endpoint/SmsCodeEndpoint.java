/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: SmsCodeEndpoin
 * Author:   Derek Xu
 * Date:     2021/11/29 16:16
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.uaa.boot.endpoint;

import cn.com.xuct.calendar.uaa.api.client.BasicServicesFeignClient;
import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.req.SmsCodeFeignInfo;
import cn.com.xuct.calendar.common.module.params.MemberPhoneParam;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
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
 * @create 2021/11/29
 * @since 1.0.0
 */
@Slf4j
@Api(tags = "【通用】登陆短信")
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
@Controller
public class SmsCodeEndpoint {

    private final StringRedisTemplate stringRedisTemplate;

    private final BasicServicesFeignClient basicServicesFeignClient;

    @ApiOperation(value = "短信验证码")
    @ApiImplicitParam(name = "phoneNumber", example = "17621590365", value = "手机号", required = true)
    @PostMapping
    public R<String> sendSmsCode(@Validated @RequestBody MemberPhoneParam param) {
        String code = RandomUtil.randomNumbers(4);
        String redisKey = RedisConstants.MEMBER_PHONE_LOGIN_CODE_KEY.concat(param.getPhone());
        stringRedisTemplate.opsForValue().set(redisKey, code, 60 * 2, TimeUnit.SECONDS);
        return basicServicesFeignClient.smsCode(SmsCodeFeignInfo.builder().code(code).phones(Lists.newArrayList(param.getPhone())).template("login").build());
    }
}