/**
 * Copyright (C), 2015-2021, XXX有限公司
 * FileName: MemberCalendarMapper
 * Author:   Derek Xu
 * Date:     2021/12/6 13:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.cms.boot.mapper;

import cn.com.xuct.calendar.cms.api.dodo.MemberMarjoCalendarDo;
import cn.com.xuct.calendar.cms.api.entity.MemberCalendar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2021/12/6
 * @since 1.0.0
 */
public interface MemberCalendarMapper extends BaseMapper<MemberCalendar> {

    /**
     * 查询用户所有日历
     *
     * @param memberId
     * @return
     */
    List<MemberCalendar> queryMemberCalendar(@Param("memberId") Long memberId);

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    MemberCalendar getMemberCalendar(@Param("id") Long id);

    /**
     * 查询主日历
     *
     * @param memberIds
     * @return
     */
    List<MemberMarjoCalendarDo> queryMarjoCalendarIds(@Param("memberIds") List<String> memberIds);
}