/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberAppController
 * Author:   Administrator
 * Date:     2021/11/27 22:06
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.controller.app;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.com.xuct.calendar.common.core.enums.PasswordEncoderTypeEnum;
import cn.com.xuct.calendar.common.core.exception.SvrException;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.res.SvrResCode;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.enums.IdentityTypeEnum;
import cn.com.xuct.calendar.common.module.feign.WxUserPhoneFeignInfoReq;
import cn.com.xuct.calendar.common.module.params.*;
import cn.com.xuct.calendar.common.module.req.MemberGetPhoneReq;
import cn.com.xuct.calendar.common.module.vo.MemberPhoneAuthVo;
import cn.com.xuct.calendar.common.web.utils.JwtUtils;
import cn.com.xuct.calendar.common.web.utils.SpringContextHolder;
import cn.com.xuct.calendar.ums.api.entity.Member;
import cn.com.xuct.calendar.ums.api.entity.MemberAuth;
import cn.com.xuct.calendar.ums.api.feign.BasicServicesFeignClient;
import cn.com.xuct.calendar.ums.api.vo.MemberInfoVo;
import cn.com.xuct.calendar.ums.boot.event.MemberModifyNameEvent;
import cn.com.xuct.calendar.ums.boot.service.IMemberAuthService;
import cn.com.xuct.calendar.ums.boot.service.IMemberService;
import cn.com.xuct.calendar.ums.boot.support.SmsCodeValidateSupport;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
@Api(tags = "【移动端】会员接口")
@RequestMapping("/api/app/v1/member")
@RequiredArgsConstructor
public class MemberAppController {

    private final IMemberService memberService;

    private final IMemberAuthService memberAuthService;

    private final SmsCodeValidateSupport smsCodeValidateSupport;

    private final PasswordEncoder passwordEncoder;

    private final BasicServicesFeignClient basicServicesFeignClient;


    @ApiOperation(value = "获取用户基础信息及所有认证")
    @GetMapping("/info/all")
    public R<MemberInfoVo> getAllInfo() {
        Long userId = JwtUtils.getUserId();
        Member member = memberService.findMemberById(userId);
        if (member == null) return R.fail("获取用户信息失败");
        MemberInfoVo memberInfoVo = new MemberInfoVo();
        memberInfoVo.setMember(member);
        List<MemberAuth> memberAuths = memberAuthService.find(Column.of("member_id", member.getId()));
        memberAuths.stream().forEach(item -> item.setPassword(null));
        memberInfoVo.setAuths(memberAuths);
        return R.data(memberInfoVo);
    }

    @ApiOperation(value = "获取用户基础信息")
    @GetMapping("/info/base")
    public R<Member> getBaseInfo() {
        Long userId = JwtUtils.getUserId();
        Member member = memberService.findMemberById(userId);
        if (member == null) return R.fail("获取用户信息失败");
        return R.data(member);
    }

    @ApiOperation(value = "获取用户所有认证")
    @GetMapping("/auths")
    public R<List<MemberAuth>> auths() {
        Long userId = JwtUtils.getUserId();
        List<MemberAuth> memberAuths = memberAuthService.find(Column.of("member_id", userId));
        memberAuths.stream().forEach(item -> item.setPassword(null));
        return R.data(memberAuths);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/password")
    public R<String> modifyPassword(@Validated @RequestBody MemberPasswordParam param) {
        Long userId = JwtUtils.getUserId();
        String password = this.delegatingPassword(param.getPassword()).replace("{bcrypt}", "");
        List<MemberAuth> memberAuths = memberAuthService.find(Column.of("member_id", userId));
        memberAuths.stream().forEach(item -> item.setPassword(password));
        memberAuthService.updateBatchById(memberAuths, memberAuths.size());
        return R.status(true);
    }

    @ApiOperation(value = "修改名字")
    @PostMapping("/name")
    public R<String> modifyName(@Validated @RequestBody MemberNameParam param) {
        Long userId = JwtUtils.getUserId();
        Member member = memberService.findMemberById(userId);
        if (member == null) return R.fail("获取用户信息失败");
        member.setName(param.getName());
        memberService.updateMember(member);
        /* 发送修改名称事件 */
        SpringContextHolder.publishEvent(new MemberModifyNameEvent(this, userId, param.getName()));
        return R.status(true);
    }

    @ApiOperation(value = "查询名字")
    @GetMapping("/name")
    public R<String> getName(@RequestParam("memberId") Long memberId) {
        Member member = memberService.findMemberById(memberId);
        if (member == null) return R.fail("获取用户信息失败");
        return R.data(member.getName());
    }

    @ApiOperation(value = "修改头像")
    @PostMapping("/avatar")
    public R<String> modifyAvatar(@Validated @RequestBody MemberAvatarParam param) {
        Member member = memberService.findMemberById(JwtUtils.getUserId());
        if (member == null) return R.fail("获取用户信息失败");
        member.setAvatar(param.getAvatar());
        memberService.updateMember(member);
        return R.status(true);
    }

    @ApiOperation(value = "获取微信手机号")
    @PostMapping("/phone/get")
    public R<String> getWxPhone(@RequestBody MemberGetPhoneReq getPhoneReq) {
        Long userId = JwtUtils.getUserId();
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("member_id", userId), Column.of("identity_type", IdentityTypeEnum.open_id)));
        if (memberAuth == null) throw new SvrException(SvrResCode.UMS_MEMBER_AUTH_TYPE_ERROR);
        R<WxMaPhoneNumberInfo> wxMaPhoneNumberInfoR = basicServicesFeignClient.getPhoneNoInfo(WxUserPhoneFeignInfoReq.builder().sessionKey(memberAuth.getSessionKey()).encryptedData(getPhoneReq.getEncryptedData()).ivStr(getPhoneReq.getIvStr()).build());
        if (wxMaPhoneNumberInfoR == null || !wxMaPhoneNumberInfoR.isSuccess()) return R.fail("查询微信失败");
        return R.data(wxMaPhoneNumberInfoR.getData().getPhoneNumber());
    }

    @ApiOperation(value = "手机号绑定")
    @PostMapping("/phone/bind")
    public R<MemberPhoneAuthVo> bindPhone(@Validated @RequestBody MemberPhoneParam param) {
        Long userId = JwtUtils.getUserId();
        /* 1.验证验证码 */
        smsCodeValidateSupport.validateCode(1, String.valueOf(param.getPhone()), param.getCode());
        /* 2.查询绑定*/
        MemberPhoneAuthVo memberPhoneAuthVo = new MemberPhoneAuthVo();
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", param.getPhone()), Column.of("identity_type", IdentityTypeEnum.phone)));
        /* 3. 不存在直接绑定 */
        if (memberAuth == null) {
            memberPhoneAuthVo.setExist(false);
            memberAuth = new MemberAuth();
            memberAuth.setMemberId(userId);
            memberAuth.setUsername(param.getPhone());
            memberAuth.setIdentityType(IdentityTypeEnum.phone);
            memberAuthService.save(memberAuth);
            return R.data(memberPhoneAuthVo);
        }
        /*4. 判断电话下是否为唯一认证方式 */
        memberPhoneAuthVo.setExist(true);
        List<MemberAuth> memberAuths = memberAuthService.find(Column.of("member_id", memberAuth.getMemberId()));
        if (memberAuths.size() == 1) {
            memberPhoneAuthVo.setMerge(true);
            return R.data(memberPhoneAuthVo);
        }
        /*5.如果不唯一，则更新到新的用户下*/
        memberAuth.setMemberId(userId);
        memberAuthService.updateById(memberAuth);
        memberPhoneAuthVo.setMerge(false);
        return R.data(memberPhoneAuthVo);
    }

    @ApiOperation(value = "手机号解绑")
    @PostMapping("/phone/unbind")
    public R<String> unbindPhone(@Validated @RequestBody MemberPhoneParam param) {
        Long userId = JwtUtils.getUserId();
        /* 1.验证验证码 */
        smsCodeValidateSupport.validateCode(2, String.valueOf(param.getPhone()), param.getCode());
        /* 2.校验登陆方式 */
        List<MemberAuth> memberAuths = memberAuthService.find(Lists.newArrayList(Column.of("member_id", userId)));
        if (CollectionUtils.isEmpty(memberAuths) || memberAuths.size() == 1) return R.fail("仅存在唯一登陆方式");
        Optional<MemberAuth> optPhoneAuth = memberAuths.stream().filter(x -> x.getIdentityType().equals(IdentityTypeEnum.phone)).findFirst();
        if (!optPhoneAuth.isPresent()) return R.fail("未绑定手机");
        memberAuthService.removeById(optPhoneAuth.get().getId());
        return R.status(true);
    }

    @ApiOperation(value = "账号绑定")
    @PostMapping("/username/bind")
    public R<String> bindUserName(@RequestBody MemberUsernameParam param) {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", param.getUsername()), Column.of("identity_type", IdentityTypeEnum.user_name)));
        if (memberAuth != null) return R.fail("账号已存在");
        Long userId = JwtUtils.getUserId();
        memberAuth = new MemberAuth();
        memberAuth.setMemberId(userId);
        memberAuth.setUsername(param.getUsername());
        memberAuth.setPassword(delegatingPassword(param.getPassword()));
        memberAuth.setIdentityType(IdentityTypeEnum.user_name);
        memberAuthService.save(memberAuth);
        return R.status(true);
    }

    @ApiOperation(value = "邮箱绑定")
    @PostMapping("/email/bind")
    public R<String> bindEmail(@Validated @RequestBody EmailCodeParam param) {
        /* 1.验证验证码 */
        smsCodeValidateSupport.validateCode(3, String.valueOf(param.getEmail()), param.getCode());
        /* 2.读取记录 */
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", param.getEmail()), Column.of("identity_type", IdentityTypeEnum.email)));
        if (memberAuth != null) return R.fail("邮箱已存在");
        memberAuth = new MemberAuth();
        memberAuth.setMemberId(JwtUtils.getUserId());
        memberAuth.setUsername(param.getEmail());
        memberAuth.setIdentityType(IdentityTypeEnum.email);
        memberAuthService.save(memberAuth);
        return R.status(true);
    }

    @ApiOperation(value = "邮箱解绑")
    @PostMapping("/email/unbind")
    public R<String> unbindEmail(@Validated @RequestBody EmailCodeParam param) {
        /* 1.验证验证码 */
        smsCodeValidateSupport.validateCode(4, String.valueOf(param.getEmail()), param.getCode());
        /* 2.校验登陆方式 */
        List<MemberAuth> memberAuths = memberAuthService.find(Lists.newArrayList(Column.of("member_id", String.valueOf(JwtUtils.getUserId()))));
        if (CollectionUtils.isEmpty(memberAuths) || memberAuths.size() == 1) return R.fail("仅存在唯一登陆方式");
        Optional<MemberAuth> optPhoneAuth = memberAuths.stream().filter(x -> x.getIdentityType().equals(IdentityTypeEnum.email)).findFirst();
        if (!optPhoneAuth.isPresent()) return R.fail("未绑定邮箱");
        memberAuthService.removeById(optPhoneAuth.get().getId());
        return R.status(true);
    }


    private String delegatingPassword(String password) {
        return passwordEncoder.encode(password).replace(PasswordEncoderTypeEnum.BCRYPT.getPrefix(), "");
    }
}
