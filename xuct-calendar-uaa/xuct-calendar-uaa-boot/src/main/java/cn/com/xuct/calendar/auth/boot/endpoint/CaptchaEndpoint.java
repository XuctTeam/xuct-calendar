/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: CaptchaEndpoint
 * Author:   Derek Xu
 * Date:     2022/2/19 18:59
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.auth.boot.endpoint;

import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/19
 * @since 1.0.0
 */
@Slf4j
@RestController
@Api(tags = "【通用】图形码服务")
@RequestMapping("/captcha")
@RequiredArgsConstructor
public class CaptchaEndpoint {

    private final StringRedisTemplate stringRedisTemplate;

    @SneakyThrows
    @ApiOperation(value = "获取注册验证码")
    @GetMapping("")
    public R<Map<String, String>> captcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        String redisKey = RedisConstants.MEMBER_PHONE_REGISTER_CODE_KEY.concat(key);
        stringRedisTemplate.opsForValue().set(redisKey, verCode, 60 * 2, TimeUnit.SECONDS);

        return R.data(new HashMap<String, String>() {{
            put("key", key);
            put("image", specCaptcha.toBase64(""));
        }});
    }

}