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
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import cn.com.xuct.calendar.ums.api.dto.GroupInfoDto;
import cn.com.xuct.calendar.ums.api.dto.GroupMemberInfoDto;
import cn.com.xuct.calendar.ums.api.entity.MemberGroup;
import cn.com.xuct.calendar.ums.api.vo.GroupMemberPinYinVo;
import cn.com.xuct.calendar.ums.boot.service.IGroupService;
import cn.com.xuct.calendar.ums.boot.service.IMemberGroupService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.LinkedHashMap;
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


    @PostMapping("/apply")
    @ApiOperation(value = "申请入群")
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
        return R.status(true);
    }

    @PostMapping("/apply/agree")
    @ApiOperation(value = "同意入群")
    public R<String> applyAgreeJoinGroup(@RequestBody @Validated GroupApplyParam groupApplyParam) {
        memberGroupService.applyAgreeJoinGroup(groupApplyParam.getGroupId(), groupApplyParam.getMemberId());
        return R.status(true);
    }

    @PostMapping("/apply/refuse")
    @ApiOperation(value = "拒绝入群")
    public R<String> applyRefuseJoinGroup(@RequestBody @Validated GroupApplyParam groupApplyParam) {
        memberGroupService.applyRefuseJoinGroup(groupApplyParam.getGroupId(), groupApplyParam.getMemberId());
        return R.status(true);
    }
}