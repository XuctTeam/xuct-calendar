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

import ch.qos.logback.core.net.SMTPAppenderBase;
import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.req.EmailFeignInfo;
import cn.com.xuct.calendar.common.module.params.EmailCodeParam;
import cn.com.xuct.calendar.common.security.annotation.Inner;
import cn.com.xuct.calendar.common.security.utils.SecurityUtils;
import cn.com.xuct.calendar.ums.api.feign.BasicServicesFeignClient;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "【所有端】邮件接口")
@RestController
@RequestMapping("/api/v1/common/email")
@RequiredArgsConstructor
public class EmailController {

    private final BasicServicesFeignClient basicServicesFeignClient;

    private final StringRedisTemplate stringRedisTemplate;

    @Operation(summary = "发送邮件")
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
                        SMTPAppenderBase<Object> JwtUtils;
                        put("userName", SecurityUtils.getUserName());
                        put("duration", "2");
                    }})
                    .build());
        }
        return R.fail("发送错误");
    }

    private String bindEmail(final String email, final Integer type) {
        String userId = String.valueOf(SecurityUtils.getUserId());
        String key = type == 1 ? RedisConstants.MEMBER_BIND_EMAIL_CODE_KEY : RedisConstants.MEMBER_UNBIND_EMAIL_CODE_KEY;
        String code = RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set(key.concat(userId).concat(":").concat(email), code, 60 * 2, TimeUnit.SECONDS);
        return code;
    }
}