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

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        // 算数验证码
        Captcha captcha = new ArithmeticCaptcha(DEFAULT_IMAGE_WIDTH , DEFAULT_IMAGE_HEIGHT);
        // 文字验证码
        // Captcha captcha2=new ChineseCaptcha();
        // 生成的验证码
        String text = captcha.text();
        System.out.println("验证码 = " + text);
        // 保存验证码信息
        Optional<String> randomStr = serverRequest.queryParam("randomStr");
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        randomStr.ifPresent(s -> redisTemplate.opsForValue().set(CacheConstants.DEFAULT_CODE_KEY + s, text, SecurityConstants.CODE_TIME, TimeUnit.SECONDS));
        // 转换流信息写出
        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
        captcha.out(os);
        return ServerResponse.status(HttpStatus.OK).contentType(MediaType.IMAGE_JPEG)
                .body(BodyInserters.fromResource(new ByteArrayResource(os.toByteArray())));
    }

}