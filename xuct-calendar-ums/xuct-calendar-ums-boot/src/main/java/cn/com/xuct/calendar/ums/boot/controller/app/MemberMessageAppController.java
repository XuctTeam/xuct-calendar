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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("")
    @ApiOperation(value = "分页查询消息")
    public R<MemberMessagePageVo> list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        MemberMessagePageVo memberMessagePageVo = new MemberMessagePageVo();
        memberMessagePageVo.setFinished(false);
        List<MemberMessage> memberMessageList = memberMessageService.pages(JwtUtils.getUserId(), page, limit + 1);
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
}