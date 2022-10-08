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
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.params.MessageDeleteBatchParam;
import cn.com.xuct.calendar.common.module.params.MessageReadParam;
import cn.com.xuct.calendar.common.security.utils.SecurityUtils;
import cn.com.xuct.calendar.ums.api.entity.MemberMessage;
import cn.com.xuct.calendar.ums.api.vo.MemberMessagePageVo;
import cn.com.xuct.calendar.ums.boot.service.IMemberMessageService;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
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
@Tag(name = "【移动端】消息接口")
@RequestMapping("/api/app/v1/message")
@RequiredArgsConstructor
public class MemberMessageAppController {

    private final IMemberMessageService memberMessageService;

    @Operation(summary = "分页查询消息")
    @GetMapping("/list")
    public R<MemberMessagePageVo> list(@RequestParam(value = "title", required = false) String title, @RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                                       @RequestParam(value = "status", required = false) Integer status) {
        MemberMessagePageVo memberMessagePageVo = new MemberMessagePageVo();
        memberMessagePageVo.setFinished(false);
        List<MemberMessage> memberMessageList = memberMessageService.pages(title, SecurityUtils.getUserId(), page, limit + 1, status);
        if (CollectionUtils.isEmpty(memberMessageList)) {
            memberMessagePageVo.setFinished(true);
            memberMessagePageVo.setMessages(Lists.newArrayList());
            return R.data(memberMessagePageVo);
        }
        if (memberMessageList.size() >= limit + 1) {
            memberMessageList.remove(memberMessageList.size() - 1);
            memberMessagePageVo.setMessages(memberMessageList);
            return R.data(memberMessagePageVo);
        }
        if (memberMessageList.size() < limit) {
            memberMessagePageVo.setFinished(true);
        }
        memberMessagePageVo.setMessages(memberMessageList);
        return R.data(memberMessagePageVo);
    }

    @GetMapping("/count")
    public R<Long> count() {
        return R.data(memberMessageService.count(Lists.newArrayList(Column.of("member_id", SecurityUtils.getUserId()), Column.of("status", 0))));
    }

    @GetMapping("")
    @Operation(summary = "获取消息")
    public R<MemberMessage> get(@RequestParam("id") Long id) {
        return R.data(memberMessageService.getById(id));
    }

    @Operation(summary = "清除未读")
    @PostMapping("/clear")
    public R<String> clearUnread() {
        MemberMessage memberMessage = new MemberMessage();
        memberMessage.setCreateTime(null);
        memberMessage.setUpdateTime(null);
        memberMessage.setStatus(1);
        memberMessageService.update(memberMessage, Lists.newArrayList(Column.of("member_id", SecurityUtils.getUserId()), Column.of("status", 0)));
        return R.status(true);
    }

    @Operation(summary = "已读消息")
    @PostMapping("")
    public R<String> read(@Validated @RequestBody MessageReadParam param) {
        MemberMessage memberMessage = new MemberMessage();
        memberMessage.setId(Long.valueOf(param.getId()));
        memberMessage.setStatus(1);
        memberMessageService.updateById(memberMessage);
        return R.status(true);
    }

    @Operation(summary = "删除消息")
    @DeleteMapping("/{id}")
    public R<String> delete(@PathVariable("id") Long id) {
        memberMessageService.removeById(id);
        return R.status(true);
    }

    @Operation(summary = "批量删除消息")
    @DeleteMapping("/batch")
    public R<String> batchDelete(@RequestBody MessageDeleteBatchParam param) {
        Assert.notEmpty(param.getIds(), "批量删除ID不能为空");
        memberMessageService.removeBatchByIds(param.getIds());
        return R.status(true);
    }

    @Operation(summary = "批量已读消息")
    @PostMapping("/batch")
    public R<String> batchRead(@RequestBody MessageDeleteBatchParam param) {
        Assert.notEmpty(param.getIds(), "批量已经ID不能为空");
        List<MemberMessage> memberMessages = memberMessageService.find(
                Lists.newArrayList(Column.in("id", param.getIds()),
                        Column.of("status", 0),
                        Column.of("member_id", SecurityUtils.getUserId()))
        );
        if (CollectionUtils.isEmpty(memberMessages)) return R.fail("全部已读");
        memberMessages.stream().forEach(x -> x.setStatus(1));
        memberMessageService.updateBatchById(memberMessages);
        return R.status(true);
    }
}