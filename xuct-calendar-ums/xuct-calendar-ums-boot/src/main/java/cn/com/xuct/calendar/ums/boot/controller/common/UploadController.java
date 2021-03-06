/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: UploadController
 * Author:   Derek Xu
 * Date:     2022/3/2 8:58
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.common;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.smms.client.SmmsClient;
import cn.com.xuct.calendar.common.smms.vo.SmmsUploadRes;
import cn.com.xuct.calendar.common.smms.vo.data.SmmsUploadData;
import cn.com.xuct.calendar.common.web.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/3/2
 * @since 1.0.0
 */
@Slf4j
@Api(tags = "【所有端】上传接口")
@RestController
@RequestMapping("/api/v1/common/file")
@RequiredArgsConstructor
public class UploadController {

    private final SmmsClient smmsClient;

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
}