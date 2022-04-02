/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: AlarmNotifyFeignController
 * Author:   Derek Xu
 * Date:     2022/3/30 10:50
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.feign;

import cn.com.xuct.calendar.common.core.enums.ColumnEnum;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.enums.IdentityTypeEnum;
import cn.com.xuct.calendar.common.module.feign.req.ComponentNotifyFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.EmailFeignInfo;
import cn.com.xuct.calendar.common.web.utils.SpringContextHolder;
import cn.com.xuct.calendar.ums.api.entity.Member;
import cn.com.xuct.calendar.ums.api.entity.MemberAuth;
import cn.com.xuct.calendar.ums.api.feign.BasicServicesFeignClient;
import cn.com.xuct.calendar.ums.boot.event.AlarmNotifyEvent;
import cn.com.xuct.calendar.ums.boot.event.ComponentDelEvent;
import cn.com.xuct.calendar.ums.boot.service.IMemberAuthService;
import cn.com.xuct.calendar.ums.boot.service.IMemberService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/30
 * @since 1.0.0
 */
@Slf4j
@RestController
@Api(tags = "【远程调用】通知接口")
@RequestMapping("/api/feign/v1/component")
@RequiredArgsConstructor
public class ComponentNotifyFeignController {

    private final IMemberAuthService memberAuthService;

    private final IMemberService memberService;

    private final BasicServicesFeignClient basicServicesFeignClient;


    @ApiOperation(value = "删除通知")
    @PostMapping("/delete")
    public R<String> delete(@RequestBody ComponentNotifyFeignInfo componentNotifyFeignInfo) {
        String memberName = null;
        Member member = memberService.getById(componentNotifyFeignInfo.getCreateMemberId());
        if (member != null) {
            memberName = member.getName();
        }
        ComponentDelEvent alarmNotifyEvent = new ComponentDelEvent(this,
                componentNotifyFeignInfo.getSummary(), componentNotifyFeignInfo.getStartDate(), componentNotifyFeignInfo.getLocation(),
                componentNotifyFeignInfo.getComponentId(), componentNotifyFeignInfo.getCreateMemberId(),
                memberName, componentNotifyFeignInfo.getRepeat(), componentNotifyFeignInfo.getTriggerSec(), componentNotifyFeignInfo.getIds());
        SpringContextHolder.publishEvent(alarmNotifyEvent);
        return R.status(true);
    }


    @ApiOperation(value = "发送提醒")
    @PostMapping("/alarm")
    public R<String> notify(@RequestBody ComponentNotifyFeignInfo componentNotifyFeignInfo) {
        Member member = memberService.getById(componentNotifyFeignInfo.getCreateMemberId());
        /*1. 站内信 直接发送 */
        if (componentNotifyFeignInfo.getType() == 1) {
            AlarmNotifyEvent alarmNotifyEvent = new AlarmNotifyEvent(this,
                    componentNotifyFeignInfo.getSummary(), componentNotifyFeignInfo.getStartDate(), componentNotifyFeignInfo.getLocation(), componentNotifyFeignInfo.getComponentId(),
                    componentNotifyFeignInfo.getCreateMemberId(), member.getName(), componentNotifyFeignInfo.getRepeat(), componentNotifyFeignInfo.getTriggerSec(), componentNotifyFeignInfo.getIds());
            SpringContextHolder.publishEvent(alarmNotifyEvent);
            return R.status(true);
        }
        /*2. 邮箱或openid 查询*/
        List<Column> columns = Lists.newArrayList(Column.of("member_id", componentNotifyFeignInfo.getIds(), ColumnEnum.in));
        if (componentNotifyFeignInfo.getType() == 2) {
            columns.add(Column.of("identity_type", IdentityTypeEnum.email));
        } else if (componentNotifyFeignInfo.getType() == 3) {
            columns.add(Column.of("identity_type", IdentityTypeEnum.open_id));
        }
        List<MemberAuth> memberAuths = memberAuthService.find(columns);
        if (CollectionUtils.isEmpty(memberAuths)) {
            log.error("alarm feign controller:: notify error , auths are empty");
            return R.fail("未匹配发送用户");
        }
        /*2. 邮件 发送一封并添加抄送 */
        if (componentNotifyFeignInfo.getType() == 2) {
            List<String> tos = memberAuths.stream().map(auth -> {
                return auth.getUsername();
            }).collect(Collectors.toList());
            basicServicesFeignClient.sendEmail(EmailFeignInfo.builder().subject("事件提醒").tos(tos).template("alarm").params(new HashMap<>() {{
                put("summary", componentNotifyFeignInfo.getSummary());
                put("start_date", componentNotifyFeignInfo.getStartDate());
                put("create_member_name", member.getName());
                put("create_member_id", componentNotifyFeignInfo.getCreateMemberId());
            }}).build());
            return R.status(true);
        }
        /*3. TODO 小程序 发送订阅消息 */
        return R.status(true);
    }
}