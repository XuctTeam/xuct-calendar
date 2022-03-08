/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberMessageAppController
 * Author:   Derek Xu
 * Date:     2022/2/17 13:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.app;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.params.MessageReadParam;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import cn.com.xuct.calendar.ums.api.entity.MemberMessage;
import cn.com.xuct.calendar.ums.api.vo.MemberMessagePageVo;
import cn.com.xuct.calendar.ums.boot.service.IMemberMessageService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/17
 * @since 1.0.0
 */
@Slf4j
@RestController
@Api(tags = "【移动端】消息接口")
@RequestMapping("/api/app/v1/message")
@RequiredArgsConstructor
public class MemberMessageAppController {

    private final IMemberMessageService memberMessageService;

    @ApiOperation(value = "分页查询消息")
    @GetMapping("/list")
    public R<MemberMessagePageVo> list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                       @RequestParam(value = "status", required = false) Integer status,
                                       @RequestParam(value = "sort", required = false) Integer sort) {
        MemberMessagePageVo memberMessagePageVo = new MemberMessagePageVo();
        memberMessagePageVo.setFinished(false);
        List<MemberMessage> memberMessageList = memberMessageService.pages(JwtUtils.getUserId(), page, limit + 1, status, sort);
        if (CollectionUtils.isEmpty(memberMessageList)) {
            memberMessagePageVo.setFinished(true);
            memberMessagePageVo.setMessages(Lists.newArrayList());
            return R.data(memberMessagePageVo);
        }
        if (memberMessageList.size() < limit) {
            memberMessagePageVo.setFinished(true);
        }
        memberMessagePageVo.setMessages(memberMessageList);
        return R.data(memberMessagePageVo);
    }

    @GetMapping("")
    @ApiOperation(value = "获取消息")
    public R<MemberMessage> get(@RequestParam("id") Long id) {
        return R.data(memberMessageService.getById(id));
    }

    @ApiOperation(value = "已读消息")
    @PostMapping("")
    public R<String> read(@Validated @RequestBody MessageReadParam param) {
        MemberMessage memberMessage = new MemberMessage();
        memberMessage.setId(Long.valueOf(param.getId()));
        memberMessage.setStatus(1);
        memberMessageService.updateById(memberMessage);
        return R.status(true);
    }
}