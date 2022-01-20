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
import cn.com.xuct.calendar.common.module.enums.IdentityTypeEnum;
import cn.com.xuct.calendar.ums.api.entity.Member;
import cn.com.xuct.calendar.ums.api.entity.MemberAuth;
import cn.com.xuct.calendar.ums.boot.mapper.MemberMapper;
import cn.com.xuct.calendar.service.base.BaseServiceImpl;
import cn.com.xuct.calendar.ums.boot.service.IMemberAuthService;
import cn.com.xuct.calendar.ums.boot.service.IMemberService;
import cn.hutool.core.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Cacheable(value = RedisConstants.MEMBER_INFO_KEY, key = "#id", unless = "#result == null")
    public Member findMemberById(Long id) {
        return super.getById(id);
    }

    @Override
    @CachePut(value = RedisConstants.MEMBER_INFO_KEY, key = "#member.id", unless = "#result == null")
    public Member updateMember(Member member) {
        super.updateById(member);
        return member;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Member saveMemberByOpenId(String openId, String nickName, String avatar, String sessionKey, String timeZone) {
        Member member = new Member();
        member.setStatus(0);
        member.setTimeZone(timeZone);
        member.setAvatar(avatar);
        member.setName("用户" + RandomUtil.randomString(6));
        this.save(member);
        MemberAuth memberAuth = new MemberAuth();
        memberAuth.setMemberId(member.getId());
        memberAuth.setIdentityType(IdentityTypeEnum.open_id);
        memberAuth.setNickName(nickName);
        memberAuth.setSessionKey(sessionKey);
        memberAuth.setUsername(openId);
        memberAuthService.save(memberAuth);
        return member;
    }


}