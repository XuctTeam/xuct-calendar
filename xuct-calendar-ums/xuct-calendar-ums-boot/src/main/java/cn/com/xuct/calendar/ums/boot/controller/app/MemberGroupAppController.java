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
import cn.com.xuct.calendar.common.core.utils.PinYinUtils;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.enums.GroupMemberStatusEnum;
import cn.com.xuct.calendar.common.module.params.GroupApplyParam;
import cn.com.xuct.calendar.common.module.params.GroupJoinParam;
import cn.com.xuct.calendar.common.module.params.GroupLeaveParam;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import cn.com.xuct.calendar.common.web.utils.SpringContextHolder;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/28
 * @since 1.0.0
 */
@Slf4j
@Api(tags = "【移动端】会员群组接口")
@RestController
@RequestMapping("/api/app/v1/mbr/group")
@RequiredArgsConstructor
public class MemberGroupAppController {

    private final IGroupService groupService;

    private final IMemberGroupService memberGroupService;

    @ApiOperation(value = "按拼音分组用户")
    @GetMapping("")
    public R<List<GroupMemberPinYinVo>> list() {
        List<GroupMemberInfoDto> memberInfoDtos = memberGroupService.list(JwtUtils.getUserId());
        if (CollectionUtils.isEmpty(memberInfoDtos)) return R.data(Lists.newArrayList());
        TreeMap<String, List<GroupMemberInfoDto>> pinyinVos = new TreeMap<String, List<GroupMemberInfoDto>>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        memberInfoDtos.stream().forEach(x -> {
            String first = PinYinUtils.first(x.getName());
            if (!pinyinVos.containsKey(first)) {
                pinyinVos.put(first, Lists.newArrayList(x));
                return;
            }
            pinyinVos.get(first).add(x);
        });
        List<GroupMemberPinYinVo> groupMemberPinYinVos = Lists.newArrayList();
        GroupMemberPinYinVo pinVo = null;
        for (String pinyin : pinyinVos.keySet()) {
            pinVo = new GroupMemberPinYinVo();
            pinVo.setCharCode(pinyin);
            pinVo.setMembers(pinyinVos.get(pinyin));
            groupMemberPinYinVos.add(pinVo);
        }
        return R.data(groupMemberPinYinVos);
    }

    @ApiOperation(value = "通过群组查询")
    @GetMapping("/query")
    public R<List<GroupMemberInfoDto>> queryMembers(@RequestParam("groupId") Long groupId) {
        return R.data(memberGroupService.queryMembersByGroupId(groupId));
    }

    @ApiOperation(value = "按组内用户去重查询")
    @GetMapping("/distinct")
    public R<List<GroupMemberInfoDto>> distinctGroupMembers() {
        return R.data(memberGroupService.distinctGroupMembers(JwtUtils.getUserId()));
    }

    @ApiOperation(value = "申请入群")
    @PostMapping("/apply")
    public R<String> applyGroup(@RequestBody @Validated GroupJoinParam joinParam) {
        MemberGroup memberGroup = memberGroupService.get(Lists.newArrayList(Column.of("group_id", joinParam.getId()), Column.of("member_id", JwtUtils.getUserId())));
        if (memberGroup != null) {
            if (memberGroup.getStatus().equals(GroupMemberStatusEnum.APPLY)) return R.fail("已在申请中");
            return R.fail("已在群组");
        }
        GroupInfoDto groupInfoDto = groupService.getGroupCountByGroupId(Long.valueOf(joinParam.getId()));
        if (groupInfoDto == null) return R.fail("群组不存在");
        if (groupInfoDto.getCount() > 200) return R.fail("群组已满");
        memberGroupService.applyJoinGroup(groupInfoDto.getId(), groupInfoDto.getName(), groupInfoDto.getCreateMemberId(), JwtUtils.getUserId());
        /* 发出申请入群消息 */
        SpringContextHolder.publishEvent(new GroupApplyEvent(this, JwtUtils.getUserId(), groupInfoDto.getId(), groupInfoDto.getName(), groupInfoDto.getCreateMemberId()));
        return R.status(true);
    }

    @ApiOperation(value = "同意入群")
    @PostMapping("/apply/agree")
    public R<String> applyAgreeJoinGroup(@RequestBody @Validated GroupApplyParam groupApplyParam) {
        memberGroupService.applyAgreeJoinGroup(groupApplyParam.getGroupId(), groupApplyParam.getMemberId());
        /* 发出入群同意消息 */
        SpringContextHolder.publishEvent(new GroupApplyOptionEvent(this, groupApplyParam.getGroupId(), groupApplyParam.getMemberId(), 1));
        return R.status(true);
    }

    @ApiOperation(value = "拒绝入群")
    @PostMapping("/apply/refuse")
    public R<String> applyRefuseJoinGroup(@RequestBody @Validated GroupApplyParam groupApplyParam) {
        memberGroupService.applyRefuseJoinGroup(groupApplyParam.getGroupId(), groupApplyParam.getMemberId());
        /* 发出入群拒绝消息 */
        SpringContextHolder.publishEvent(new GroupApplyOptionEvent(this, groupApplyParam.getGroupId(), groupApplyParam.getMemberId(), 2));
        return R.status(true);
    }

    @ApiOperation(value = "请离或主动退组")
    @PostMapping("/leave")
    public R<String> leave(@RequestBody @Validated GroupLeaveParam param) {
        Group group = groupService.getById(param.getGroupId());
        Assert.notNull(group, "组不存在或非管理员");
        if (param.getAction() == 4) {
            if (!String.valueOf(group.getMemberId()).equals(String.valueOf(JwtUtils.getUserId())))
                return R.fail("非管理员");
            if (param.getMemberId() == null) return R.fail("会员ID不能为空");
        }
        Long mId = param.getAction() == 3 ? JwtUtils.getUserId() : param.getMemberId();
        memberGroupService.leaveOut(param.getGroupId(), mId);
        /* 发出清理消息 */
        SpringContextHolder.publishEvent(new GroupLeaveEvent(this, group.getId(), group.getName(), mId, param.getAction()));
        return R.status(true);
    }
}