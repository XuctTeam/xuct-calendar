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

import cn.com.xuct.calendar.cms.api.entity.MemberCalendar;
import cn.com.xuct.calendar.cms.boot.mapper.MemberCalendarMapper;
import cn.com.xuct.calendar.common.module.req.MemberCalendarUpdateReq;
import cn.com.xuct.calendar.service.base.IBaseService;

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
     * 修改日历
     *
     * @param memberId
     * @param memberCalendar
     * @param memberCalendarUpdateReq
     */
    void updateMemberCalendar(Long memberId, MemberCalendar memberCalendar, MemberCalendarUpdateReq memberCalendarUpdateReq);
}