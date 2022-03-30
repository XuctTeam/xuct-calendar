/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: SmsController
 * Author:   Derek Xu
 * Date:     2022/3/28 10:22
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.basic.services.controller;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.res.ResultCode;
import cn.com.xuct.calendar.common.module.feign.SmsCodeFeignInfoReq;
import cn.com.xuct.calendar.common.tencent.client.TencentSmsClient;
import cn.com.xuct.calendar.common.tencent.config.TencentProperties;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
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
 * @create 2022/3/28
 * @since 1.0.0
 */
@Slf4j
@RestController
@Api(tags = "【基础服务】短信接口")
@RequiredArgsConstructor
@RequestMapping("/api/basic/v1/sms")
public class SmsController {

    private final TencentSmsClient tencentSmsClient;

    private final TencentProperties tencentProperties;


    @ApiOperation(value = "发送短信")
    @PostMapping("")
    public R<String> smsCode(@Validated @RequestBody SmsCodeFeignInfoReq smsCodeFeignInfoReq) {
        Assert.isTrue(!CollectionUtils.isEmpty(smsCodeFeignInfoReq.getPhones()), "联系人为空");
        String[] phones = new String[smsCodeFeignInfoReq.getPhones().size()];
        for (int i = 0; i < smsCodeFeignInfoReq.getPhones().size(); i++) {
            phones[i] = "+86".concat(smsCodeFeignInfoReq.getPhones().get(i));
        }
        try {
            tencentSmsClient.sendSmsCode(tencentProperties.getSms().getTemplateId().get(smsCodeFeignInfoReq.getTemplate()), phones, smsCodeFeignInfoReq.getCode());
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return R.fail(ResultCode.SMS_SEND_ERROR, e.getMessage());
        }
        return R.status(true);
    }

}