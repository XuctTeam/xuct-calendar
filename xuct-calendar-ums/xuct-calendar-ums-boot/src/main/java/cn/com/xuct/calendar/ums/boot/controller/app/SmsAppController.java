/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: SmsAppController
 * Author:   Derek Xu
 * Date:     2021/12/14 11:20
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.app;

import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.params.MemberPhoneParam;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @create 2021/12/14
 * @since 1.0.0
 */
@Slf4j
@RestController
@Api(tags = "【移动端】短信接口")
@RequestMapping("/api/app/v1/sms")
@RequiredArgsConstructor
public class SmsAppController {

    private final StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "手机号绑定/解绑短信")
    @PostMapping("/phone/bind/code")
    public R<String> phoneBindSmdCode(@Validated @RequestBody MemberPhoneParam param) {
        Long userId = JwtUtils.getUserId();
        String code = RandomUtil.randomNumbers(6);
        String key = param.getEdit() ? RedisConstants.MEMBER_UNBIND_PHONE_CODE_KEY : RedisConstants.MEMBER_BIND_PHONE_CODE_KEY;
        stringRedisTemplate.opsForValue().set(key.concat(userId.toString()).concat(":").concat(param.getPhone()), code, 120, TimeUnit.SECONDS);
        return R.status(true);
    }
}