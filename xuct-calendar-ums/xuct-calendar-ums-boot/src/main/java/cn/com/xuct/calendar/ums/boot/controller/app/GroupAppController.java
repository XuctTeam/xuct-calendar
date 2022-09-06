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
import cn.com.xuct.calendar.common.module.enums.CommonPowerEnum;
import cn.com.xuct.calendar.common.module.params.GroupAddParam;
import cn.com.xuct.calendar.common.module.params.GroupDeleteParam;
import cn.com.xuct.calendar.common.security.utils.SecurityUtils;
import cn.com.xuct.calendar.common.web.utils.SpringContextHolder;
import cn.com.xuct.calendar.ums.api.dto.GroupInfoDto;
import cn.com.xuct.calendar.ums.api.dto.GroupMemberInfoDto;
import cn.com.xuct.calendar.ums.api.dto.GroupSearchPageDto;
import cn.com.xuct.calendar.ums.api.entity.Group;
import cn.com.xuct.calendar.ums.boot.event.GroupDeleteEvent;
import cn.com.xuct.calendar.ums.boot.service.IGroupService;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.math.MathUtil;
import cn.hutool.core.util.RandomUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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

    @ApiOperation(value = "获取用户所在群组")
    @GetMapping("")
    public R<List<GroupInfoDto>> list() {
        return R.data(groupService.findGroupCountByMember(SecurityUtils.getUserId()));
    }

    @ApiOperation(value = "查询群组信息")
    @GetMapping("/get")
    public R<GroupInfoDto> get(@RequestParam("id") Long id) {
        GroupInfoDto groupInfoDto = groupService.getGroupCountByGroupId(id);
        Assert.notNull(groupInfoDto, "查询结果为空");
        if (!String.valueOf(SecurityUtils.getUserId()).equals(String.valueOf(groupInfoDto.getCreateMemberId())))
            groupInfoDto.setPassword(null);
        return R.data(groupInfoDto);
    }

    @ApiOperation(value = "搜索群组")
    @GetMapping("/search")
    public R<GroupSearchPageDto> search(@RequestParam("word") String word, @RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                        @RequestParam(value = "hasPass", required = false) String hasPass, @RequestParam(value = "dateScope", required = false) String dateScope,
                                        @RequestParam(value = "numCount", required = false) String numCount) {
        GroupSearchPageDto groupSearchPageDto = new GroupSearchPageDto();
        groupSearchPageDto.setFinished(true);
        word = StringUtils.hasLength(word) ? word : null;
        hasPass = StringUtils.hasLength(hasPass) ? hasPass : null;
        dateScope = StringUtils.hasLength(dateScope) ? dateScope : null;
        numCount = StringUtils.hasLength(numCount) ? numCount : null;

        List<GroupInfoDto> groupInfoDtos = groupService.findGroupBySearchByPage(SecurityUtils.getUserId(), word, page, limit + 1, hasPass, dateScope, numCount);
        if (groupInfoDtos.size() == limit + 1) {
            groupInfoDtos.remove(groupInfoDtos.size() - 1);
            groupSearchPageDto.setFinished(false);
        }
        groupSearchPageDto.setList(groupInfoDtos);
        return R.data(groupSearchPageDto);
    }

    @ApiOperation(value = "我申请的群组")
    @GetMapping("/mine/apply")
    public R<List<GroupMemberInfoDto>> mineApplyGroup() {
        return R.data(groupService.mineApplyGroup(SecurityUtils.getUserId()));
    }

    @ApiOperation(value = "申请我的群组")
    @GetMapping("/apply/mine")
    public R<List<GroupMemberInfoDto>> applyMineGroup() {
        return R.data(groupService.applyMineGroup(SecurityUtils.getUserId()));
    }

    @ApiOperation(value = "添加/修改群组")
    @PostMapping
    public R<String> addGroup(@RequestBody @Validated GroupAddParam addParam) {
        if (addParam.getId() != null) {
            Group group = groupService.getById(addParam.getId());
            if (group == null) return R.fail("群组不存在");
            if (!String.valueOf(group.getMemberId()).equals(String.valueOf(SecurityUtils.getUserId())))
                return R.fail("非群组管理员");
            group.setName(addParam.getName());
            if (StringUtils.hasLength(addParam.getImageUrl()) && !addParam.getImageUrl().equals(group.getImages())) {
                group.setImages(addParam.getImageUrl());
            } else if (StringUtils.hasLength(group.getImages()) && !StringUtils.hasLength(addParam.getImageUrl())) {
                group.setImages(null);
            }
            if (StringUtils.hasLength(addParam.getPassword()))
                group.setPassword(addParam.getPassword());
            group.setPower(CommonPowerEnum.valueOf(addParam.getPower()));
            group.setNum(addParam.getNum());
            if (!StringUtils.hasLength(group.getNo())) {
                group.setNo(RandomUtil.randomNumbers(8));
            }
            groupService.updateById(group);
            return R.status(true);
        }
        groupService.addGroup(SecurityUtils.getUserId(), addParam.getName(), addParam.getPassword(), addParam.getImageUrl(), addParam.getPower(), addParam.getNum(), RandomUtil.randomNumbers(8));
        return R.status(true);
    }

    @ApiOperation(value = "解散群组")
    @PostMapping("/delete")
    public R<String> deleteGroup(@RequestBody @Validated GroupDeleteParam param) {
        Group group = groupService.getById(param.getId());
        if (group == null)
            return R.fail("群组不存在");
        if (!String.valueOf(group.getMemberId()).equals(String.valueOf(SecurityUtils.getUserId())))
            return R.fail("非群组管理员");
        List<Long> memberIds = groupService.deleteGroup(param.getId());
        if (CollectionUtils.isEmpty(memberIds)) return R.status(true);
        /* 发出结算群组消息 */
        SpringContextHolder.publishEvent(new GroupDeleteEvent(this, group.getName(), group.getId(), SecurityUtils.getUserId(), memberIds));
        return R.status(true);
    }
}