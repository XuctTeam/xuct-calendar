/**
 * Copyright (C), 2015-2022, 楚恬商场
 * FileName: ImageCodeHandler
 * Author:   Derek Xu
 * Date:     2022/9/11 18:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.gateway.handler;

import cn.com.xuct.calendar.common.core.constant.CacheConstants;
import cn.com.xuct.calendar.common.core.constant.GlobalConstants;
import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import com.pig4cloud.captcha.ArithmeticCaptcha;
import com.pig4cloud.captcha.base.Captcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/9/11
 * @since 1.0.0
 */
@Slf4j
@RequiredArgsConstructor
public class ImageCodeHandler implements HandlerFunction<ServerResponse> {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Integer DEFAULT_IMAGE_WIDTH = 100;

    private static final Integer DEFAULT_IMAGE_HEIGHT = 40;

    private static final String REGISTER_IMG = "register";

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {

        Optional<String> randomStr = serverRequest.queryParam("randomStr");
        Optional<String> imgType = serverRequest.queryParam("imgType");
        // 算数验证码
        Captcha captcha = new ArithmeticCaptcha(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
        // 文字验证码
        // Captcha captcha2=new ChineseCaptcha();
        // 生成的验证码
        String text = captcha.text();
        System.out.println("验证码 = " + text);
        // 保存验证码信息
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        if (imgType.isPresent() && randomStr.isPresent() && StringUtils.hasLength(randomStr.get())) {
            String redisKey = "";
            switch (imgType.get()){
                case REGISTER_IMG:
                    redisKey = RedisConstants.MEMBER_CAPTCHA_REGISTER_CODE_KEY;
                    break;
                default:
                    redisKey = CacheConstants.DEFAULT_LOGIN_CODE_KEY;
            }
            final String finalRedisKey = redisKey.concat(GlobalConstants.COLON);
            randomStr.ifPresent(s -> redisTemplate.opsForValue().set(finalRedisKey.concat(s), text, SecurityConstants.CODE_TIME, TimeUnit.SECONDS));
        }
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        captcha.out(os);
        return ServerResponse.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG)
                .body(BodyInserters.fromResource(new ByteArrayResource(os.toByteArray())));
    }

}