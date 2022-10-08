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
package cn.com.xuct.calendar.ums.boot.controller.common;

import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.params.SmsSendParam;
import cn.com.xuct.calendar.common.security.annotation.Inner;
import cn.com.xuct.calendar.common.security.utils.SecurityUtils;
import cn.com.xuct.calendar.ums.api.feign.BasicServicesFeignClient;
import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Tag(name = "【所有端】短信接口")
@RestController
@RequestMapping("/api/v1/common/sms")
@RequiredArgsConstructor
public class SmsCodeController {

    private final StringRedisTemplate stringRedisTemplate;

    private final BasicServicesFeignClient basicServicesFeignClient;

    @Operation(summary = "其他短信")
    @PostMapping("")
    public R<String> sendSmsCode(@Validated @RequestBody SmsSendParam param) {
        if (!(param.getType() == 1 || param.getType() == 2)) return R.fail("类型错误");
        String code = this.sendBindCode(param.getPhone(), param.getType());
        //basicServicesFeignClient.smsCode(SmsCodeFeignInfo.builder().phones(Lists.newArrayList(param.getPhone())).code(code).template("bind").build());
        return R.status(true);
    }

    @Inner(value = false)
    @Operation(summary = "登录短信")
    @PostMapping("/login")
    public R<String> sendLoginSmsCode(@RequestParam("phone") String phone) {
        String code = this.sendBindCode(phone, 0);
        //basicServicesFeignClient.smsCode(SmsCodeFeignInfo.builder().phones(Lists.newArrayList(param.getPhone())).code(code).template("login").build());
        return R.status(true);
    }

    private String sendBindCode(String phone, Integer type) {
        String userId = String.valueOf(SecurityUtils.getUserId());
        String key = null;
        switch (type) {
            case 0:
                key = RedisConstants.MEMBER_PHONE_LOGIN_CODE_KEY;
                break;
            case 1:
                key = RedisConstants.MEMBER_BIND_PHONE_CODE_KEY;
                break;
            case 2:
                key = RedisConstants.MEMBER_UNBIND_PHONE_CODE_KEY;
                break;
        }
        String code = RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set(key.concat(userId).concat(":").concat(phone), code, 60 * 2, TimeUnit.SECONDS);
        return code;
    }
}