/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: ComponentNoAuthController
 * Author:   Derek Xu
 * Date:     2022/4/20 20:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.controller.app;

import cn.com.xuct.calendar.cms.api.entity.Component;
import cn.com.xuct.calendar.cms.api.entity.ComponentAttend;
import cn.com.xuct.calendar.cms.api.vo.ComponentShareVo;
import cn.com.xuct.calendar.cms.boot.service.IComponentAttendService;
import cn.com.xuct.calendar.cms.boot.service.IComponentService;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.feign.PersonInfo;
import cn.com.xuct.calendar.common.security.utils.SecurityUtils;
import cn.com.xuct.calendar.ums.oauth.client.MemberFeignClient;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/4/20
 * @since 1.0.0
 */
@Slf4j
@RestController
@Tag(name = "【移动端】事件非登录服务")
@RequestMapping("/api/v1/anon/component")
@RequiredArgsConstructor
public class ComponentUsersAnonController {

    private final IComponentService componentService;

    private final IComponentAttendService componentAttendService;

    private final MemberFeignClient memberFeignClient;

    @GetMapping("")
    public R<ComponentShareVo> getComponentInfo(@RequestParam("componentId") String componentId) {
        Component component = componentService.getById(componentId);
        if (component == null) return R.fail("未找到事件");
        ComponentShareVo shareVo = new ComponentShareVo();
        BeanUtils.copyProperties(component, shareVo);
        R<PersonInfo> memberFeignInfoR = memberFeignClient.getMemberById(component.getCreatorMemberId());
        if (memberFeignInfoR != null && memberFeignInfoR.isSuccess()) {
            shareVo.setCreateMemberName(memberFeignInfoR.getData().getName());
        }
        Long userId = SecurityUtils.getUserId();
        ComponentAttend attend = componentAttendService.get(Lists.newArrayList(Column.of("component_id", componentId), Column.of("member_id", userId)));
        if (attend != null) {
            shareVo.setAttend(true);
        }
        return R.data(shareVo);
    }
}