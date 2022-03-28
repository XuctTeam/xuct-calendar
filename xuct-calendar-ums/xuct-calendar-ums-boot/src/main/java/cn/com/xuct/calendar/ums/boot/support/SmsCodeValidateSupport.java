/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: SmsCodeValidateSuppot
 * Author:   Derek Xu
 * Date:     2022/3/28 14:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.support;

import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.exception.SvrException;
import cn.com.xuct.calendar.common.core.res.SvrResCode;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/28
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class SmsCodeValidateSupport {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 功能描述: <br>
     * 〈〉
     *
     * @param type
     * @param val
     * @param code
     * @return:java.lang.String
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2022/3/28 15:00
     */
    public void validateCode(Integer type, String val, String code) {
        String userId = String.valueOf(JwtUtils.getUserId());
        String redisKeys = this.redisKey(type).concat(userId).concat(":").concat(val);
        String cacheCode = stringRedisTemplate.opsForValue().get(redisKeys);
        if (!StringUtils.hasLength(cacheCode) || !StringUtils.hasLength(code) || !code.toLowerCase().equals(cacheCode.toLowerCase()))
            throw new SvrException(SvrResCode.UMS_SMS_CODE_ERROR);
        stringRedisTemplate.delete(redisKeys);
    }


    private String redisKey(Integer type) {
        switch (type) {
            case 1:
                return RedisConstants.MEMBER_BIND_PHONE_CODE_KEY;
            case 2:
                return RedisConstants.MEMBER_UNBIND_PHONE_CODE_KEY;
            case 3:
                return RedisConstants.MEMBER_BIND_EMAIL_CODE_KEY;
            case 4:
                return RedisConstants.MEMBER_UNBIND_EMAIL_CODE_KEY;
            default:
                return null;
        }
    }

}