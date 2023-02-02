/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: ShortChainController
 * Author:   Derek Xu
 * Date:     2022/5/5 17:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.basic.services.controller;

import cn.com.xuct.calendar.basic.services.config.ShortChainDomainConfiguration;
import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.utils.JsonUtils;
import cn.com.xuct.calendar.common.core.utils.StrUtils;
import cn.com.xuct.calendar.common.module.feign.req.ShortChainFeignInfo;
import cn.hutool.core.date.DateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/5/5
 * @since 1.0.0
 */
@Slf4j
@RestController
@Tag(name = "【基础服务】短链接口")
@RequiredArgsConstructor
@RequestMapping("/api/basic/v1/short/chain")
public class ShortChainController {

    private final ShortChainDomainConfiguration domainConfiguration;

    private final StringRedisTemplate stringRedisTemplate;

    @Operation(summary = "获取短链")
    @PostMapping("")
    public R<String> get(@Validated @RequestBody ShortChainFeignInfo shortChain) {
        String domain = domainConfiguration.getDomain(shortChain.getType());
        if (!StringUtils.hasLength(domain)) {
            return R.status(false);
        }
        String minKey = StrUtils.getMiniUuid();
        boolean setRedisFlag = setIfAbsentRedisKey(minKey, shortChain);
        if (!setRedisFlag) {
            minKey = StrUtils.getMiniUuid();
            setRedisFlag = setIfAbsentRedisKey(minKey, shortChain);
        }
        if (!setRedisFlag) {
            log.error("url controller:: displace url is error , form = {}", JsonUtils.obj2json(shortChain));
            return R.status(false);
        }
        String respUrl = domain.concat(domain.endsWith("/") ? minKey : "/".concat(minKey));
        log.info("short url controller:: url success , long url = {} , short url = {}", shortChain.getUrl(), respUrl);
        return R.data(respUrl);
    }

    @Operation(summary = "短链换长链")
    @GetMapping("/{key:[a-zA-Z0-9]{6}}")
    public RedirectView join(HttpServletRequest request, @PathVariable("key") String key) {

        String redisKey = this.getShortKey(key);
        log.info("join controller:: get redis key = {}", redisKey);
        String val = stringRedisTemplate.opsForValue().get(redisKey);
        if (!StringUtils.hasLength(val)) {
            String notFound = domainConfiguration.getNotFound(request.getServerName());
            if (!StringUtils.hasLength(notFound)) {
                notFound = "404";
            }
            return new RedirectView(notFound);
        }
        return new RedirectView(val);
    }

    /**
     * 功能描述: <br>
     * 〈当不存在时设置redis值〉
     *
     * @param minKey
     * @param shortChain
     * @return:boolean
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/2/7 10:10
     */
    private boolean setIfAbsentRedisKey(String minKey, ShortChainFeignInfo shortChain) {
        String redisKey = this.getShortKey(minKey);
        long expireTime = shortChain.getExpire() > DateUtil.current() ? shortChain.getExpire() - DateUtil.current() : -1;
        log.debug("short controller:: form = {} , key = {} , expireTime = {} ", shortChain.toString(), redisKey, expireTime);
        return stringRedisTemplate.opsForValue().setIfAbsent(redisKey, shortChain.getUrl(), expireTime, TimeUnit.MILLISECONDS);
    }


    private String getShortKey(String minKey) {
        return RedisConstants.BASIC_SHORT_URL_KEY.concat(minKey);
    }
}