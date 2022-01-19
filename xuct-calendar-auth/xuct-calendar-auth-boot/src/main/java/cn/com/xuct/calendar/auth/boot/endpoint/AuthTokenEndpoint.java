/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: AuthController
 * Author:   Derek Xu
 * Date:     2021/11/16 9:11
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.auth.boot.endpoint;

import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import cn.hutool.json.JSONObject;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/16
 * @since 1.0.0
 */
@Slf4j
@Api(tags = "【通用】Token认证服务")
@RestController
@RequiredArgsConstructor
public class AuthTokenEndpoint {

    private final RedisTemplate redisTemplate;

    private final KeyPair keyPair;

    @ApiOperation(value = "注销")
    @DeleteMapping("/oauth/logout")
    public R<String> logout() {
        JSONObject payload = JwtUtils.getJwtPayload();
        String jti = payload.getStr(SecurityConstants.JWT_JTI); // JWT唯一标识
        Long expireTime = payload.getLong(SecurityConstants.JWT_EXP); // JWT过期时间戳(单位：秒)
        if (expireTime != null) {
            long currentTime = System.currentTimeMillis() / 1000;// 当前时间（单位：秒）
            if (expireTime > currentTime) { // token未过期，添加至缓存作为黑名单限制访问，缓存时间为token过期剩余时间
                redisTemplate.opsForValue().set(SecurityConstants.TOKEN_BLACKLIST_PREFIX + jti, null, (expireTime - currentTime), TimeUnit.SECONDS);
            }
        } else { // token 永不过期则永久加入黑名单
            redisTemplate.opsForValue().set(SecurityConstants.TOKEN_BLACKLIST_PREFIX + jti, null);
        }
        return R.data("注销成功");
    }

    @ApiOperation(value = "获取公钥")
    @GetMapping("/oauth/public-key")
    public R<Map<String, Object>> getPublicKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return R.data(new JWKSet(key).toJSONObject());
    }
}