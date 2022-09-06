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
import cn.com.xuct.calendar.common.module.params.MemberPhoneParam;
import cn.com.xuct.calendar.common.security.utils.SecurityUtils;
import cn.com.xuct.calendar.ums.api.feign.BasicServicesFeignClient;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * @create 2022/3/28
 * @since 1.0.0
 */
@Slf4j
@Api(tags = "【所有端】短信接口")
@RestController
@RequestMapping("/api/v1/common/sms")
@RequiredArgsConstructor
public class SmsCodeController {

    private final StringRedisTemplate stringRedisTemplate;

    private final BasicServicesFeignClient basicServicesFeignClient;

    @ApiOperation(value = "发送短信")
    @PostMapping("")
    public R<String> sendSmsCode(@Validated @RequestBody MemberPhoneParam param) {
        if (param.getType() == 1 || param.getType() == 2) {
            String code = this.sendBindCode(param.getPhone(), param.getType());
            //basicServicesFeignClient.smsCode(SmsCodeFeignInfo.builder().phones(Lists.newArrayList(param.getPhone())).code(code).template("bind").build());
            return R.status(true);
        }
        return R.fail("发送错误");
    }

    private String sendBindCode(String phone, Integer type) {
        String userId = String.valueOf(SecurityUtils.getUserId());
        String key = type == 1 ? RedisConstants.MEMBER_BIND_PHONE_CODE_KEY : RedisConstants.MEMBER_UNBIND_PHONE_CODE_KEY;
        String code = RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set(key.concat(userId).concat(":").concat(phone), code, 60 * 2, TimeUnit.SECONDS);
        return code;
    }
}