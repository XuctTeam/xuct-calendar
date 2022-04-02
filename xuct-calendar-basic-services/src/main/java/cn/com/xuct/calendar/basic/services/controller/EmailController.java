/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: EmailController
 * Author:   Derek Xu
 * Date:     2022/3/28 10:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.basic.services.controller;

import cn.com.xuct.calendar.basic.services.service.MailService;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.req.EmailFeignInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
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
@Api(tags = "【基础服务】邮件接口")
@RequiredArgsConstructor
@RequestMapping("/api/basic/v1/email")
public class EmailController {

    private final MailService mailService;

    private final MailProperties mailProperties;

    @ApiOperation(value = "发送邮件")
    @PostMapping("")
    private R<String> sendEmail(@Validated @RequestBody EmailFeignInfo emailFeignInfo) {
        mailService.sendTemplate(mailProperties.getUsername(), emailFeignInfo.getTos(), emailFeignInfo.getCcs(),
                emailFeignInfo.getSubject(), emailFeignInfo.getParams(), emailFeignInfo.getTemplate());
        return R.status(true);
    }
}