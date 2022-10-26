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
package cn.com.xuct.calendar.ums.boot.controller.app;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.imgurl.client.ImgUrlClient;
import cn.com.xuct.calendar.common.imgurl.vo.data.ImgUrlData;
import cn.com.xuct.calendar.common.web.utils.FilesUtils;
import cn.com.xuct.calendar.ums.api.vo.UploadImageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "【所有端】上传接口")
@RestController
@RequestMapping("/api/v1/app/file")
@RequiredArgsConstructor
public class UploadAppController {

    private final ImgUrlClient imgUrlClient;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @Operation(summary = "上传图片")
    public R<UploadImageVo> uploadImage(@RequestParam MultipartFile file) {
        ImgUrlData imgUrlData = imgUrlClient.upload(FilesUtils.multipartFileToFile(file, "/temp", FilesUtils.contentTypeToFileSuffix(file.getContentType())));
        if (imgUrlData == null) return R.fail("上传失败");
        return R.data(UploadImageVo.builder().url(imgUrlData.getUrl()).width(imgUrlData.getImageWidth()).height(imgUrlData.getImageHeight())
                .thumbnailUrl(imgUrlData.getThumbnailUrl())
                .build());
    }
}