/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: UserServiceImpl
 * Author:   Derek Xu
 * Date:     2021/11/10 19:26
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.service.impl;

import cn.com.xuct.calendar.common.core.constant.RedisConstants;
import cn.com.xuct.calendar.common.core.vo.Column;
import cn.com.xuct.calendar.common.module.enums.IdentityTypeEnum;
import cn.com.xuct.calendar.ums.api.entity.Member;
import cn.com.xuct.calendar.ums.api.entity.MemberAuth;
import cn.com.xuct.calendar.ums.boot.mapper.MemberMapper;
import cn.com.xuct.calendar.common.db.service.BaseServiceImpl;
import cn.com.xuct.calendar.ums.boot.service.IGroupService;
import cn.com.xuct.calendar.ums.boot.service.IMemberAuthService;
import cn.com.xuct.calendar.ums.boot.service.IMemberMessageService;
import cn.com.xuct.calendar.ums.boot.service.IMemberService;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/10
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class MemberServiceImpl extends BaseServiceImpl<MemberMapper, Member> implements IMemberService {

    private final IMemberAuthService memberAuthService;
    private final IGroupService groupService;

    private final IMemberMessageService memberMessageService;

    @Override
    public Member findMemberById(Long id) {
        return super.getById(id);
    }

    @Override
    public Member updateMember(Member member) {
        super.updateById(member);
        return member;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Member saveMemberByOpenId(String openId, String nickName, String avatar, String sessionKey, String timeZone) {
        Member member = this.saveMember(timeZone, avatar);
        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setMemberId(member.getId());
        memberAuth.setIdentityType(IdentityTypeEnum.open_id);
        memberAuth.setNickName(nickName);
        memberAuth.setSessionKey(sessionKey);
        memberAuth.setUsername(openId);
        memberAuthService.save(memberAuth);
        return member;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Member saveMemberByUserName(String username, String password, String timeZone) {
        Member member = this.saveMember(timeZone, null);
        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setMemberId(member.getId());
        memberAuth.setIdentityType(IdentityTypeEnum.user_name);
        memberAuth.setUsername(username);
        memberAuth.setPassword(password);
        memberAuthService.save(memberAuth);
        return member;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Member saveMemberByPhone(String phone, String password, String timeZone) {
        Member member = this.saveMember(timeZone, null);
        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setMemberId(member.getId());
        memberAuth.setIdentityType(IdentityTypeEnum.phone);
        memberAuth.setUsername(phone);
        memberAuth.setPassword(password);
        memberAuthService.save(memberAuth);
        return member;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Member saveMemberByEmail(String email, String password, String timeZone) {
        Member member = this.saveMember(timeZone, null);
        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setMemberId(member.getId());
        memberAuth.setIdentityType(IdentityTypeEnum.email);
        memberAuth.setUsername(email);
        memberAuth.setPassword(password);
        memberAuthService.save(memberAuth);
        return member;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void mergeMember(Long memberId, MemberAuth memberAuth) {
        Long deleteMemberId = memberAuth.getMemberId();
        memberAuth.setMemberId(memberId);
        memberAuthService.updateById(memberAuth);
        /* 1.删除用户下创建所有的分组 */
        groupService.removeAllGroupByMemberId(deleteMemberId);
        /* 2.删除用户所有消息 */
        memberMessageService.removeAllMessageByMemberId(memberId);
        /*3.删除用户*/
        this.removeById(deleteMemberId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMemberById(Long memberId) {
        memberAuthService.delete(Column.of("member_id", memberId));
        this.removeById(memberId);
    }

    private Member saveMember(String timeZone, String avatar) {
        Member member = new Member();
        member.setStatus(0);
        member.setTimeZone(timeZone);
        if (StringUtils.hasLength(avatar)) {
            member.setAvatar(avatar);
        }
        member.setName("用户" + RandomUtil.randomString(6));
        this.save(member);
        return member;
    }
}