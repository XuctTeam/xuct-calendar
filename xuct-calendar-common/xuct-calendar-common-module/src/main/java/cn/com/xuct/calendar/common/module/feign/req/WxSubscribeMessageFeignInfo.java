/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: WxSubscribeMessageFeignInfo
 * Author:   Derek Xu
 * Date:     2022/3/30 9:15
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.common.module.feign.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/30
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxSubscribeMessageFeignInfo implements Serializable {

    /**
     * 接收者（用户）的 openid.
     * <pre>
     * 参数：touser
     * 是否必填： 是
     * 描述： 接收者（用户）的 openid
     * </pre>
     */
    @Schema(title = "接收者(OPENID)")
    private String toUser;

    /**
     * 所需下发的模板消息的id.
     * <pre>
     * 参数：template_id
     * 是否必填： 是
     * 描述： 所需下发的模板消息的id
     * </pre>
     */
    @Schema(title ="所需下发的模板消息的id")
    private String templateId;

    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面.
     * <pre>
     * 参数：page
     * 是否必填： 否
     * 描述： 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
     * </pre>
     */
    @Schema(title ="点击模板卡片后的跳转页面")
    private String page;

    /**
     * 模板内容，不填则下发空模板.
     * <pre>
     * 参数：data
     * 是否必填： 是
     * 描述： 模板内容，不填则下发空模板
     * </pre>
     */
    @Schema(title ="模板内容")
    private List<MsgData> data;

    /**
     * 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
     */
    @Schema(title = " 跳转小程序类型" , description = "developer为开发版；trial为体验版；formal为正式版")
    private String miniprogramState = "formal";

    /**
     * 进入小程序查看的语言类型，支持zh_CN(简体中文)、en_US(英文)、zh_HK(繁体中文)、zh_TW(繁体中文)，默认为zh_CN
     */
    @Schema(title = "进入小程序查看的语言类型")
    private String lang = "ZH_CN";

    public WxSubscribeMessageFeignInfo addData(MsgData datum) {
        if (this.data == null) {
            this.data = new ArrayList<>();
        }

        this.data.add(datum);

        return this;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MsgData implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private String value;
    }
}