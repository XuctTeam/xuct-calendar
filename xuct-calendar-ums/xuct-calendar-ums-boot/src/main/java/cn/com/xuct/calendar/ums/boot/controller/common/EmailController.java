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
package cn.com.xuct.calendar.ums.boot.controller.common;

import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.EmailFeignInfoReq;
import cn.com.xuct.calendar.common.module.params.EmailCodeParam;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
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
@Api(tags = "【所有端】邮件接口")
@RestController
@RequestMapping("/api/v1/common/email")
@RequiredArgsConstructor
public class EmailController {

    private final BasicServicesFeignClient basicServicesFeignClient;

    private final StringRedisTemplate stringRedisTemplate;

    @ApiOperation(value = "发送邮件")
    @PostMapping("")
    public R<String> sendEmail(@Validated @RequestBody EmailCodeParam param) {
        if (param.getType() == 1 || param.getType() == 2) {
            String code = this.bindEmail(param.getEmail(), param.getType());
            return basicServicesFeignClient.sendEmail(EmailFeignInfoReq.builder().subject("【楚日历】绑定认证邮件")
                    .template("emailCode")
                    .tos(Lists.newArrayList(param.getEmail()))
                    .params(new HashMap<>() {{
                        put("title", param.getType() == 1 ? "邮箱绑定" : "邮箱解绑");
                        put("code", code);
                        put("userName", JwtUtils.getUsername());
                        put("duration", "2");
                    }})
                    .build());
        }
        return R.fail("发送错误");
    }

    private String bindEmail(final String email, final Integer type) {
        String userId = String.valueOf(JwtUtils.getUserId());
        String key = type == 1 ? RedisConstants.MEMBER_BIND_EMAIL_CODE_KEY : RedisConstants.MEMBER_UNBIND_EMAIL_CODE_KEY;
        String code = RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set(key.concat(userId).concat(":").concat(email), code, 60 * 2, TimeUnit.SECONDS);
        return code;
    }
}