/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: GroupController
 * Author:   Derek Xu
 * Date:     2022/2/7 16:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.app;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.params.GroupAddParam;
import cn.com.xuct.calendar.common.module.params.GroupDeleteParam;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import cn.com.xuct.calendar.common.web.utils.SpringContextHolder;
import cn.com.xuct.calendar.ums.api.dto.GroupInfoDto;
import cn.com.xuct.calendar.ums.api.entity.Group;
import cn.com.xuct.calendar.ums.boot.service.IGroupService;
import cn.com.xuct.calendar.ums.boot.event.GroupDeleteEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/7
 * @since 1.0.0
 */
@Slf4j
@Api(tags = "【移动端】群组接口")
@RestController
@RequestMapping("/api/app/v1/group")
@RequiredArgsConstructor
public class GroupAppController {

    private final IGroupService groupService;

    @GetMapping("")
    @ApiOperation(value = "获取用户所在群组")
    public R<List<GroupInfoDto>> list() {
        return R.data(groupService.findGroupCountByMember(JwtUtils.getUserId()));
    }


    @GetMapping("/get")
    @ApiOperation(value = "查询群组信息")
    public R<GroupInfoDto> get(@RequestParam("id") Long id) {
        return R.data(groupService.getGroupCountByGroupId(id));
    }

    @GetMapping("/search")
    @ApiOperation(value = "搜索群组")
    public R<List<GroupInfoDto>> search(@RequestParam("word") String word) {
        return R.data(groupService.findGroupBySearch(word));
    }

    @GetMapping("/mine/apply")
    @ApiOperation(value = "我申请的群组")
    public R<List<GroupInfoDto>> mineApplyGroup() {
        return R.data(groupService.mineApplyGroup(JwtUtils.getUserId()));
    }

    @GetMapping("/apply/mine")
    @ApiOperation(value = "申请我的群组")
    public R<List<GroupInfoDto>> applyMineGroup() {
        return R.data(groupService.applyMineGroup(JwtUtils.getUserId()));
    }


    @PostMapping
    @ApiOperation(value = "添加/修改群组")
    public R<String> addGroup(@RequestBody @Validated GroupAddParam addParam) {
        if (addParam.getId() != null) {
            Group group = groupService.getById(addParam.getId());
            if (group == null) return R.fail("群组不存在");
            group.setName(addParam.getName());
            if (StringUtils.hasLength(addParam.getImageUrl())) {
                group.setImages(addParam.getImageUrl());
            }
            groupService.updateById(group);
            return R.status(true);
        }
        groupService.addGroup(JwtUtils.getUserId(), addParam.getName(), addParam.getImageUrl());
        return R.status(true);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "解散群组")
    public R<String> deleteGroup(@RequestBody @Validated GroupDeleteParam param) {
        Group group = groupService.getById(param.getId());
        if (group == null || !String.valueOf(group.getMemberId()).equals(String.valueOf(JwtUtils.getUserId())))
            return R.fail("群组不存在或权限不够");
        List<Long> memberIds = groupService.deleteGroup(param.getId());
        if (CollectionUtils.isEmpty(memberIds)) return R.status(true);
        /* 发出结算群组消息 */
        SpringContextHolder.publishEvent(new GroupDeleteEvent(this, group.getName(), group.getId(), JwtUtils.getUserId(), memberIds));
        return R.status(true);
    }
}