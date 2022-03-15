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
package cn.com.xuct.calendar.ums.boot.service;

import cn.com.xuct.calendar.common.db.service.IBaseService;
import cn.com.xuct.calendar.ums.api.entity.Member;
import cn.com.xuct.calendar.ums.boot.mapper.MemberMapper;

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
     * 通过openId保存用户
     *
     * @param openId
     * @param nickName
     * @param sessionKey
     * @param avatar
     * @param timeZone
     * @return
     */
    Member saveMemberByOpenId(String openId, String nickName, String avatar, String sessionKey, String timeZone);

    /**
     * 通过账号保存用户
     *
     * @param username
     * @param password
     * @param timeZone
     * @return
     */
    Member saveMemberByUserName(String username, String password, String timeZone);
}