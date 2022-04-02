/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberMessageFeignController
 * Author:   Derek Xu
 * Date:     2022/4/1 13:05
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.feign;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.enums.MemberMessageTypeEnum;
import cn.com.xuct.calendar.common.module.feign.req.MemberMessageFeignInfo;
import cn.com.xuct.calendar.ums.api.entity.MemberMessage;
import cn.com.xuct.calendar.ums.boot.service.IMemberMessageService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/4/1
 * @since 1.0.0
 */
@RestController
@Api(tags = "【远程调用】消息接口")
@RequestMapping("/api/feign/v1/message")
@RequiredArgsConstructor
public class MemberMessageFeignController {

    private final IMemberMessageService memberMessageService;

    @PostMapping("")
    public R<String> sendMemberMessage(@Validated @RequestBody MemberMessageFeignInfo memberMessageFeignInfo) {
        MemberMessage memberMessage = null;
        List<MemberMessage> memberMessageList = Lists.newArrayList();
        List<Long> memberIds = memberMessageFeignInfo.getMemberIds();
        for (int i = 0, j = memberIds.size(); i < j; i++) {
            memberMessage = new MemberMessage();
            memberMessage.setMemberId(memberIds.get(i));
            memberMessage.setType(typeEnum(memberMessageFeignInfo.getType()));
            memberMessage.setOperation(memberMessageFeignInfo.getOperation());
            memberMessage.setContent(new JSONObject(memberMessageFeignInfo.getContent()));
            memberMessage.setStatus(0);
            memberMessageList.add(memberMessage);
        }
        if (CollectionUtils.isEmpty(memberMessageList)) return R.fail("保存失败");
        memberMessageService.saveBatch(memberMessageList);
        return R.status(true);
    }

    private MemberMessageTypeEnum typeEnum(String type) {
        switch (type) {
            case "SYSTEM":
                return MemberMessageTypeEnum.SYSTEM;
            case "GROUP":
                return MemberMessageTypeEnum.GROUP;
            case "EVENT":
                return MemberMessageTypeEnum.EVENT;
            default:
                return null;
        }
    }
}