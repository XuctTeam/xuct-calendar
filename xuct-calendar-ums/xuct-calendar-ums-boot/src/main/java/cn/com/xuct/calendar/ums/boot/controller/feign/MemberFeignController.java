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
import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.enums.ColumnEnum;
import cn.com.xuct.calendar.common.core.enums.PasswordEncoderTypeEnum;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.dto.*;
import cn.com.xuct.calendar.common.module.enums.IdentityTypeEnum;
import cn.com.xuct.calendar.common.module.params.MemberRegisterParam;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import cn.com.xuct.calendar.common.web.utils.SpringContextHolder;
import cn.com.xuct.calendar.ums.api.entity.Member;
import cn.com.xuct.calendar.ums.api.entity.MemberAuth;
import cn.com.xuct.calendar.ums.api.feign.CalendarFeignClient;
import cn.com.xuct.calendar.ums.boot.config.DictCacheManager;
import cn.com.xuct.calendar.ums.boot.config.WxMaConfiguration;
import cn.com.xuct.calendar.ums.boot.event.MemberRegisterEvent;
import cn.com.xuct.calendar.ums.boot.service.IMemberAuthService;
import cn.com.xuct.calendar.ums.boot.service.IMemberService;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
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
@RestController
@Api(tags = "【远程调用】会员接口")
@RequestMapping("/api/feign/v1/member")
@RequiredArgsConstructor
public class MemberFeignController {

    private final WxMaConfiguration wxMaConfiguration;

    private final IMemberService memberService;

    private final IMemberAuthService memberAuthService;

    private final CalendarFeignClient calendarFeignClient;

    private final PasswordEncoder passwordEncoder;


    @ApiOperation(value = "通过手机号查询会员")
    @GetMapping("/get/phone")
    public R<MemberInfoDto> getUserByPhone(@RequestParam("phone") String phone) {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", phone), Column.of("identity_type", IdentityTypeEnum.phone)));
        if (memberAuth == null) return R.fail("用户不存在");
        Member member = memberService.getById(memberAuth.getMemberId());
        return R.data(MemberInfoDto.builder().userId(member.getId()).username(memberAuth.getUsername()).password(memberAuth.getPassword()).status(member.getStatus()).timeZone(member.getTimeZone()).build());
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
        calendarFeignClient.addCalendar(calendarInitDto);
        return R.data(MemberInfoDto.builder().userId(member.getId()).username(session.getOpenid()).status(member.getStatus()).timeZone(member.getTimeZone()).build());
    }

    @ApiOperation(value = "通过微信openId查询会员")
    @GetMapping("/get/openId")
    public R<MemberInfoDto> getUserByOpenId(@RequestParam("openId") String openId) {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", openId), Column.of("identity_type", IdentityTypeEnum.open_id)));
        if (memberAuth == null) return R.fail("用户不存在");
        Member member = memberService.getById(memberAuth.getMemberId());
        return R.data(MemberInfoDto.builder().userId(member.getId()).username(memberAuth.getUsername()).password(memberAuth.getPassword()).timeZone(member.getTimeZone()).status(member.getStatus()).build());
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
        return R.data(MemberInfoDto.builder().userId(member.getId()).username(memberAuth.getUsername()).password(memberAuth.getPassword()).timeZone(member.getTimeZone()).status(member.getStatus()).build());
    }

    @ApiOperation(value = "通过邮箱查询会员")
    @GetMapping("/get/email")
    public R<MemberInfoDto> getUserByEmail(@RequestParam("email") String email) {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", email), Column.of("identity_type", IdentityTypeEnum.email)));
        if (memberAuth == null) return R.fail("用户不存在");
        Member member = memberService.getById(memberAuth.getMemberId());
        return R.data(MemberInfoDto.builder().userId(member.getId()).username(memberAuth.getUsername()).password(memberAuth.getPassword()).status(member.getStatus()).timeZone(member.getTimeZone()).build());
    }

    @ApiOperation(value = "通过ID查询会员")
    @GetMapping("/get/id")
    public R<MemberInfoDto> getUserById(@RequestParam("id") Long id) {
        Member member = memberService.findMemberById(id);
        if (member == null) return R.fail("用户不存在");
        return R.data(MemberInfoDto.builder().userId(member.getId()).name(member.getName()).status(member.getStatus()).timeZone(member.getTimeZone()).build());
    }

    @ApiOperation(value = "通过IDS查询会员")
    @PostMapping("/list/ids")
    public R<List<MemberInfoDto>> listMemberByIds(@RequestBody List<String> ids) {
        List<Member> members = memberService.find(Column.of("id", ids, ColumnEnum.in));
        if (CollectionUtils.isEmpty(members)) return R.data(Lists.newArrayList());
        return R.data(members.stream().map(member -> {
            MemberInfoDto memberInfoDto = new MemberInfoDto();
            memberInfoDto.setName(member.getName());
            memberInfoDto.setAvatar(member.getAvatar());
            return memberInfoDto;
        }).collect(Collectors.toList()));
    }

    @ApiOperation(value = "会员注册")
    @PostMapping("/register")
    public R<String> register(@RequestBody MemberRegisterDto registerDto) {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", registerDto.getUsername()), Column.of("identity_type", IdentityTypeEnum.user_name)));
        if (memberAuth != null) return R.fail("账号已存在");
        String password = this.delegatingPassword(registerDto.getPassword()).replace("{bcrypt}", "");
        Member member = memberService.saveMemberByUserName(registerDto.getUsername(), password, DictCacheManager.getDictByCode(DictConstants.TIME_ZONE_TYPE, DictConstants.EAST_8_CODE).getValue());
        /* 添加日历 */
        CalendarInitDto calendarInitDto = new CalendarInitDto();
        calendarInitDto.setMemberId(member.getId());
        calendarInitDto.setMemberNickName(member.getName());
        calendarFeignClient.addCalendar(calendarInitDto);
        /* 添加注册消息到用户 */
        SpringContextHolder.publishEvent(new MemberRegisterEvent(this, member.getName(), JwtUtils.getUserId()));
        return R.status(true);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/modify/password")
    public R<String> modifyPassword(@RequestBody MemberModifyPasswordDto memberModifyPasswordDto) {
        Member member = memberService.getById(memberModifyPasswordDto.getMemberId());
        if (member == null) return R.fail("用户不存在");
        final String password = this.delegatingPassword(memberModifyPasswordDto.getPassword());
        List<MemberAuth> memberAuths = memberAuthService.find(Column.of("member_id", member.getId()));
        memberAuths.stream().forEach(item -> item.setPassword(password));
        memberAuthService.updateBatchById(memberAuths, memberAuths.size());
        return R.status(true);
    }

    private String delegatingPassword(String password) {
        return passwordEncoder.encode(password).replace(PasswordEncoderTypeEnum.BCRYPT.getPrefix(), "");
    }
}