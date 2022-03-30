/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: WxUserFeignClient
 * Author:   Derek Xu
 * Date:     2021/11/15 11:49
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.api.feign;

import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.module.feign.AlarmNotifyFeignInfoReq;
import cn.com.xuct.calendar.common.module.feign.MemberFeignInfoRes;
import cn.com.xuct.calendar.common.web.web.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/15
 * @since 1.0.0
 */

@FeignClient(name = "dav-ums", contextId = "members", configuration = FeignConfiguration.class)
public interface UmsFeignClient {

    /**
     * 通过id查询会员信息
     *
     * @param id
     * @return
     */
    @GetMapping("/api/feign/v1/member/get/id")
    R<MemberFeignInfoRes> getMemberById(@RequestParam("id") Long id);

    /**
     * 通过ids数组查询会员信息
     *
     * @param ids
     * @return
     */
    @PostMapping("/api/feign/v1/member/list/ids")
    R<List<MemberFeignInfoRes>> listMemberByIds(@RequestBody List<Long> ids);


    /**
     * 通过ids发送提醒信息
     *
     * @param alarmNotifyFeignInfoReq
     * @return
     */
    @PostMapping("/api/feign/v1/alarm")
    R<String> notifyAlarm(@RequestBody AlarmNotifyFeignInfoReq alarmNotifyFeignInfoReq);
}