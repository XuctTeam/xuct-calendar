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
import cn.com.xuct.calendar.common.smms.client.SmmsClient;
import cn.com.xuct.calendar.common.smms.vo.SmmsUploadRes;
import cn.com.xuct.calendar.common.smms.vo.data.SmmsUploadData;
import cn.com.xuct.calendar.common.web.utils.FileUtils;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import cn.com.xuct.calendar.ums.api.dto.GroupCountDto;
import cn.com.xuct.calendar.ums.boot.service.IGroupService;
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
public class GroupController {

    private final IGroupService groupService;

    private final SmmsClient smmsClient;

    @GetMapping("")
    @ApiOperation(value = "获取用户群组")
    public R<List<GroupCountDto>> list() {
        return R.data(groupService.findGroupCountByMember(JwtUtils.getUserId()));
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

    /**
     * 功能描述: <br>
     * 〈添加群组〉
     *
     * @param addParam
     * @return:cn.com.xuct.calendar.common.core.res.R<java.lang.String>
     * @since: 1.0.0
     * @Author:
     * @Date: 2022/2/13 10:28
     */
    @PostMapping
    @ApiOperation(value = "添加群组")
    public R<String> addGroup(@RequestBody @Validated GroupAddParam addParam) {
        groupService.addGroup(JwtUtils.getUserId(), addParam.getName(), addParam.getImageUrl());
        return R.status(true);
    }
}