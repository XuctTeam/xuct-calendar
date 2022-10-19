/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: SmsUserAnonController
 * Author:   Derek Xu
 * Date:     2022/10/19 17:15
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.app;

import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.params.SmsSendParam;
import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
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
 * @create 2022/10/19
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/v1/anon/sms")
@Tag(name = "【所有端】【不认证】短信接口")
@RequiredArgsConstructor
public class SmsUserAnonController {

    private final StringRedisTemplate stringRedisTemplate;


    @Operation(summary = "发送短信")
    @PostMapping("")
    public R<String> sendLoginSmsCode(@Validated @RequestBody SmsSendParam param) {
        String code = this.sendBindCode(param.getPhone(), 0);
        //basicServicesFeignClient.smsCode(SmsCodeFeignInfo.builder().phones(Lists.newArrayList(param.getPhone())).code(code).template("login").build());
        return R.status(true);
    }

    private String sendBindCode(String phone, Integer type) {
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
        stringRedisTemplate.opsForValue().set(key.concat(":").concat(phone), code, 60 * 10, TimeUnit.SECONDS);
        return code;
    }
}