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

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.com.xuct.calendar.common.core.constant.DictConstants;
import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.common.core.exception.SvrException;
import cn.com.xuct.calendar.common.core.res.AuthResCode;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.res.RetOps;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.enums.IdentityTypeEnum;
import cn.com.xuct.calendar.common.module.feign.OpenIdInfo;
import cn.com.xuct.calendar.common.module.feign.PersonInfo;
import cn.com.xuct.calendar.common.module.feign.UserInfo;
import cn.com.xuct.calendar.common.module.feign.req.CalendarInitFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxUserInfoFeignInfo;
import cn.com.xuct.calendar.common.security.annotation.Inner;
import cn.com.xuct.calendar.common.core.utils.SpringContextHolder;
import cn.com.xuct.calendar.ums.api.entity.Member;
import cn.com.xuct.calendar.ums.api.entity.MemberAuth;
import cn.com.xuct.calendar.ums.api.feign.BasicServicesFeignClient;
import cn.com.xuct.calendar.ums.api.feign.CalendarFeignClient;
import cn.com.xuct.calendar.ums.boot.config.DictCacheManager;
import cn.com.xuct.calendar.ums.boot.event.MemberEvent;
import cn.com.xuct.calendar.ums.boot.service.IMemberAuthService;
import cn.com.xuct.calendar.ums.boot.service.IMemberService;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/27
 * @since 1.0.0
 */
@Slf4j
@RestController
@Tag(name = "【远程调用】会员接口")
@RequestMapping("/api/feign/v1/member")
@RequiredArgsConstructor
public class MemberFeignController {

    private final IMemberService memberService;

    private final IMemberAuthService memberAuthService;

    private final CalendarFeignClient calendarFeignClient;

    private final BasicServicesFeignClient basicServicesFeignClient;


    @Inner
    @Operation(summary = "通过微信CODE获取OPENID")
    @GetMapping("/wx/code")
    public R<OpenIdInfo> wxCode(@RequestParam("code") String code) {
        R<WxMaJscode2SessionResult> jscode2SessionResultR = basicServicesFeignClient.getSessionInfo(code);
        if (jscode2SessionResultR == null || !jscode2SessionResultR.isSuccess()) return R.fail("获取微信OPENID失败");
        WxMaJscode2SessionResult result = jscode2SessionResultR.getData();
        return R.data(OpenIdInfo.builder().openId(result.getOpenid()).sessionKey(result.getSessionKey()).build());
    }

    @Inner
    @Operation(summary = "通过OPENID查询")
    @PostMapping("/get/openId")
    public R<UserInfo> getUserByWechatCode(@RequestBody WxUserInfoFeignInfo wxUserInfoFeignInfo) {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", wxUserInfoFeignInfo.getOpenId()), Column.of("identity_type", IdentityTypeEnum.open_id)));
        if (!wxUserInfoFeignInfo.isLogin()) {
            if (memberAuth == null)
                return R.fail("用户未找到");
            Member member = memberService.getById(memberAuth.getMemberId());
            return R.data(UserInfo.builder().personInfo(PersonInfo.builder().userId(member.getId()).username(memberAuth.getUsername()).name(member.getName())
                    .status(member.getStatus()).timeZone(member.getTimeZone()).build()).build());
        }
        R<WxMaUserInfo> wxMaUserInfoR = basicServicesFeignClient.getUserInfo(WxUserInfoFeignInfo.builder().sessionKey(wxUserInfoFeignInfo.getSessionKey()).encryptedData(wxUserInfoFeignInfo.getEncryptedData()).iv(wxUserInfoFeignInfo.getIv()).build());
        WxMaUserInfo wxMaUserInfo = RetOps.of(wxMaUserInfoR).getData().orElseThrow(() -> new SvrException(AuthResCode.ACCESS_UNAUTHORIZED));
        /* 2. 如果存在更新基础信息直接返回 */
        if (memberAuth != null) {
            memberAuth.setNickName(wxMaUserInfo.getNickName());
            memberAuth.setAvatar(wxMaUserInfo.getAvatarUrl());
            memberAuth.setSessionKey(wxUserInfoFeignInfo.getSessionKey());
            memberAuthService.updateById(memberAuth);
            Member member = memberService.getById(memberAuth.getMemberId());
            return R.data(UserInfo.builder().personInfo(PersonInfo.builder().userId(member.getId()).username(memberAuth.getUsername()).name(member.getName())
                    .status(member.getStatus()).timeZone(member.getTimeZone()).build()).build());
        }
        /* 3. 不存在则自动注册一个 */
        Member member = memberService.saveMemberByOpenId(wxUserInfoFeignInfo.getOpenId(), wxMaUserInfo.getNickName(), wxMaUserInfo.getAvatarUrl(), wxUserInfoFeignInfo.getSessionKey(), DictCacheManager.getDictByCode(DictConstants.TIME_ZONE_TYPE, DictConstants.EAST_8_CODE).getValue());
        CalendarInitFeignInfo calendarInitFeignInfo = new CalendarInitFeignInfo();
        calendarInitFeignInfo.setMemberId(member.getId());
        calendarInitFeignInfo.setMemberNickName(member.getName());
        calendarFeignClient.addCalendar(calendarInitFeignInfo, SecurityConstants.FROM_IN);
        /* 添加注册消息到用户 */
        SpringContextHolder.publishEvent(new MemberEvent(this, member.getId(), member.getName(), 0));
        return R.data(UserInfo.builder().personInfo(PersonInfo.builder().userId(member.getId()).username(wxUserInfoFeignInfo.getOpenId()).name(member.getName()).status(member.getStatus()).timeZone(member.getTimeZone()).build()).build());
    }


    @Inner
    @Operation(summary = "通过用户名/手机号/邮箱查询")
    @GetMapping("/get/username")
    public R<UserInfo> getUserByUserName(@RequestParam("username") String username) {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", username), Column.in("identity_type", Lists.newArrayList(IdentityTypeEnum.user_name, IdentityTypeEnum.phone, IdentityTypeEnum.email))));
        if (memberAuth == null) return R.fail("登录方式不存在");
        Member member = memberService.getById(memberAuth.getMemberId());
        if (member == null) return R.fail("用户不存在");
        return R.data(UserInfo.builder()
                .personInfo(PersonInfo.builder().userId(member.getId()).username(memberAuth.getUsername())
                        .password(memberAuth.getPassword()).name(member.getName()).timeZone(member.getTimeZone()).status(member.getStatus()).build()).build());
    }

    @Inner
    @Operation(summary = "通过ID查询")
    @GetMapping("/get/id")
    public R<PersonInfo> getUserById(@RequestParam("id") Long id) {
        Member member = memberService.findMemberById(id);
        if (member == null) return R.fail("用户不存在");
        return R.data(PersonInfo.builder().userId(member.getId()).name(member.getName()).status(member.getStatus()).timeZone(member.getTimeZone()).build());
    }

    @Operation(summary = "通过IDS查询")
    @PostMapping("/list/ids")
    public R<List<PersonInfo>> listMemberByIds(@RequestBody List<String> ids) {
        List<Member> members = memberService.find(Column.in("id", ids));
        if (CollectionUtils.isEmpty(members)) return R.data(Lists.newArrayList());
        return R.data(members.stream().map(member -> {
            PersonInfo memberFeignInfo = new PersonInfo();
            memberFeignInfo.setName(member.getName());
            memberFeignInfo.setUserId(member.getId());
            memberFeignInfo.setAvatar(member.getAvatar());
            return memberFeignInfo;
        }).collect(Collectors.toList()));
    }
}