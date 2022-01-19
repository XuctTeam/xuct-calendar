/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: IUserService
 * Author:   Derek Xu
 * Date:     2021/11/10 19:25
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.service;

import cn.com.xuct.calendar.dao.entity.Member;
import cn.com.xuct.calendar.dao.mapper.MemberMapper;
import cn.com.xuct.calendar.service.base.IBaseService;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/11/10
 * @since 1.0.0
 */
public interface IMemberService extends IBaseService<MemberMapper, Member> {

    /**
     * 通过id查询，缓存
     *
     * @param id
     * @return
     */
    Member findMemberById(Long id);

    /**
     * 更新用户信息
     *
     * @param member
     */
    Member updateMember(Member member);

    /**
     * 新建账号
     *
     * @param openId
     * @param nickName
     * @param sessionKey
     * @param avatar
     * @param timeZone
     * @return
     */
    Member saveMemberByOpenId(String openId, String nickName, String avatar, String sessionKey, String timeZone);
}