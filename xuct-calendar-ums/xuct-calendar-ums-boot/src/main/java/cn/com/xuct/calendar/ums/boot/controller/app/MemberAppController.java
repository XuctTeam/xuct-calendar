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
import cn.com.xuct.calendar.common.core.constant.CacheConstants;
import cn.com.xuct.calendar.common.core.constant.DictConstants;
import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.constant.SecurityConstants;
import cn.com.xuct.calendar.common.core.enums.PasswordEncoderTypeEnum;
import cn.com.xuct.calendar.common.core.exception.SvrException;
import cn.com.xuct.calendar.common.core.res.R;
import cn.com.xuct.calendar.common.core.res.SvrResCode;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.dto.CalendarMergeDto;
import cn.com.xuct.calendar.common.module.enums.IdentityTypeEnum;
import cn.com.xuct.calendar.common.module.enums.RegisterEnum;
import cn.com.xuct.calendar.common.module.feign.req.CalendarCountFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.CalendarInitFeignInfo;
import cn.com.xuct.calendar.common.module.feign.req.WxUserPhoneFeignInfo;
import cn.com.xuct.calendar.common.module.params.*;
import cn.com.xuct.calendar.common.module.vo.MemberPhoneAuthVo;
import cn.com.xuct.calendar.common.security.utils.SecurityUtils;
import cn.com.xuct.calendar.common.web.utils.SpringContextHolder;
import cn.com.xuct.calendar.ums.api.dto.MemberRegisterDto;
import cn.com.xuct.calendar.ums.api.entity.Member;
import cn.com.xuct.calendar.ums.api.entity.MemberAuth;
import cn.com.xuct.calendar.ums.api.feign.BasicServicesFeignClient;
import cn.com.xuct.calendar.ums.api.feign.CalendarFeignClient;
import cn.com.xuct.calendar.ums.api.vo.MemberInfoVo;
import cn.com.xuct.calendar.ums.boot.config.DictCacheManager;
import cn.com.xuct.calendar.ums.boot.event.MemberEvent;
import cn.com.xuct.calendar.ums.boot.service.IMemberAuthService;
import cn.com.xuct.calendar.ums.boot.service.IMemberService;
import cn.com.xuct.calendar.ums.boot.support.SmsCodeValidateSupport;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @date  2021/11/27
 * @since 1.0.0
 */
@Slf4j
@RestController
@Tag(name = "【移动端】会员接口")
@RequestMapping("/api/app/v1/member")
@RequiredArgsConstructor
public class MemberAppController {

    private final IMemberService memberService;

    private final IMemberAuthService memberAuthService;

    private final SmsCodeValidateSupport smsCodeValidateSupport;

    private final PasswordEncoder passwordEncoder;

    private final BasicServicesFeignClient basicServicesFeignClient;

    private final CalendarFeignClient calendarFeignClient;

    private final RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "获取用户基础信息及所有认证")
    @GetMapping("/info/all")
    public R<MemberInfoVo> getAllInfo() {
        Long userId = SecurityUtils.getUserId();
        Member member = memberService.findMemberById(userId);
        if (member == null) {
            return R.fail("获取用户信息失败");
        }
        MemberInfoVo memberInfoVo = new MemberInfoVo();
        memberInfoVo.setMember(member);
        List<MemberAuth> memberAuths = memberAuthService.find(Column.of("member_id", member.getId()));
        memberAuths.stream().forEach(item -> item.setPassword(null));
        memberInfoVo.setAuths(memberAuths);
        return R.data(memberInfoVo);
    }

    @Operation(summary = "获取用户基础信息")
    @GetMapping("/info/base")
    public R<Member> getBaseInfo() {
        Long userId = SecurityUtils.getUserId();
        Member member = memberService.findMemberById(userId);
        if (member == null) {
            return R.fail("获取用户信息失败");
        }
        return R.data(member);
    }

    @Operation(summary = "获取用户所有认证")
    @GetMapping("/auths")
    public R<List<MemberAuth>> auths() {
        Long userId = SecurityUtils.getUserId();
        List<MemberAuth> memberAuths = memberAuthService.find(Column.of("member_id", userId));
        memberAuths.stream().forEach(item -> item.setPassword(null));
        return R.data(memberAuths);
    }

    @Operation(summary = "获取用户微信认证")
    @GetMapping("/auth/wechat")
    public R<MemberAuth> getWxAuth() {
        return R.data(memberAuthService.get(Lists.newArrayList(Column.of("identity_type", IdentityTypeEnum.open_id), Column.of("member_id", SecurityUtils.getUserId()))));
    }

    @Operation(summary = "修改密码")
    @PostMapping("/password")
    public R<String> modifyPassword(@Validated @RequestBody MemberPasswordParam param) {
        Long userId = SecurityUtils.getUserId();
        String password = this.delegatingPassword(param.getPassword()).replace("{bcrypt}", "");
        List<MemberAuth> memberAuths = memberAuthService.find(Column.of("member_id", userId));
        memberAuths.stream().forEach(item -> item.setPassword(password));
        memberAuthService.updateBatchById(memberAuths, memberAuths.size());
        return R.status(true);
    }

    @Operation(summary = "修改名字")
    @PostMapping("/name")
    public R<Member> modifyName(@Validated @RequestBody MemberNameParam param) {
        Long userId = SecurityUtils.getUserId();
        Member member = memberService.findMemberById(userId);
        if (member == null) {
            return R.fail("获取用户信息失败");
        }
        member.setName(param.getName());
        memberService.updateMember(member);
        /* 发送修改名称事件 */
        SpringContextHolder.publishEvent(new MemberEvent(this, userId, param.getName(), 1));
        return R.data(member);
    }

    @Operation(summary = "查询名字")
    @GetMapping("/name")
    public R<String> getName(@RequestParam("memberId") Long memberId) {
        Member member = memberService.findMemberById(memberId);
        if (member == null) {
            return R.fail("获取用户信息失败");
        }
        return R.data(member.getName());
    }

    @Operation(summary = "修改头像")
    @PostMapping("/avatar")
    public R<Member> modifyAvatar(@Validated @RequestBody MemberAvatarParam param) {
        Member member = memberService.findMemberById(SecurityUtils.getUserId());
        if (member == null) {
            return R.fail("获取用户信息失败");
        }
        member.setAvatar(param.getAvatar());
        memberService.updateMember(member);
        return R.data(member);
    }

    @Operation(summary = "获取微信手机号")
    @PostMapping("/phone/get")
    public R<String> getWxPhone(@RequestBody MemberGetPhoneParam getPhoneReq) {
        Long userId = SecurityUtils.getUserId();
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("member_id", userId), Column.of("identity_type", IdentityTypeEnum.open_id)));
        if (memberAuth == null) {
            throw new SvrException(SvrResCode.UMS_MEMBER_AUTH_TYPE_ERROR);
        }
        R<WxMaPhoneNumberInfo> wxMaPhoneNumberInfoR = basicServicesFeignClient.getPhoneNoInfo(WxUserPhoneFeignInfo.builder().code(getPhoneReq.getCode()).build());
        if (wxMaPhoneNumberInfoR == null || !wxMaPhoneNumberInfoR.isSuccess()) {
            return R.fail("查询微信失败");
        }
        return R.data(wxMaPhoneNumberInfoR.getData().getPhoneNumber());
    }

    @Operation(summary = "手机号绑定")
    @PostMapping("/phone/bind")
    public R<MemberPhoneAuthVo> bindPhone(@Validated @RequestBody MemberPhoneParam param) {
        Long userId = SecurityUtils.getUserId();
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

    @Operation(summary = "手机号解绑")
    @PostMapping("/phone/unbind")
    public R<String> unbindPhone(@Validated @RequestBody MemberPhoneParam param) {
        Long userId = SecurityUtils.getUserId();
        /* 1.验证验证码 */
        smsCodeValidateSupport.validateCode(2, String.valueOf(param.getPhone()), param.getCode());
        /* 2.校验登陆方式 */
        List<MemberAuth> memberAuths = memberAuthService.find(Lists.newArrayList(Column.of("member_id", userId)));
        if (CollectionUtils.isEmpty(memberAuths) || memberAuths.size() == 1) {
            return R.fail("仅存在唯一登陆方式");
        }
        Optional<MemberAuth> optPhoneAuth = memberAuths.stream().filter(x -> x.getIdentityType().equals(IdentityTypeEnum.phone)).findFirst();
        if (!optPhoneAuth.isPresent()) {
            return R.fail("未绑定手机");
        }
        memberAuthService.removeById(optPhoneAuth.get().getId());
        return R.status(true);
    }

    @Operation(summary = "账号绑定")
    @PostMapping("/username/bind")
    public R<String> bindUserName(@RequestBody MemberUsernameParam param) {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", param.getUsername()), Column.of("identity_type", IdentityTypeEnum.user_name)));
        Assert.isNull(memberAuth != null, "账号已存在");
        Long userId = SecurityUtils.getUserId();
        memberAuth = new MemberAuth();
        memberAuth.setMemberId(userId);
        memberAuth.setUsername(param.getUsername());
        memberAuth.setPassword(delegatingPassword(param.getPassword()));
        memberAuth.setIdentityType(IdentityTypeEnum.user_name);
        memberAuthService.save(memberAuth);
        return R.status(true);
    }

    @Operation(summary = "邮箱绑定")
    @PostMapping("/email/bind")
    public R<String> bindEmail(@Validated @RequestBody EmailCodeParam param) {
        /* 1.验证验证码 */
        smsCodeValidateSupport.validateCode(3, String.valueOf(param.getEmail()), param.getCode());
        /* 2.读取记录 */
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", param.getEmail()), Column.of("identity_type", IdentityTypeEnum.email)));
        Assert.isNull(memberAuth != null, "邮箱已存在");
        memberAuth = new MemberAuth();
        memberAuth.setMemberId(SecurityUtils.getUserId());
        memberAuth.setUsername(param.getEmail());
        memberAuth.setIdentityType(IdentityTypeEnum.email);
        memberAuthService.save(memberAuth);
        return R.status(true);
    }

    @Operation(summary = "邮箱解绑")
    @PostMapping("/email/unbind")
    public R<String> unbindEmail(@Validated @RequestBody EmailCodeParam param) {
        /* 1.验证验证码 */
        smsCodeValidateSupport.validateCode(4, String.valueOf(param.getEmail()), param.getCode());
        /* 2.校验登陆方式 */
        List<MemberAuth> memberAuths = memberAuthService.find(Lists.newArrayList(Column.of("member_id", String.valueOf(SecurityUtils.getUserId()))));
        if (CollectionUtils.isEmpty(memberAuths) || memberAuths.size() == 1) {
            return R.fail("仅存在唯一登陆方式");
        }
        Optional<MemberAuth> optPhoneAuth = memberAuths.stream().filter(x -> x.getIdentityType().equals(IdentityTypeEnum.email)).findFirst();
        if (!optPhoneAuth.isPresent()) {
            return R.fail("未绑定邮箱");
        }
        memberAuthService.removeById(optPhoneAuth.get().getId());
        return R.status(true);
    }

    @Operation(summary = "使用微信头像昵称")
    @PostMapping("/wx/update/info")
    public R<Member> modifyNameAndAvatarToWx() {
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("member_id", SecurityUtils.getUserId()), Column.of("identity_type", IdentityTypeEnum.open_id)));
        Assert.notNull(memberAuth, "未找到微信信息");
        Member member = memberService.getById(SecurityUtils.getUserId());
        Assert.notNull(member, "获取用户信息失败");
        member.setName(memberAuth.getNickName());
        member.setAvatar(memberAuth.getAvatar());
        memberService.updateById(member);
        /* 发送修改名称事件 */
        SpringContextHolder.publishEvent(new MemberEvent(this, SecurityUtils.getUserId(), memberAuth.getNickName(), 2));
        return R.data(member);
    }

    @Operation(summary = "用户账号合并")
    @PostMapping("/merge")
    public R<String> mergeAccount(@Validated @RequestBody MemberMergeParam memberMergeParam) {
        Long userId = SecurityUtils.getUserId();
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("user_name", memberMergeParam.getPhone()), Column.of("identity_type", IdentityTypeEnum.phone)));
        Assert.notNull(memberAuth, "获取用户信息失败");
        R<Long> memberCalendarNumberR = calendarFeignClient.countCalendarNumberByMemberIds(CalendarCountFeignInfo.builder().memberIds(Lists.newArrayList(userId, memberAuth.getMemberId())).build(), SecurityConstants.FROM_IN);
        if (memberCalendarNumberR == null || !memberCalendarNumberR.isSuccess()) {
            return R.fail("获取用户日历失败");
        }
        if (memberCalendarNumberR.getData() > 5) {
            return R.fail("超过最大日历数");
        }
        R<String> mergeCalendarR = calendarFeignClient.mergeCalendar(CalendarMergeDto.builder().fromMemberId(memberAuth.getMemberId()).memberId(userId).build(), SecurityConstants.FROM_IN);
        if (mergeCalendarR == null || !mergeCalendarR.isSuccess()) {
            return R.fail("合并日历失败");
        }
        memberService.mergeMember(userId, memberAuth);
        /* 清除当前账号登录的缓存,避免合并和登录方式错误 */
        redisTemplate.delete(CacheConstants.USER_DETAILS.concat("::").concat(memberMergeParam.getPhone()));
        /* 发送账号合并消息 */
        SpringContextHolder.publishEvent(new MemberEvent(this, SecurityUtils.getUserId(), memberAuth.getNickName(), 3));
        return R.status(true);
    }

    @Operation(summary = "【非登录】找回密码更新")
    @PostMapping("/anno/forget/modify")
    public R<String> forgetPasswordModify(@Validated @RequestBody ForgetModifyParam param) {
        Member member = memberService.findMemberById(param.getMemberId());
        Assert.notNull(member, "修改密码失败");
        List<MemberAuth> auths = memberAuthService.find(Column.of("member_id", param.getMemberId()));
        Assert.notEmpty(auths, "修改密码失败");
        Optional<MemberAuth> authOpt = auths.stream().filter(auth -> auth.getIdentityType().equals(IdentityTypeEnum.phone)).findFirst();
        if (!authOpt.isPresent()) {
            authOpt = auths.stream().filter(auth -> auth.getIdentityType().equals(IdentityTypeEnum.email)).findFirst();
        }
        if (!authOpt.isPresent()) {
            return R.fail("修改密码失败");
        }
        String redisKey =
                (authOpt.get().getIdentityType().equals(IdentityTypeEnum.phone) ?
                        RedisConstants.MEMBER_FORGET_PASSWORD_PHONE_CODE_KEY : RedisConstants.MEMBER_FORGET_PASSWORD_EMAIL_CODE_KEY)
                        .concat(":").concat(authOpt.get().getUsername());
        Object redisVal = redisTemplate.opsForValue().get(redisKey);
        if (redisVal == null || !String.valueOf(redisVal).equals(param.getCode())) {
            return R.fail("修改密码失败");
        }
        /* 此时删除redis的验证码缓存 */
        redisTemplate.delete(redisKey);
        String bcryptPassword = this.delegatingPassword(param.getPassword()).replace("{bcrypt}", "");
        auths.forEach(auth -> auth.setPassword(bcryptPassword));
        memberAuthService.saveOrUpdateBatch(auths);
        return R.status(true);
    }

    @Operation(summary = "【非登录】找回密码验证")
    @PostMapping("/anno/forget/check")
    public R<String> forgetPasswordCheckCode(@Validated @RequestBody ForgetPasswordParam param) {
        Object redisVal = redisTemplate.opsForValue().get(param.getType() == 1 ?
                RedisConstants.MEMBER_FORGET_PASSWORD_PHONE_CODE_KEY.concat(":").concat(param.getPhone()) :
                RedisConstants.MEMBER_FORGET_PASSWORD_EMAIL_CODE_KEY.concat(":").concat(param.getEmail()));
        if (redisVal == null || !String.valueOf(redisVal).equals(param.getCode())) {
            return R.fail("验证码错误");
        }
        MemberAuth memberAuth = memberAuthService.get(Lists.newArrayList(Column.of("identity_type", param.getType() == 1 ? IdentityTypeEnum.phone : IdentityTypeEnum.email),
                Column.of("user_name", param.getType() == 1 ? param.getPhone() : param.getEmail())));

        if (memberAuth == null) {
            return R.fail("验证失败");
        }
        return R.data(memberAuth.getMemberId().toString());
    }

    @Operation(summary = "【非登录】用户注册")
    @PostMapping("/anno/register")
    public R<String> registerMember(@Validated @RequestBody MemberRegisterParam param) {
        Assert.notNull(param.getFormType(), "注册信息错误");
        if (param.getFormType().equals(RegisterEnum.username) && param.getUsername() == null ||
                param.getFormType().equals(RegisterEnum.phone) && param.getPhone() == null ||
                param.getFormType().equals(RegisterEnum.email) && param.getEmail() == null) {
            return R.fail("注册信息错误");
        }
        MemberRegisterDto registerDto = null;
        switch (param.getFormType()) {
            case username:
                registerDto = this.registerByUserName(param.getUsername().getUsername(), param.getUsername().getPassword(), param.getUsername().getRandomStr(), param.getUsername().getCaptcha());
                break;
            case phone:
                registerDto = this.registerByPhoneOrEmail(param.getPhone().getPhone(), null, param.getPhone().getPassword(), param.getPhone().getCode());
                break;
            case email:
                registerDto = this.registerByPhoneOrEmail(null, param.getEmail().getEmail(), param.getEmail().getPassword(), param.getEmail().getCode());
        }
        if (registerDto.getCode() == 0) {
            return R.status(true);
        }
        return R.fail(registerDto.getCode(), registerDto.getMessage());
    }

    private String delegatingPassword(String password) {
        return passwordEncoder.encode(password).replace(PasswordEncoderTypeEnum.BCRYPT.getPrefix(), "");
    }

    private MemberRegisterDto registerByUserName(final String username, final String password, final String randomStr, final String code) {
        String redisKey = RedisConstants.MEMBER_CAPTCHA_REGISTER_CODE_KEY.concat(randomStr);
        Object redisObj = redisTemplate.opsForValue().get(redisKey);
        if (redisObj == null || !String.valueOf(redisObj).equals(code)) {
            return MemberRegisterDto.builder().code(1000).message("图形码错误").build();
        }
        MemberAuth memberAuth = memberAuthService.get(Column.of("user_name", username));
        if (memberAuth != null) {
            return MemberRegisterDto.builder().code(2000).message("注册信息无效").build();
        }
        String bcryptPassword = this.delegatingPassword(password).replace("{bcrypt}", "");
        Member member = memberService.saveMemberByUserName(username, bcryptPassword, DictCacheManager.getDictByCode(DictConstants.TIME_ZONE_TYPE, DictConstants.EAST_8_CODE).getValue());
        boolean remoteCreateCalendar = this.createCalendar(member);
        if (!remoteCreateCalendar) {
            /* 删除保存的会员信息 */
            memberService.deleteMemberById(member.getId());
            return MemberRegisterDto.builder().code(3000).message("注册日程失败").build();
        }
        return MemberRegisterDto.builder().code(0).build();
    }

    private MemberRegisterDto registerByPhoneOrEmail(final String phone, final String email, final String password, final String code) {
        boolean isPhone = StringUtils.hasLength(phone);
        String redisKey = isPhone ? RedisConstants.MEMBER_PHONE_REGISTER_CODE_KEY.concat(":").concat(phone) : RedisConstants.MEMBER_EMAIL_REGISTER_CODE_KEY.concat(":").concat(email);
        Object redisObj = redisTemplate.opsForValue().get(redisKey);
        if (redisObj == null || !String.valueOf(redisObj).equals(code)) {
            return MemberRegisterDto.builder().code(1000).message("验证码无效").build();
        }
        List<Column> columns = isPhone ?
                Lists.newArrayList(Column.of("user_name", phone), Column.of("identity_type", IdentityTypeEnum.phone)) :
                Lists.newArrayList(Column.of("user_name", email), Column.of("identity_type", IdentityTypeEnum.email));
        MemberAuth memberAuth = memberAuthService.get(columns);
        if (memberAuth != null) {
            return MemberRegisterDto.builder().code(2000).message("注册信息无效").build();
        }
        String bcryptPassword = this.delegatingPassword(password).replace("{bcrypt}", "");
        Member member = isPhone ?
                memberService.saveMemberByPhone(phone, bcryptPassword, DictCacheManager.getDictByCode(DictConstants.TIME_ZONE_TYPE, DictConstants.EAST_8_CODE).getValue()) :
                memberService.saveMemberByEmail(email, bcryptPassword, DictCacheManager.getDictByCode(DictConstants.TIME_ZONE_TYPE, DictConstants.EAST_8_CODE).getValue());
        boolean remoteCreateCalendar = this.createCalendar(member);
        if (!remoteCreateCalendar) {
            /* 删除保存的会员信息 */
            memberService.deleteMemberById(member.getId());
            return MemberRegisterDto.builder().code(3000).message("注册日程失败").build();
        }
        redisTemplate.delete(redisKey);
        return MemberRegisterDto.builder().code(0).build();
    }


    private boolean createCalendar(final Member member) {
        /* 添加日历 */
        CalendarInitFeignInfo calendarInitFeignInfo = new CalendarInitFeignInfo();
        calendarInitFeignInfo.setMemberId(member.getId());
        calendarInitFeignInfo.setMemberNickName(member.getName());
        R<String> remoteRes = calendarFeignClient.addCalendar(calendarInitFeignInfo, SecurityConstants.FROM_IN);
        if (!remoteRes.isSuccess()){
            return false;
        }
        /* 添加注册消息到用户 */
        SpringContextHolder.publishEvent(new MemberEvent(this, member.getId(), member.getName(), 0));
        return true;
    }
}
