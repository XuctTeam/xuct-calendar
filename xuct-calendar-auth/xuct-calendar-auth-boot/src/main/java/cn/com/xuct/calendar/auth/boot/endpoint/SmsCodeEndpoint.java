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
package cn.com.xuct.calendar.auth.boot.endpoint;

import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.tencent.client.TencentSmsClient;
import cn.com.xuct.calendar.common.tencent.config.TencentProperties;
import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import cn.com.xuct.calendar.common.module.params.MemberPhoneParam;
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
@Api(tags = "【通用】短信服务")
@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
@Controller
public class SmsCodeEndpoint {

    private final TencentSmsClient tencentSmsClient;

    private final TencentProperties tencentProperties;

    private final StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "短信验证码服务")
    @ApiImplicitParam(name = "phoneNumber", example = "17621590365", value = "手机号", required = true)
    @PostMapping
    public R<String> sendSmsCode(@Validated @RequestBody MemberPhoneParam param) {
        switch (param.getType()) {
            case 0:
                return this.sendLoginCode(param.getPhone());
            case 1:
            case 2:
                return this.sendBindCode(param.getPhone(), param.getType());
            default:
                return R.status(false);
        }
    }

    private R<String> sendLoginCode(String phone) {
        String code = RandomUtil.randomNumbers(4);
        String redisKey = RedisConstants.MEMBER_PHONE_LOGIN_CODE_KEY.concat(phone);
        stringRedisTemplate.opsForValue().set(redisKey, code, 60 * 2, TimeUnit.SECONDS);
        //        try {
//            tencentSmsClient.sendSmsCode(tencentProperties.getSms().getTemplateId().get("login"), new String[]{"+86".concat(smsCodeRep.getPhoneNumber())}, code);
//        } catch (TencentCloudSDKException e) {
//            e.printStackTrace();
//            stringRedisTemplate.delete(redisKey);
//            return R.fail(ResultCode.SMS_SEND_ERROR, e.getMessage());
//        }
        return R.success("发送成功");
    }

    private R<String> sendBindCode(String phone, Integer type) {
        String key = type == 1 ? RedisConstants.MEMBER_UNBIND_PHONE_CODE_KEY : RedisConstants.MEMBER_BIND_PHONE_CODE_KEY;
        String code = RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set(key.concat(phone), code, 60 * 2, TimeUnit.SECONDS);
        return R.success("发送成功");
    }
}