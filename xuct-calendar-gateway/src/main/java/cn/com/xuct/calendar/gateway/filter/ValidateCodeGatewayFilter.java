/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.com.xuct.calendar.gateway.filter;

import cn.com.xuct.calendar.common.core.constant.CacheConstants;
import cn.com.xuct.calendar.common.core.constant.GlobalConstants;
import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.common.core.exception.ValidateCodeException;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.web.utils.WebUtils;
import cn.com.xuct.calendar.gateway.config.GatewayConfigProperties;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * The type Validate code gateway filter.
 *
 * @author lengleng
 * @date 2018 /7/4 登录验证码
 */
@Slf4j
@RequiredArgsConstructor
public class ValidateCodeGatewayFilter extends AbstractGatewayFilterFactory<Object> {

    private final GatewayConfigProperties configProperties;

    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            boolean isAuthToken = CharSequenceUtil.containsAnyIgnoreCase(request.getURI().getPath(), SecurityConstants.OAUTH_TOKEN_URL);

            // 不是登录请求，直接向下执行
            if (!isAuthToken) {
                return chain.filter(exchange);
            }

            // 刷新token，手机号登录（也可以这里进行校验） 直接向下执行
            String grantType = request.getQueryParams().getFirst("grant_type");
            if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType) || StrUtil.equals(SecurityConstants.PHONE_GRANT_TYPE, grantType)) {
                return chain.filter(exchange);
            }
            String clientId = WebUtils.getClientId(request);
            /* 忽略客户端 直接向下执行 */
            boolean isIgnoreClient = configProperties.getIgnoreClients().contains(clientId);
            if (isIgnoreClient) {
                return chain.filter(exchange);
            }
            try {
                checkCode(request, clientId);
            } catch (Exception e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.PRECONDITION_REQUIRED);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

                final String errMsg = e.getMessage();
                return response.writeWith(Mono.create(monoSink -> {
                    try {
                        byte[] bytes = objectMapper.writeValueAsBytes(R.fail(errMsg));
                        DataBuffer dataBuffer = response.bufferFactory().wrap(bytes);

                        monoSink.success(dataBuffer);
                    } catch (JsonProcessingException jsonProcessingException) {
                        log.error("对象输出异常", jsonProcessingException);
                        monoSink.error(jsonProcessingException);
                    }
                }));
            }

            return chain.filter(exchange);
        };
    }

    @Override
    public String name() {
        return "ValidateCodeGatewayFilter";
    }

    @SneakyThrows
    private void checkCode(ServerHttpRequest request, String clientId) {
        String code = request.getQueryParams().getFirst("code");
        if (CharSequenceUtil.isBlank(code)) {
            throw new ValidateCodeException("验证码不能为空");
        }
        String randomStr = request.getQueryParams().getFirst("randomStr");
        if (CharSequenceUtil.isBlank(randomStr)) {
            randomStr = request.getQueryParams().getFirst(SecurityConstants.PHONE_PARAM);
        }
        if (CharSequenceUtil.isBlank(randomStr)) {
            throw new ValidateCodeException("验证信息错误");
        }
        String key = CacheConstants.DEFAULT_LOGIN_CODE_KEY.concat(GlobalConstants.COLON).concat(randomStr);
        Object codeObj = redisTemplate.opsForValue().get(key);
        if (ObjectUtil.isEmpty(codeObj) || !code.equals(codeObj)) {
            throw new ValidateCodeException("验证码无效或已过期");
        }
        redisTemplate.delete(key);
    }
}
