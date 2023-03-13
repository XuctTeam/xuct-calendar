/**
 * Copyright (C), 2015-2023, XXX有限公司
 * FileName: PublicKeyController
 * Author:   Derek Xu
 * Date:     2023/3/13 9:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu         修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.app;

import cn.com.xuct.calendar.common.core.constant.DateConstants;
import cn.com.xuct.calendar.common.core.constant.GlobalConstants;
import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Derek Xu
 * @create 2023/3/13
 * @since 1.0.0
 */
@Slf4j
@RestController
@Tag(name = "【移动端】公共PublicKey接口")
@RequestMapping("/api/app/v1/anno/public")
@RequiredArgsConstructor
public class PublicKeyController {

    private final RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "【非登录】发送短信认证Key")
    @GetMapping("/key")
    public R<Map> publicKey(@RequestParam("randomStr") String randomStr) {
        String key = UUID.randomUUID().toString(true);
        redisTemplate.opsForValue().set(RedisConstants.MEMBER_PHONE_CODE_PUBLIC_KEY.concat(GlobalConstants.COLON).concat(randomStr), key, DateConstants.TWO_MINUTES_SECONDS, TimeUnit.SECONDS);
        return R.data(new HashMap<String,String>(2){{
            put("key", key);
            put("randomStr" , randomStr);
        }});
    }

    @Operation(summary = "【非登录】获取随机验证码")
    @GetMapping("/random/code")
    public R<Map> publicCode(@RequestParam("phone") String phone) {
        String randomCode = RandomUtil.randomNumbers(5);
        redisTemplate.opsForValue().set(RedisConstants.MEMBER_PHONE_RANDOM_CODE_KEY.concat(GlobalConstants.COLON).concat(phone), randomCode, DateConstants.TWO_MINUTES_SECONDS, TimeUnit.SECONDS);
        return R.data(new HashMap<String,String>(2){{
            put("phone", phone);
            put("randomCode" , randomCode);
        }});
    }
}