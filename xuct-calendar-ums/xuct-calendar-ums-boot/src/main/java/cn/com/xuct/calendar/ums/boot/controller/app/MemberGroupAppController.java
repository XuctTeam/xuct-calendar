/**
 * Copyright (C), 2015-2022, 楚恬
 * FileName: MemberGroupAppController
 * Author:   Derek Xu
 * Date:     2022/2/28 21:04
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.app;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.enums.GroupMemberStatusEnum;
import cn.com.xuct.calendar.common.module.params.GroupApplyParam;
import cn.com.xuct.calendar.common.module.params.GroupJoinParam;
import cn.com.xuct.calendar.common.module.params.GroupLeaveParam;
import cn.com.xuct.calendar.common.module.params.GroupMemberIdsParam;
import cn.com.xuct.calendar.common.security.utils.SecurityUtils;
import cn.com.xuct.calendar.common.core.utils.SpringContextHolder;
import cn.com.xuct.calendar.ums.api.dto.GroupInfoDto;
import cn.com.xuct.calendar.ums.api.dto.GroupMemberInfoDto;
import cn.com.xuct.calendar.ums.api.entity.Group;
import cn.com.xuct.calendar.ums.api.entity.MemberGroup;
import cn.com.xuct.calendar.ums.api.vo.GroupMemberPinYinVo;
import cn.com.xuct.calendar.ums.boot.event.GroupApplyEvent;
import cn.com.xuct.calendar.ums.boot.event.GroupApplyOptionEvent;
import cn.com.xuct.calendar.ums.boot.event.GroupLeaveEvent;
import cn.com.xuct.calendar.ums.boot.service.IGroupService;
import cn.com.xuct.calendar.ums.boot.service.IMemberGroupService;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/28
 * @since 1.0.0
 */
@Slf4j
@Tag(name = "【移动端】会员群组接口")
@RestController
@RequestMapping("/api/app/v1/mbr/group")
@RequiredArgsConstructor
public class MemberGroupAppController {

    private final IGroupService groupService;

    private final IMemberGroupService memberGroupService;

    @Operation(summary = "按拼音分组用户")
    @GetMapping("")
    public R<List<GroupMemberPinYinVo>> list(@RequestParam("groupId") Long groupId) {
        return R.data(memberGroupService.listByPinYin(groupId));
    }

    @Operation(summary = "通过群组查询")
    @GetMapping("/query")
    public R<List<GroupMemberInfoDto>> queryMembers(@RequestParam("groupId") Long groupId) {
        return R.data(memberGroupService.listByGroupIdAndNotMember(groupId, SecurityUtils.getUserId()));
    }




    @Operation(summary = "通过ids查询")
    @PostMapping("/ids")
    public R<List<GroupMemberInfoDto>> queryMemberIds(@RequestBody GroupMemberIdsParam idsParam) {
        return R.data(memberGroupService.listByMemberIds(idsParam.getIds()));
    }

    @Operation(summary = "申请入群")
    @PostMapping("/apply")
    public R<String> applyGroup(@RequestBody @Validated GroupJoinParam joinParam) {
        MemberGroup memberGroup = memberGroupService.get(Lists.newArrayList(Column.of("group_id", joinParam.getId()), Column.of("member_id", SecurityUtils.getUserId())));
        if (memberGroup != null) {
            if (memberGroup.getStatus().equals(GroupMemberStatusEnum.APPLY)) return R.fail("已在申请中");
            return R.fail("已在群组");
        }
        GroupInfoDto groupInfoDto = groupService.getGroupCountByGroupId(Long.valueOf(joinParam.getId()));
        if (groupInfoDto == null)
            return R.fail("群组不存在");
        /* 有密码是判断密码 */
        if (StringUtils.hasLength(groupInfoDto.getPassword()) && !joinParam.getPassword().equals(groupInfoDto.getPassword()))
            return R.fail("密码错误");
        /* 已有人数大于群组配置人数 */
        if (groupInfoDto.getCount() > groupInfoDto.getNum())
            return R.fail("群组已满");
        memberGroupService.applyJoinGroup(groupInfoDto.getId(), groupInfoDto.getName(), groupInfoDto.getCreateMemberId(), SecurityUtils.getUserId());
        /* 发出申请入群消息 */
        SpringContextHolder.publishEvent(new GroupApplyEvent(this, SecurityUtils.getUserId(), groupInfoDto.getId(), groupInfoDto.getName(), groupInfoDto.getCreateMemberId()));
        return R.status(true);
    }

    @Operation(summary = "同意入群")
    @PostMapping("/apply/agree")
    public R<String> agreeApply(@RequestBody @Validated GroupApplyParam groupApplyParam) {
        MemberGroup memberGroup = memberGroupService.getById(groupApplyParam.getId());
        Assert.notNull(memberGroup, "申请不存在");
        Group group = groupService.getById(memberGroup.getGroupId());
        Assert.notNull(group, "群组不存在");
        Assert.isTrue(memberGroup.getStatus().equals(GroupMemberStatusEnum.APPLY), "状态错误");
        memberGroup.setStatus(GroupMemberStatusEnum.NORMAL);
        memberGroupService.updateById(memberGroup);
        /* 发出入群同意消息 */
        SpringContextHolder.publishEvent(new GroupApplyOptionEvent(this, group.getId(), group.getName(), memberGroup.getMemberId(), groupApplyParam.getAction()));
        return R.status(true);
    }

    @Operation(summary = "拒绝或撤回申请")
    @PostMapping("/apply/refuse")
    public R<String> refuseApply(@RequestBody @Validated GroupApplyParam groupApplyParam) {
        MemberGroup memberGroup = memberGroupService.getById(groupApplyParam.getId());
        Assert.notNull(memberGroup, "申请不存在");
        Assert.isTrue(memberGroup.getStatus().equals(GroupMemberStatusEnum.APPLY), "状态错误");
        memberGroupService.removeById(groupApplyParam.getId());
        Group group = groupService.getById(memberGroup.getGroupId());
        /* 发出入群拒绝消息 */
        SpringContextHolder.publishEvent(new GroupApplyOptionEvent(this, memberGroup.getGroupId(), group != null ? group.getName() : "", memberGroup.getMemberId(), groupApplyParam.getAction()));
        return R.status(true);
    }

    @Operation(summary = "请离或主动退组")
    @PostMapping("/leave")
    public R<String> leave(@RequestBody @Validated GroupLeaveParam param) {
        Group group = groupService.getById(param.getGroupId());
        Assert.notNull(group, "组不存在或非群主");
        if (param.getAction() == 5) {
            if (!String.valueOf(group.getMemberId()).equals(String.valueOf(SecurityUtils.getUserId())))
                return R.fail("非群主");
            if (param.getMemberId() == null) return R.fail("会员ID不能为空");
        }
        Long mId = param.getAction() == 4 ? SecurityUtils.getUserId() : param.getMemberId();
        memberGroupService.leaveOut(param.getGroupId(), mId);
        /* 发出清理消息 */
        SpringContextHolder.publishEvent(new GroupLeaveEvent(this, group.getId(), group.getName(), mId, param.getAction()));
        return R.status(true);
    }

    @Operation(summary = "查询组内用户")
    @GetMapping("/get")
    public R<GroupMemberInfoDto> getGroupMember(@RequestParam("groupId") Long groupId, @RequestParam("memberId") Long memberId) {
        return R.data(memberGroupService.getGroupMember(groupId, memberId));
    }
}