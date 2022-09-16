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
import cn.com.xuct.calendar.common.core.enums.PasswordEncoderTypeEnum;
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
import cn.com.xuct.calendar.common.module.feign.req.MemberModifyPasswordFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.MemberRegisterFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxUserInfoFeignInfo;
import cn.com.xuct.calendar.common.security.annotation.Inner;
import cn.com.xuct.calendar.common.web.utils.SpringContextHolder;
import cn.com.xuct.calendar.ums.api.entity.Member;
import cn.com.xuct.calendar.ums.api.entity.MemberAuth;
import cn.com.xuct.calendar.ums.api.feign.BasicServicesFeignClient;
import cn.com.xuct.calendar.ums.api.feign.CalendarFeignClient;
import cn.com.xuct.calendar.ums.boot.config.DictCacheManager;
import cn.com.xuct.calendar.ums.boot.event.MemberEvent;
import cn.com.xuct.calendar.ums.boot.service.IMemberAuthService;
import cn.com.xuct.calendar.ums.boot.service.IMemberService;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@Api(tags = "【远程调用】会员接口")
@RequestMapping("/api/feign/v1/member")
@RequiredArgsConstructor
public class MemberFeignController {

    private final IMemberService memberService;

    private final IMemberAuthService memberAuthService;

    private final CalendarFeignClient calendarFeignClient;

    private final PasswordEncoder passwordEncoder;

    private final BasicServicesFeignClient basicServicesFeignClient;


    @Inner
    @ApiOperation(value = "通过微信CODE获取OPENID")
    @GetMapping("/wx/code")
    public R<OpenIdInfo> wxCode(@RequestParam("code") String code) {
        R<WxMaJscode2SessionResult> jscode2SessionResultR = basicServicesFeignClient.getSessionInfo(code);
        if (jscode2SessionResultR == null || !jscode2SessionResultR.isSuccess()) return R.fail("获取微信OPENID失败");
        WxMaJscode2SessionResult result = jscode2SessionResultR.getData();
        return R.data(OpenIdInfo.builder().openId(result.getOpenid()).sessionKey(result.getSessionKey()).build());
    }

    @Inner
    @ApiOperation(value = "通过OPENID查询会员")
    @PostMapping("/get/openId")
    public R<PersonInfo> getUserByWechatCode(@RequestBody WxUserInfoFeignInfo wxUserInfoFeignInfo) {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", wxUserInfoFeignInfo.getOpenId()), Column.of("identity_type", IdentityTypeEnum.open_id)));
        if (!wxUserInfoFeignInfo.isLogin()) {
            if (memberAuth == null)
                return R.fail("用户未找到");
            Member member = memberService.getById(memberAuth.getMemberId());
            return R.data(PersonInfo.builder().userId(member.getId()).username(memberAuth.getUsername()).status(member.getStatus()).build());
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
            return R.data(PersonInfo.builder().userId(member.getId()).username(memberAuth.getUsername()).status(member.getStatus()).build());
        }
        /* 3. 不存在则自动注册一个 */
        Member member = memberService.saveMemberByOpenId(wxUserInfoFeignInfo.getOpenId(), wxMaUserInfo.getNickName(), wxMaUserInfo.getAvatarUrl(), wxUserInfoFeignInfo.getSessionKey(), DictCacheManager.getDictByCode(DictConstants.TIME_ZONE_TYPE, DictConstants.EAST_8_CODE).getValue());
        CalendarInitFeignInfo calendarInitFeignInfo = new CalendarInitFeignInfo();
        calendarInitFeignInfo.setMemberId(member.getId());
        calendarInitFeignInfo.setMemberNickName(member.getName());
        calendarFeignClient.addCalendar(calendarInitFeignInfo);
        /* 添加注册消息到用户 */
        SpringContextHolder.publishEvent(new MemberEvent(this, member.getId(), member.getName(), 0));
        return R.data(PersonInfo.builder().userId(member.getId()).username(wxUserInfoFeignInfo.getOpenId()).status(member.getStatus()).timeZone(member.getTimeZone()).build());
    }


    @Inner
    @ApiOperation(value = "通过登录用户名或邮箱查询会员")
    @GetMapping("/get/username")
    public R<UserInfo> getUserByUserName(@RequestParam("username") String username) {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", username), Column.of("identity_type", IdentityTypeEnum.user_name)));
        if (memberAuth == null) {
            memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", username), Column.of("identity_type", IdentityTypeEnum.phone)));
        }
        if (memberAuth == null) {
            memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", username), Column.of("identity_type", IdentityTypeEnum.email)));
        }
        if (memberAuth == null) return R.fail("登录方式不存在");
        Member member = memberService.getById(memberAuth.getMemberId());
        if (member == null) return R.fail("用户不存在");
        return R.data(UserInfo.builder()
                .personInfo(PersonInfo.builder().userId(member.getId()).username(memberAuth.getUsername())
                        .password(memberAuth.getPassword()).name(member.getName()).timeZone(member.getTimeZone()).status(member.getStatus()).build()).build());
    }

    @ApiOperation(value = "通过ID查询会员")
    @GetMapping("/get/id")
    public R<PersonInfo> getUserById(@RequestParam("id") Long id) {
        Member member = memberService.findMemberById(id);
        if (member == null) return R.fail("用户不存在");
        return R.data(PersonInfo.builder().userId(member.getId()).name(member.getName()).status(member.getStatus()).timeZone(member.getTimeZone()).build());
    }

    @ApiOperation(value = "通过IDS查询会员")
    @PostMapping("/list/ids")
    public R<List<PersonInfo>> listMemberByIds(@RequestBody List<String> ids) {
        List<Member> members = memberService.find(Column.in("id", ids));
        if (CollectionUtils.isEmpty(members)) return R.data(Lists.newArrayList());
        return R.data(members.stream().map(member -> {
            PersonInfo memberFeignInfo = new PersonInfo();
            memberFeignInfo.setName(member.getName());
            memberFeignInfo.setAvatar(member.getAvatar());
            return memberFeignInfo;
        }).collect(Collectors.toList()));
    }

    @ApiOperation(value = "会员注册")
    @PostMapping("/register")
    public R<String> register(@RequestBody MemberRegisterFeignInfo registerDto) {
        List<Column> qry = Lists.newArrayList(Column.of("user_name", registerDto.getUsername()));
        switch (registerDto.getFormType()) {
            case 0:
                qry.add(Column.of("identity_type", IdentityTypeEnum.user_name));
                break;
            case 1:
                qry.add(Column.of("identity_type", IdentityTypeEnum.phone));
                break;
            case 2:
                qry.add(Column.of("identity_type", IdentityTypeEnum.email));
                break;
        }
        MemberAuth memberAuth = memberAuthService.get(qry);
        if (memberAuth != null) return R.fail("账号已存在");
        Member member = null;
        String timeZone = DictCacheManager.getDictByCode(DictConstants.TIME_ZONE_TYPE, DictConstants.EAST_8_CODE).getValue();
        String password = this.delegatingPassword(registerDto.getPassword()).replace("{bcrypt}", "");
        switch (registerDto.getFormType()) {
            case 0:
                member = memberService.saveMemberByUserName(registerDto.getUsername(), password, timeZone);
                break;
            case 1:
                member = memberService.saveMemberByPhone(registerDto.getUsername(), password, timeZone);
                break;
            case 2:
                member = memberService.saveMemberByEmail(registerDto.getUsername(), password, timeZone);
                break;
        }
        if (member == null) return R.fail("注册失败");
        /* 添加日历 */
        CalendarInitFeignInfo calendarInitFeignInfo = new CalendarInitFeignInfo();
        calendarInitFeignInfo.setMemberId(member.getId());
        calendarInitFeignInfo.setMemberNickName(member.getName());
        calendarFeignClient.addCalendar(calendarInitFeignInfo);
        /* 添加注册消息到用户 */
        SpringContextHolder.publishEvent(new MemberEvent(this, member.getId(), member.getName(), 0));
        return R.status(true);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/modify/password")
    public R<String> modifyPassword(@RequestBody MemberModifyPasswordFeignInfo memberModifyPasswordFeignInfo) {
        Member member = memberService.getById(memberModifyPasswordFeignInfo.getMemberId());
        if (member == null) return R.fail("用户不存在");
        final String password = this.delegatingPassword(memberModifyPasswordFeignInfo.getPassword());
        List<MemberAuth> memberAuths = memberAuthService.find(Column.of("member_id", member.getId()));
        memberAuths.stream().forEach(item -> item.setPassword(password));
        memberAuthService.updateBatchById(memberAuths, memberAuths.size());
        return R.status(true);
    }

    private String delegatingPassword(String password) {
        return passwordEncoder.encode(password).replace(PasswordEncoderTypeEnum.BCRYPT.getPrefix(), "");
    }
}