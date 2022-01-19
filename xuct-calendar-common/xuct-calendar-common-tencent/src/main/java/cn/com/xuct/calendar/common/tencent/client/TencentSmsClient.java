/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: TencentSmsClient
 * Author:   Derek Xu
 * Date:     2021/11/29 15:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.tencent.client;

import cn.com.xuct.calendar.common.tencent.config.TencentProperties;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/29
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class TencentSmsClient {

    public final SmsClient smsClient;

    public final TencentProperties tencentProperties;

    /**
     * 功能描述: <br>
     * 〈发送短信〉
     *
     * @param templateId 模板 ID: 必须填写已审核通过的模板 ID，可登录 [短信控制台] 查看模板 ID
     * @param phones
     * @return:void
     * @since: 1.0.0
     * @Author:Derek Xu
     * @Date: 2021/11/29 16:02
     */
    public void sendSmsCode(String templateId, String[] phones, String text) throws TencentCloudSDKException {
        /* 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
         * 您可以直接查询 SDK 源码确定接口有哪些属性可以设置
         * 属性可能是基本类型，也可能引用了另一个数据结构
         * 推荐使用 IDE 进行开发，可以方便地跳转查阅各个接口和数据结构的文档说明 */
        SendSmsRequest req = new SendSmsRequest();
        /* 填充请求参数，这里 request 对象的成员变量即对应接口的入参
         * 您可以通过官网接口文档或跳转到 request 对象的定义处查看请求参数的定义
         * 基本类型的设置:
         * 帮助链接：
         * 短信控制台：https://console.cloud.tencent.com/smsv2
         * sms helper：https://cloud.tencent.com/document/product/382/3773 */
        /* 短信应用 ID: 在 [短信控制台] 添加应用后生成的实际 SDKAppID，例如1400006666 */
        req.setSmsSdkAppid(tencentProperties.getSms().getAppId());
        /* 短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名，可登录 [短信控制台] 查看签名信息 */
        req.setSign(tencentProperties.getSms().getSign());
        req.setTemplateID(templateId);
        /* 下发手机号码，采用 e.164 标准，+[国家或地区码][手机号]
         * 例如+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号*/
        req.setPhoneNumberSet(phones);
        /* 模板参数: 若无模板参数，则设置为空*/
        String[] templateParams = {text, "2"};
        req.setTemplateParamSet(templateParams);
        /* 通过 client 对象调用 SendSms 方法发起请求。注意请求方法名与请求对象是对应的
         * 返回的 res 是一个 SendSmsResponse 类的实例，与请求对象对应 */
        SendSmsResponse res = smsClient.SendSms(req);
        // 输出 JSON 格式的字符串回包
        System.out.println(SendSmsResponse.toJsonString(res));
        // 可以取出单个值，您可以通过官网接口文档或跳转到 response 对象的定义处查看返回字段的定义
        System.out.println(res.getRequestId());
    }
}