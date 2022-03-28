/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: RegisterEndpoint
 * Author:   Derek Xu
 * Date:     2022/3/26 19:38
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.auth.boot.endpoint;

import cn.com.xuct.calendar.auth.api.client.MemberFeignClient;
import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.dto.MemberRegisterDto;
import cn.com.xuct.calendar.common.module.params.MemberRegisterParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/26
 * @since 1.0.0
 */
@Slf4j
@Api(tags = "【通用】注册服务")
@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
@Controller
public class RegisterEndpoint {

    private final StringRedisTemplate stringRedisTemplate;

    private final MemberFeignClient memberFeignClient;

    @ApiOperation(value = "会员注册")
    @PostMapping("")
    public R<String> register(@Validated @RequestBody MemberRegisterParam param) {
        Assert.isTrue(StringUtils.hasLength(param.getKey()), "验证失败");
        String verCode = stringRedisTemplate.opsForValue().get(RedisConstants.MEMBER_PHONE_REGISTER_CODE_KEY.concat(param.getKey()));
        if (!StringUtils.hasLength(verCode) || !verCode.toLowerCase().equals(param.getCaptcha().toLowerCase()))
            return R.fail("验证码错误");
        return memberFeignClient.registerMember(MemberRegisterDto.builder().username(param.getUsername()).password(param.getPassword()).build());
    }
}