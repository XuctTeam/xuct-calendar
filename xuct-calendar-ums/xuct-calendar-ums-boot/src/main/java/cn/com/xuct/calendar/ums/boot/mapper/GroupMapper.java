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

import cn.com.xuct.calendar.ums.api.dto.GroupInfoDto;
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
    List<GroupInfoDto> findGroupCountByMember(@Param("memberId") Long memberId);

    /**
     * 通过id查询群组
     *
     * @param id
     * @return
     */
    GroupInfoDto getGroupCountByGroupId(@Param("id") Long id);

    /**
     * 关键字查询
     *
     * @param word
     * @return
     */
    List<GroupInfoDto> findGroupBySearch(@Param("word") String word);

    /**
     * 我申请的群组
     *
     * @param memberId
     * @return
     */
    List<GroupInfoDto> mineApplyGroup(@Param("memberId") Long memberId);

    /**
     * 申请我的群组
     *
     * @param memberId
     * @return
     */
    List<GroupInfoDto> applyMineGroup(@Param("memberId") Long memberId);
}