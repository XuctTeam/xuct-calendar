/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: UuidFeginController
 * Author:   Administrator
 * Date:     2022/1/20 22:16
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.sequence.controller.feign;

import cn.com.xuct.calendar.common.core.res.R;
import com.sankuai.inf.leaf.common.Result;
import com.sankuai.inf.leaf.common.Status;
import com.sankuai.inf.leaf.service.SegmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/1/20
 * @since 1.0.0
 */
@Slf4j
@RestController
@Api(tags = "【远程调用】Sequence接口")
@RequiredArgsConstructor
@RequestMapping("/api/feign/uuid")
public class SequenceFeignController {


    private final SegmentService segmentService;


    @ApiOperation(value = "获取UUID")
    @GetMapping("")
    public R<Long> uuid(@RequestParam("key") String key) {
        Result result = segmentService.getId(key);
        if (!result.getStatus().equals(Status.SUCCESS)) R.fail("获取失败");
        return R.data(result.getId());
    }
}