/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: IMemberCalendarService
 * Author:   Derek Xu
 * Date:     2021/12/6 13:29
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.service;

import cn.com.xuct.calendar.cms.api.dodo.MemberMarjoCalendarDo;
import cn.com.xuct.calendar.cms.api.entity.MemberCalendar;
import cn.com.xuct.calendar.cms.boot.mapper.MemberCalendarMapper;
import cn.com.xuct.calendar.common.db.service.IBaseService;
import cn.com.xuct.calendar.common.module.req.MemberCalendarUpdateReq;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/6
 * @since 1.0.0
 */
public interface IMemberCalendarService extends IBaseService<MemberCalendarMapper, MemberCalendar> {

    /**
     * 查询用户的所有日历
     *
     * @param memberId
     * @return
     */
    List<MemberCalendar> queryMemberCalendar(Long memberId);

    /**
     * 查询用户主日历
     *
     * @param memberIds
     * @return
     */
    List<MemberMarjoCalendarDo> queryMarjoCalendarIds(List<String> memberIds);

    /**
     * 获取日历详情
     *
     * @param id
     * @return
     */
    MemberCalendar getMemberCalendar(Long id);


    /**
     * 新增日历
     *
     * @param memberId
     * @param memberCalendarUpdateReq
     */
    void createMemberCalendar(Long memberId, MemberCalendarUpdateReq memberCalendarUpdateReq);

    /**
     * 更新日历创建用户显示名称
     *
     * @param createMemberId
     * @param createMemberName
     */
    void updateMemberCalendarName(Long createMemberId, String createMemberName);


    /**
     * 修改日历
     *
     * @param memberId
     * @param memberCalendar
     * @param memberCalendarUpdateReq
     */
    void updateMemberCalendar(Long memberId, MemberCalendar memberCalendar, MemberCalendarUpdateReq memberCalendarUpdateReq);

    /**
     * 日历合并
     *
     * @param fromMemberId
     * @param memberId
     */
    void mergeMemberCalendar(Long fromMemberId, Long memberId);

    /**
     * 删除日历
     *
     * @param memberCalendarId
     * @param calendarId
     */
    void deleteCalendar(Long memberCalendarId, Long calendarId);
}