/**
 * Copyright (C), 2015-2022, XXX有限公司
 * FileName: MemberGroupMapper
 * Author:   Derek Xu
 * Date:     2022/2/7 16:14
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package cn.com.xuct.calendar.ums.boot.mapper;

import cn.com.xuct.calendar.ums.api.dto.GroupMemberInfoDto;
import cn.com.xuct.calendar.ums.api.entity.MemberGroup;
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
public interface MemberGroupMapper extends BaseMapper<MemberGroup> {

    /**
     * 群组内所有人
     *
     * @param memberId
     * @return
     */
    List<GroupMemberInfoDto> list(@Param("groupId") Long groupId, @Param("memberId") Long memberId);


    /**
     * 查询组下所有用户
     *
     * @param groupId
     * @return
     */
    List<Long> findMemberIdsByGroupId(@Param("groupId") Long groupId);

    /**
     * 通过群组查询下所有用户
     *
     * @param groupId
     * @return
     */
    List<GroupMemberInfoDto> queryMembersByGroupId(@Param("groupId") Long groupId, @Param("memberId") Long memberId);

    /**
     * 通过id查询
     *
     * @param memberIds
     * @return
     */
    List<GroupMemberInfoDto> queryMemberIds(@Param("memberIds") List<Long> memberIds);

    /**
     * 通过ID查询组内用户
     *
     * @param groupId
     * @param memberId
     * @return
     */
    GroupMemberInfoDto getGroupMember(@Param("groupId") Long groupId, @Param("memberId") Long memberId);
}