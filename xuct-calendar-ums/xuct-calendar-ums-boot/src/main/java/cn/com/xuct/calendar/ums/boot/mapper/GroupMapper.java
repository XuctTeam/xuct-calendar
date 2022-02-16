/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberGroupMapper
 * Author:   Derek Xu
 * Date:     2022/2/7 15:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.mapper;

import cn.com.xuct.calendar.ums.api.dto.GroupCountDto;
import cn.com.xuct.calendar.ums.api.entity.Group;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Derek Xu
 * @create 2022/2/7
 * @since 1.0.0
 */
public interface GroupMapper extends BaseMapper<Group> {

    /**
     * 通过会员id查询群组
     *
     * @param memberId
     * @return
     */
    List<GroupCountDto> findGroupCountByMember(@Param("memberId") Long memberId);

    /**
     * 关键字查询
     *
     * @param wrod
     * @return
     */
    List<GroupCountDto> findGroupBySearch(@Param("word") String wrod);
}