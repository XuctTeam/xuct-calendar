/**
 * Copyright (C), 2021-2022, 楚恬商行
 * FileName: ImageController
 * Author:   Derek Xu
 * Date:     2022/5/27 16:57
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * Derek Xu           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.controller.app;

import cn.com.xuct.calendar.common.fdfs.client.FdfsClient;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Administrator
 * @create 2022/5/27
 * @since 1.0.0
 */
@Slf4j
@RestController
@Api(tags = "【移动端】图片服务")
@RequestMapping("/api/app/v1/images")
@RequiredArgsConstructor
public class ImageController {

    @GetMapping("/")
    public void test() throws IOException, MyException {
        FdfsClient fdfsClient = new FdfsClient();
        //fdfsClient.upload("group1", "M00/00/00/CgAQDGKEgb2AE6fzAAB0mLZqMmI800.png");
    }
}