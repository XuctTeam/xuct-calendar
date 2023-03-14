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
@RequestMapping("/api/app/v1/anno/public/key")
@RequiredArgsConstructor
public class PublicKeyController {

    private final RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "【非登录】获取客户端认证Key")
    @GetMapping
    public R<Map> publicKey(@RequestParam("randomStr") String randomStr) {
        String key = RandomUtil.randomNumbers(5);
        redisTemplate.opsForValue().set(RedisConstants.DEFAULT_PUBLIC_CODE_KEY.concat(GlobalConstants.COLON).concat(randomStr), key, DateConstants.TWO_MINUTES_SECONDS, TimeUnit.SECONDS);
        return R.data(new HashMap<String,String>(2){{
            put("key", key);
            put("randomStr" , randomStr);
        }});
    }
}