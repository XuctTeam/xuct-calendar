/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberFeignController
 * Author:   Administrator
 * Date:     2021/11/27 22:03
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.feign;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.com.xuct.calendar.common.core.constant.DictConstants;
import cn.com.xuct.calendar.common.module.dto.MemberInfoDto;
import cn.com.xuct.calendar.common.module.dto.WechatCodeDto;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.dto.CalendarInitDto;
import cn.com.xuct.calendar.common.module.enums.IdentityTypeEnum;
import cn.com.xuct.calendar.ums.api.entity.Member;
import cn.com.xuct.calendar.ums.api.entity.MemberAuth;
import cn.com.xuct.calendar.ums.boot.service.IMemberAuthService;
import cn.com.xuct.calendar.ums.boot.service.IMemberService;
import cn.com.xuct.calendar.ums.api.feign.CalendarFeignClient;
import cn.com.xuct.calendar.ums.boot.config.DictCacheManager;
import cn.com.xuct.calendar.ums.boot.config.WxMaConfiguration;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/27
 * @since 1.0.0
 */
@RestController
@Api(tags = "【远程调用】会员接口")
@RequestMapping("/api/feign/v1/member")
@RequiredArgsConstructor
public class MemberFeignController {

    private final WxMaConfiguration wxMaConfiguration;

    private final IMemberService memberService;

    private final IMemberAuthService memberAuthService;

    private final CalendarFeignClient calendarFeignClient;


    @ApiOperation(value = "通过手机号查询会员")
    @GetMapping("/get/phone")
    public R<MemberInfoDto> getUserByPhone(@RequestParam("phone") String phone) {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", phone), Column.of("identity_type", IdentityTypeEnum.phone)));
        if (memberAuth == null) return R.fail("用户不存在");
        Member member = memberService.getById(memberAuth.getMemberId());
        return R.data(MemberInfoDto.builder().userId(member.getId()).username(memberAuth.getUsername()).password(memberAuth.getPassword()).status(member.getStatus()).build());
    }


    @ApiOperation(value = "通过微信code查询会员")
    @PostMapping("/get/code")
    public R<MemberInfoDto> getUserByWechatCode(@RequestBody WechatCodeDto wechatCodeDto) throws WxErrorException {
        final WxMaService wxService = wxMaConfiguration.getMaService();
        WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(wechatCodeDto.getCode());
        if (session == null) return R.fail("获取用户失败");
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", session.getOpenid()), Column.of("identity_type", IdentityTypeEnum.open_id)));
        WxMaUserInfo wxMaUserInfo = wxService.getUserService().getUserInfo(session.getSessionKey(), wechatCodeDto.getEncryptedData(), wechatCodeDto.getIv());
        if (memberAuth != null) {
            memberAuth.setNickName(wxMaUserInfo.getNickName());
            memberAuth.setAvatar(wxMaUserInfo.getAvatarUrl());
            memberAuth.setSessionKey(session.getSessionKey());
            memberAuthService.updateById(memberAuth);
            Member member = memberService.getById(memberAuth.getMemberId());
            return R.data(MemberInfoDto.builder().userId(member.getId()).username(memberAuth.getUsername()).status(member.getStatus()).build());
        }
        Member member = memberService.saveMemberByOpenId(session.getOpenid(), wxMaUserInfo.getNickName(), wxMaUserInfo.getAvatarUrl(),
                session.getSessionKey(), DictCacheManager.getDictByCode(DictConstants.TIME_ZONE_TYPE, DictConstants.EAST_8_CODE).getValue());
        CalendarInitDto calendarInitDto = new CalendarInitDto();
        calendarInitDto.setMemberId(member.getId());
        calendarInitDto.setMemberNickName(member.getName());
        calendarFeignClient.addCarendar(calendarInitDto);
        return R.data(MemberInfoDto.builder().userId(member.getId()).username(session.getOpenid()).status(member.getStatus()).build());
    }

    @ApiOperation(value = "通过微信openId查询会员")
    @GetMapping("/get/openId")
    public R<MemberInfoDto> getUserByOpenId(@RequestParam("openId") String openId) {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", openId), Column.of("identity_type", IdentityTypeEnum.open_id)));
        if (memberAuth == null) return R.fail("用户不存在");
        Member member = memberService.getById(memberAuth.getMemberId());
        return R.data(MemberInfoDto.builder().userId(member.getId()).username(memberAuth.getUsername()).password(memberAuth.getPassword()).status(member.getStatus()).build());
    }

    @ApiOperation(value = "通过登录用户名或手机号查询会员")
    @GetMapping("/get/username")
    public R<MemberInfoDto> getUserByUserName(@RequestParam("username") String username) {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", username), Column.of("identity_type", IdentityTypeEnum.user_name)));
        if (memberAuth == null) {
            memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", username), Column.of("identity_type", IdentityTypeEnum.phone)));
        }
        if (memberAuth == null) return R.fail("用户不存在");
        Member member = memberService.getById(memberAuth.getMemberId());
        return R.data(MemberInfoDto.builder().userId(member.getId()).username(memberAuth.getUsername()).password(memberAuth.getPassword()).status(member.getStatus()).build());
    }
}