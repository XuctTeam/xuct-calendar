/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: MailService
 * Author:   Derek Xu
 * Date:     2022/3/27 17:58
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.basic.services.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/27
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;

    /**
     * 发送html模板邮件
     *
     * @param from     邮件发送者
     * @param to       收件人
     * @param subject  邮件主题
     * @param params   html格式的邮件内容
     * @param template 模板名称
     */
    @SneakyThrows
    public void sendTemplate(String from, List<String> to, List<String> cc, String subject, Map<String, Object> params, String template) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject(subject);
        helper.setFrom(from);
        helper.setTo(to.toArray(new String[0]));
        helper.setSentDate(new Date());
        if (!CollectionUtils.isEmpty(cc)) {
            helper.setCc(cc.toArray(new String[0]));
        }
        // 这里引入的是Template的Context
        Context context = new Context();
        // 设置模板中的变量
        for (String key : params.keySet()) {
            context.setVariable(key, params.get(key));
        }
        // 第一个参数为模板的名称
        String process = templateEngine.process(template, context);
        // 第二个参数true表示这是一个html文本
        helper.setText(process, true);
        javaMailSender.send(mimeMessage);
    }
}