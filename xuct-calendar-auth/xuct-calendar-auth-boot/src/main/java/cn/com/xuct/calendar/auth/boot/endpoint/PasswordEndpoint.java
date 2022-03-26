/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: PasswordEndpoint
 * Author:   Derek Xu
 * Date:     2022/3/26 23:31
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.auth.boot.endpoint;

import cn.com.xuct.calendar.auth.api.client.MemberFeignClient;
import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.dto.MemberInfoDto;
import cn.com.xuct.calendar.common.module.dto.MemberModifyPasswordDto;
import cn.com.xuct.calendar.common.module.params.ForgetModifyParam;
import cn.com.xuct.calendar.common.module.params.ForgetPasswordParam;
import cn.hutool.core.util.RandomUtil;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/26
 * @since 1.0.0
 */
@Slf4j
@Api(tags = "【通用】密码找回服务")
@RestController
@RequestMapping("/forget/password")
@RequiredArgsConstructor
@Controller
public class PasswordEndpoint {

    private final StringRedisTemplate stringRedisTemplate;

    private final MemberFeignClient memberFeignClient;

    @ApiOperation(value = "发送验证码")
    @PostMapping("/code")
    public R<Map<String, String>> sendForgetCode(@Validated @RequestBody ForgetPasswordParam forgetPasswordParam) {
        String userId = null;
        if (forgetPasswordParam.getType() == 1) {
            Assert.notNull(forgetPasswordParam.getPhone(), "手机号码为空");
            userId = this.sendForgetPasswordByPhone(forgetPasswordParam.getPhone());
        } else if (forgetPasswordParam.getType() == 2) {
            Assert.notNull(forgetPasswordParam.getEmail(), "邮箱为空");
            userId = this.sendForgetPasswordByEmail(forgetPasswordParam.getPhone());
        }
        if (!StringUtils.hasLength(userId)) return R.fail("用户不存在");
        final String finalUserId = userId;
        return R.data(new HashMap<>() {{
            put("memberId", String.valueOf(finalUserId));
        }});
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/modify")
    public R<String> modify(@Validated @RequestBody ForgetModifyParam forgetModifyParam) {
        String cacheCode = null;
        if (forgetModifyParam.getType() == 1) {
            cacheCode = stringRedisTemplate.opsForValue().get(RedisConstants.MEMBER_FORGET_PASSWORD_PHONE_CODE_KEY.concat(forgetModifyParam.getMemberId()));
        } else if (forgetModifyParam.getType() == 2) {
            cacheCode = stringRedisTemplate.opsForValue().get(RedisConstants.MEMBER_FORGET_PASSWORD_EMAIL_CODE_KEY.concat(forgetModifyParam.getMemberId()));
        }
        if (!StringUtils.hasLength(cacheCode) || !cacheCode.toLowerCase().equals(forgetModifyParam.getCode().toLowerCase()))
            return R.fail("验证失败");
        return memberFeignClient.modifyPassword(MemberModifyPasswordDto.builder().memberId(Long.valueOf(forgetModifyParam.getMemberId())).password(forgetModifyParam.getPassword()).build());
    }


    private String sendForgetPasswordByPhone(final String phone) {
        R<MemberInfoDto> memberResult = memberFeignClient.loadMemberByMobile(phone);
        if (memberResult == null || !memberResult.isSuccess()) return null;
        String code = RandomUtil.randomNumbers(4);
        String userId = String.valueOf(memberResult.getData().getUserId());
        stringRedisTemplate.opsForValue().set(RedisConstants.MEMBER_FORGET_PASSWORD_PHONE_CODE_KEY.concat(userId), code, 120, TimeUnit.SECONDS);
        /* TODO 发送短信   0*/
        return userId;
    }


    private String sendForgetPasswordByEmail(final String email) {
        R<MemberInfoDto> memberResult = memberFeignClient.loadMemberByEmail(email);
        if (memberResult == null || !memberResult.isSuccess()) return null;
        String code = RandomUtil.randomNumbers(4);
        String userId = String.valueOf(memberResult.getData().getUserId());
        stringRedisTemplate.opsForValue().set(RedisConstants.MEMBER_FORGET_PASSWORD_EMAIL_CODE_KEY.concat(userId), code);
        /* TODO 发送邮件 */
        return userId;
    }
}