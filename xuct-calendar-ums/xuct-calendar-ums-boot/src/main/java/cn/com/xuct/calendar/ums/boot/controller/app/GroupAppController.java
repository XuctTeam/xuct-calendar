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
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.enums.GroupMemberStatusEnum;
import cn.com.xuct.calendar.common.module.params.GroupAddParam;
import cn.com.xuct.calendar.common.module.params.GroupApplyParam;
import cn.com.xuct.calendar.common.module.params.GroupJoinParam;
import cn.com.xuct.calendar.common.smms.client.SmmsClient;
import cn.com.xuct.calendar.common.smms.vo.SmmsUploadRes;
import cn.com.xuct.calendar.common.smms.vo.data.SmmsUploadData;
import cn.com.xuct.calendar.common.web.utils.FileUtils;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import cn.com.xuct.calendar.ums.api.dto.GroupInfoDto;
import cn.com.xuct.calendar.ums.api.entity.MemberGroup;
import cn.com.xuct.calendar.ums.boot.service.IGroupService;
import cn.com.xuct.calendar.ums.boot.service.IMemberGroupService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    private final SmmsClient smmsClient;

    @GetMapping("")
    @ApiOperation(value = "获取用户所在群组")
    public R<List<GroupInfoDto>> list() {
        return R.data(groupService.findGroupCountByMember(JwtUtils.getUserId()));
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

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @ApiOperation(value = "上传图片")
    public R<SmmsUploadData> uploadImage(@RequestParam MultipartFile smfile) {
        SmmsUploadRes smmsRes = smmsClient.upload(FileUtils.multipartFileToFile(smfile, "/temp", FileUtils.contentTypeToFileSuffix(smfile.getContentType())));
        if (smmsRes == null || (!smmsRes.isSuccess() && !"image_repeated".equals(smmsRes.getCode())))
            return R.fail("上传失败");
        if (!smmsRes.isSuccess() && "image_repeated".equals(smmsRes.getCode())) {
            SmmsUploadData smmsUploadData = new SmmsUploadData();
            smmsUploadData.setUrl(smmsRes.getImages());
            return R.data(smmsUploadData);
        }
        return R.data(smmsRes.getData());
    }

    @PostMapping
    @ApiOperation(value = "添加群组")
    public R<String> addGroup(@RequestBody @Validated GroupAddParam addParam) {
        groupService.addGroup(JwtUtils.getUserId(), addParam.getName(), addParam.getImageUrl());
        return R.status(true);
    }


}